package com.tadpolechain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.unity3d.player.UnityPlayer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsq on 2018/4/10.
 */
public class TCT {

    public static final int Status_Unavailable = 0;

    public static final int Status_Initial = 1;

    public static final int Status_Login = 200;

    public static final int Status_Online = 201;

    public static final int Status_Offline = 500;

    private static TCT tct;

    private static Context context;

    private static String AppKey;

    private static String SecretKey;

    protected static SharedPreferencesTool spt;

    private static String DEVICE_ID;

    private static String Token;

    protected static Game game;

    protected static User user;

    protected static Ad ad;

    protected static Settlement settlement;

    protected static TCTCallback loginCallBack;

    protected static TCTCallback adCallBack;

    protected static TCTCallback scoreCallBack;

    protected static TCTCallback dataCallBack;

    protected static TCTCallback activeCallBack;

    protected static TCTCallback settlementCallBack;

    protected static OnPayResultListener onPayResultListener;

    private static NoticeView noticeView;

    private static FloatButton floatBtn;

    public static String orientation = "0";

    private static Duration refresh;

    private static String version;

    public static float amount;

    public static String content;

    public static PayType payType;

    /**
     * 状态，0，起始状态，1，已经初始化，200，已经登录，201，已经上线，500，已经下线
     */
    private static int status = 0;

    private static float profit = -1;

    private static float palyDuration = -1;

    private static String msgId = "";

    protected static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(game == null || game.getStatus() == 0){
                return;
            }
            if(msg.what == 997){
                if(loginCallBack != null) {
                    loginCallBack.success();
                    loginCallBack = null;
                } else if(LoginScene != null){
                    unityLoginCallBack.success();
                }
                if(noticeView != null){
                    noticeView.online();
                    noticeView.addNotice("价值奖励已经开始计算，奖励数量每五分钟公告一次，也可以返回APP查询。");
                }
                if(floatBtn != null){
                    floatBtn.setVisibility(View.VISIBLE);
                    floatBtn.setOnClickListener(floatClickListener);
                    instance().loadAd(null);
                }
            } else if(msg.what == 998) {
                if (loginCallBack != null) {
                    loginCallBack.error(msg.what, (String)msg.obj);
                    loginCallBack = null;
                } else if(LoginScene != null){
                    unityLoginCallBack.error(msg.what, (String)msg.obj);
                }
            } else if(msg.what == 999) {
                if(loginCallBack != null) {
                    loginCallBack.success();
                    loginCallBack = null;
                } else if(OnlineScene != null) {
                    unityOnlineCallBack.success();
                }
                if(floatBtn != null){
                    floatBtn.setVisibility(View.VISIBLE);
                    floatBtn.setOnClickListener(floatClickListener);
                    instance().loadAd(null);
                }
            } else if(msg.what == 1000) {
                HttpBean bean = (HttpBean)msg.obj;
                if (loginCallBack != null) {
                    loginCallBack.error(bean.getCode(), bean.getMessage());
                    loginCallBack = null;
                } else if(OnlineScene != null){
                    unityOnlineCallBack.error(bean.getCode(), bean.getMessage());
                }
            } else if(msg.what == 1001) {
                if(profit > -1 && refresh.getProfit() > profit){
                    if(noticeView != null){
                        float p = refresh.getProfit() - profit;
                        if(p > 0.000001){
                            noticeView.addNotice(String.format("恭喜，您获得了 &%.6f TCT", p));
                        }
                    }
                }
                profit = refresh.getProfit();
                if(game != null && game.getReceiveTCTs() != null &&
                        game.getReceiveTCTs().length > 0){
                    for(int i = game.getReceiveTCTs().length - 1; i >= 0; i--){
                        if(refresh.getAllDuration() * 60 >= game.getReceiveTCTs()[i]){
                            if(palyDuration * 60 < game.getReceiveTCTs()[i]){
                                if(noticeView != null){
                                    noticeView.addNotice("您玩游戏的时长已经超过 " +
                                            game.getReceiveTCTs()[i] + " 分钟，请及时领取TCT币");
                                }
                            }
                            break;
                        }
                    }
                }
                palyDuration = refresh.getAllDuration();
                if(refresh.getMsgId() != null && refresh.getMsgId().length() > 0){
                    if(!refresh.getMsgId().equals(msgId)){
                        if(noticeView != null){
                            noticeView.addNotice(refresh.getMsgContent());
                        }
                    }
                }
                msgId = refresh.getMsgId();
                handler.postDelayed(duration, 5000);
            } else if(msg.what == 1003) {
                if(adCallBack != null){
                    adCallBack.success();
                    adCallBack = null;
                }
                if(floatBtn != null){
                    floatBtn.setVisibility(View.VISIBLE);
                    floatBtn.setOnClickListener(floatClickListener);
                    floatBtn.setImageResource(getResource(context,
                            "drawable", "ic_float_btn_ad"));
                }
            } else if(msg.what == 1004) {
                if(adCallBack != null){
                    HttpBean<Ad> ad = (HttpBean<Ad>)msg.obj;
                    adCallBack.error(ad.code, ad.message);
                    adCallBack = null;
                }
            } else if(msg.what == 1005) {
                if(scoreCallBack != null){
                    scoreCallBack.success();
                    scoreCallBack = null;
                }
            } else if(msg.what == 1006) {
                if(scoreCallBack != null){
                    HttpBean game = (HttpBean)msg.obj;
                    scoreCallBack.error(game.code, game.message);
                    scoreCallBack = null;
                }
            } else if(msg.what == 1007) {
                if(dataCallBack != null){
                    dataCallBack.success();
                    dataCallBack = null;
                }
            } else if(msg.what == 1008) {
                if(dataCallBack != null){
                    HttpBean game = (HttpBean)msg.obj;
                    dataCallBack.error(game.code, game.message);
                    dataCallBack = null;
                }
            } else if(msg.what == 1009) {
                if(activeCallBack != null){
                    activeCallBack.success();
                    activeCallBack = null;
                }
            } else if(msg.what == 1010) {
                if(activeCallBack != null){
                    HttpBean bean = (HttpBean)msg.obj;
                    activeCallBack.error(bean.code, bean.message);
                    activeCallBack = null;
                }
            } else if(msg.what == 1011) {
                if(settlementCallBack != null){
                    settlementCallBack.success();
                    settlementCallBack = null;
                }
            } else if(msg.what == 1012) {
                if(settlementCallBack != null){
                    HttpBean bean = (HttpBean)msg.obj;
                    settlementCallBack.error(bean.code, bean.message);
                    settlementCallBack = null;
                }
            }
        }
    };

    private TCT(){

    }

    public static TCT instance(){
        if(tct == null) {
            tct = new TCT();
        }
        return tct;
    }

    public void initialize(Context context, String appKey, String secretKey){
        initialize(context, appKey, secretKey, false);
    }

    public void initialize(Context context, String appKey, String secretKey, boolean test){
        if(test){
            Config.BaseUrl = Config.TestUrl;
        } else {
            Config.BaseUrl = Config.TCTUrl;
        }
        TCT.context = context;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(version == null || version.length() == 0){
            version = "9999";
        }
        AppKey = appKey;
        SecretKey = secretKey;
        status = 1;
        spt = new SharedPreferencesTool(context);
        DEVICE_ID = loadDeviceId(context);
        Intent intent = ((Activity)context).getIntent();
        if(intent != null){
            String session = intent.getStringExtra("session");
            String status = intent.getStringExtra("status");
            if(session != null && session.length() != 0){
                spt.putString("tct_login_token", session);
                spt.putLong("tct_login_token_time", System.currentTimeMillis());
                game = new Game();
                game.setStatus(Integer.parseInt(status));
            }
        }
        Token = spt.getString("tct_login_token", "");
        if(Token != null && Token.length() > 0){
            long times = spt.getLong("tct_login_token_time", 0);
            if(System.currentTimeMillis() + 3600000 < times + 864000000){
                status = 200;
            }
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = "0";
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientation = "1";
        } else {
            orientation = "0";
        }
        init();
    }

    public int getStatus() {
        return status;
    }

    public void setFloatBtn(FloatButton floatBtn) {
        TCT.floatBtn = floatBtn;
    }

    private static View.OnClickListener floatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(floatBtn.isMoving()) {
                floatBtn.setMoving(false);
            } else {
                if(ad == null){
                    TCT.instance().showTCT();
                } else {
                    floatBtn.setImageResource(getResource(context,
                            "drawable", "ic_float_btn"));
                    if(noticeView != null){
                        spt.putLong("tct_ad_show_time", System.currentTimeMillis());
                        noticeView.showAd();
                    } else {
                        ad = null;
                    }
                }
            }
        }
    };

    public void login(TCTCallback tctCallback){
        if(status == 0){
            if(tctCallback != null) {
                tctCallback.error(0, "SDK尚未初始化");
            }
            return;
        }
        if(game == null || game.getStatus() < 10){
            tctCallback.error(0, "内测中，暂不支持登录");
            return;
        }
        loginCallBack = tctCallback;
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onResume(TCTCallback tctCallback) {
        if(status >= 200) {
            online(tctCallback);
        }
    }

    public void onPause() {
        offline(null);
    }

    public void showTCT(){
        if(game == null || game.getStatus() == 0){
            return;
        }
        if(status < 200){
            return;
        }
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", "tct");
        context.startActivity(intent);
    }

    private void online(final TCTCallback tctCallback){
        handler.removeCallbacks(duration);
        String url = Config.BaseUrl + "sdk/game/online";
        loginCallBack = tctCallback;
        if(DEVICE_ID != null && DEVICE_ID.length() > 0){
            url += "?device_id=" + DEVICE_ID;
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "{}")).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<User> user = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<User>>(){}.getType());
                if(user != null && user.code == 200) {
                    TCT.user = user.data;
                    spt.putLong("tct_login_token_time", System.currentTimeMillis());
                    status = 201;
                    handler.sendEmptyMessage(999);
                    if(user.getData().getStatus() >= 200){
                        handler.postDelayed(duration, 5000);
                    }
                } else {
                    Message msg = new Message();
                    if(user != null) {
                        msg.what = 1000;
                        msg.obj = user;
                    } else {
                        msg.what = 1000;
                        HttpBean bean = new HttpBean();
                        bean.setCode(0);
                        bean.setMessage("网络错误");
                        msg.obj = bean;
                    }
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void clickAd(){

        if(ad == null){
            return;
        }

        String url = Config.BaseUrl + "sdk/ad/click/"+ad.getId();
        Log.e("test", url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "{}")).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    public void showAd(){

        if(ad == null){
            return;
        }

        String url = Config.BaseUrl + "sdk/ad/show/"+ad.getId();
        Log.e("test", url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "{}")).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    public void pay(final float amount, final String content, String scene, String method){

        PayScene = scene;
        PayMethod = method;
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OnPayResultListener onPayResultListener = new OnPayResultListener() {
                    @Override
                    public void success(int type, float amount, float tct, String orderId) {
                        unityPayCallBack.success();
                    }

                    @Override
                    public void fail(int code, String msg) {
                        unityPayCallBack.error(code, msg);
                    }
                };
                pay(amount, content, onPayResultListener);
            }
        });
    }

    public static String getPayType() {
        if(payType == null) {
            return "{}";
        } else {
            return new Gson().toJson(payType);
        }
    }

    public void pay(float amount, String content, OnPayResultListener onPayResultListener){

        if(game == null){
            if(onPayResultListener != null) {
                onPayResultListener.fail(500, "游戏SDK尚未初始化");
            }
        }
        if(user == null){
            if(onPayResultListener != null) {
                onPayResultListener.fail(500, "尚未登录");
            }
        }
        if(content == null || content == ""){
            if(onPayResultListener != null) {
                onPayResultListener.fail(501, "消费信息不能为空");
            }
        }
        if(amount <= 0){
            if(onPayResultListener != null) {
                onPayResultListener.fail(502, "消费金额必须大于0");
            }
        }

        TCT.onPayResultListener = onPayResultListener;
        TCT.amount = amount;
        TCT.content = content;

        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("type", "pay");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void offline(TCTCallback tctCallback){
        handler.removeCallbacks(duration);
        if(status < 200 || user == null || user.getStatus() < 200) {
            return;
        }
        status = 500;

        String url = Config.BaseUrl + "sdk/game/offline";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "{}")).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<Duration> data = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<Duration>>(){}.getType());

            }
        });
    }

    private void duration(){
        if(game == null || game.getStatus() == 0){
            return;
        }
        String url = Config.BaseUrl + "sdk/game/duration";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "{}")).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                handler.postDelayed(duration, 5000);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<Duration> data = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<Duration>>(){}.getType());
                // Log.e("duration", new Gson().toJson(data));
                if(data != null && data.code == 200) {
                    refresh = data.getData();
                    handler.sendEmptyMessage(1001);
                }
            }
        });
    }

    private static Runnable duration = new Runnable() {
        @Override
        public void run() {
            TCT.instance().duration();
        }
    };

    public User getUser() {
        return user;
    }

    protected Ad getAd() {
        return ad;
    }

    protected void setAd(Ad ad) {
        TCT.ad = ad;
    }

    public String getGameName() {
        if(game == null) {
            return null;
        } else {
            return game.getName();
        }
    }

    public String getGameSummary() {
        if(game == null) {
            return null;
        } else {
            return game.getSummary();
        }
    }

    protected void setNoticeView(NoticeView noticeView) {
        TCT.noticeView = noticeView;
    }

    protected void init(){
        String url = Config.BaseUrl + "sdk/user/init/" + AppKey + "?version=" + version;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<Game> game = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<Game>>(){}.getType());
                if(game != null && game.code == 200) {
                    TCT.game = game.data;
                }
            }
        });
    }

    protected void sendSms(final String code, final String telephone, final TCTCallback callback){
        String url = Config.BaseUrl + "sdk/sms_code/send";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", code);
        data.put("telephone", telephone);
        data.put("app_key", AppKey);
        String sign = "";
        if(DEVICE_ID != null && DEVICE_ID.length() > 0){
            data.put("device_id", DEVICE_ID);
            sign = "app_key=" + AppKey + "&code=" + code + "&device_id=" +
                    DEVICE_ID + "&telephone=" + telephone + "&secret_key=" + SecretKey;
        } else {
            sign = "app_key=" + AppKey + "&code=" + code +
                    "&telephone=" + telephone + "&secret_key=" + SecretKey;
        }
        data.put("sign", MD5.md5(sign));

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                new Gson().toJson(data))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean game = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean>(){}.getType());
                if(game != null && game.code == 200) {
                    if (callback != null) {
                        spt.putString("tct_login_telephone", telephone);
                        spt.putString("tct_login_code", code);
                        callback.success();
                    }
                } else {
                    if (callback != null) {
                        if(game != null) {
                            callback.error(game.getCode(), game.getMessage());
                        } else {
                            callback.error(0, "网络错误");
                        }
                    }
                }
            }
        });
    }

    protected void login(final String code, final String telephone,
                         String smsCode, final TCTCallback callback){
        String url = Config.BaseUrl + "sdk/sms_code/login";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", code);
        data.put("telephone", telephone);
        data.put("app_key", AppKey);
        data.put("sms_code", smsCode);
        String sign = "";
        if(DEVICE_ID != null && DEVICE_ID.length() > 0){
            data.put("device_id", DEVICE_ID);
            sign = "app_key=" + AppKey + "&code=" + code + "&device_id=" + DEVICE_ID +
                    "&sms_code=" + smsCode + "&telephone=" + telephone + "&secret_key=" + SecretKey;
        } else {
            sign = "app_key=" + AppKey + "&code=" + code + "&sms_code=" +
                    smsCode + "&telephone=" + telephone + "&secret_key=" + SecretKey;
        }
        data.put("sign", MD5.md5(sign));

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                        new Gson().toJson(data))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<User> user = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<User>>(){}.getType());
                if(user != null && user.code == 200) {
                    spt.putString("tct_login_telephone", telephone);
                    spt.putString("tct_login_code", code);
                    TCT.user = user.data;
                    spt.putLong("tct_login_token_time", System.currentTimeMillis());
                    spt.putString("tct_login_token", user.getData().getToken());
                    Token = user.getData().getToken();
                    status = 201;
                    if(callback != null) {
                        callback.success();
                    } else if(LoginScene != null) {
                        unityLoginCallBack.success();
                    }
                    if(user.getData().getStatus() >= 200){
                        handler.postDelayed(duration, 5000);
                    }
                } else {
                    if (callback != null) {
                        if(user != null) {
                            callback.error(user.getCode(), user.getMessage());
                        } else {
                            callback.error(0, "网络错误");
                        }
                    } else if(LoginScene != null) {
                        if(user != null) {
                            unityLoginCallBack.error(user.getCode(), user.getMessage());
                        } else {
                            unityLoginCallBack.error(0, "网络错误");
                        }
                    }
                }
            }
        });
    }

    protected void active(final String code, final TCTCallback callback){
        String url = Config.BaseUrl + "app/user/verify";
        activeCallBack = callback;

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", code);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                        new Gson().toJson(data))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean bean = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean>(){}.getType());
                if(bean != null && bean.code == 200) {
                    user.setStatus(201);
                    handler.sendEmptyMessage(1009);
                    handler.postDelayed(duration, 5000);
                } else {
                    Message msg = new Message();
                    if(bean != null) {
                        msg.what = 1010;
                        msg.obj = bean;
                    } else {
                        msg.what = 1010;
                        bean = new HttpBean();
                        bean.setCode(0);
                        bean.setMessage("网络错误");
                        msg.obj = bean;
                    }
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public String getOrientation() {
        return orientation;
    }

    protected void loadUserBalance(final TCTCallback callback){

        String url = Config.BaseUrl + "sdk/game/balance";
        adCallBack = callback;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                if(callback != null){
                    callback.error(0, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                Log.e("test", str);
                HttpBean<User> us = new Gson().fromJson(str,
                        new TypeToken<HttpBean<User>>(){}.getType());
                if(us != null && us.code == 200) {
                    if(callback != null){
                        TCT.user.setTct_balance(us.getData().getTct_balance());
                        TCT.user.setRmb_rate(us.getData().getRmb_rate());
                        TCT.user.setScore(us.getData().getScore());
                        callback.success();
                    }
                } else {
                    if(callback != null){
                        callback.error(us.code, us.getMessage());
                    }
                }
            }
        });
    }

    protected void pay(float amount, String content, final TCTCallback callback){

        String url = Config.BaseUrl + "sdk/game/pay";
        Log.e("test", url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("amount", amount);
        data.put("content", content);

        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                        new Gson().toJson(data))).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                if(callback != null){
                    callback.error(0, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                Log.e("test", str);
                HttpBean<PayType> us = new Gson().fromJson(str,
                        new TypeToken<HttpBean<PayType>>(){}.getType());
                if(us != null && us.code == 200) {
                    payType = us.data;
                    if(callback != null){
                        callback.success();
                    }
                } else {
                    if(callback != null){
                        callback.error(us.code, us.getMessage());
                    }
                }
            }
        });
    }

    protected void loadAd(final TCTCallback callback){

        long last = spt.getLong("tct_ad_show_time", 0);
        if(System.currentTimeMillis() - last < 600000){
            return;
        }

        String url = Config.BaseUrl + "sdk/ad/load";
        adCallBack = callback;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                Log.e("test", str);
                HttpBean<Ad> ad = new Gson().fromJson(str,
                        new TypeToken<HttpBean<Ad>>(){}.getType());
                if(ad != null && ad.code == 200) {
                    TCT.ad = ad.getData();
                    handler.sendEmptyMessage(1003);
                } else {
                    Message msg = new Message();
                    msg.what = 1004;
                    msg.obj = ad;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    protected void settlement(final TCTCallback callback){
        String url = Config.BaseUrl + "sdk/game/settlement";
        settlementCallBack = callback;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean<Settlement> settlement = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean<Settlement>>(){}.getType());
                if(settlement != null && settlement.code == 200) {
                    TCT.settlement = settlement.getData();
                    refresh.setAllDuration(0);
                    refresh.setExpectProfit(0);
                    handler.sendEmptyMessage(1011);
                } else {
                    Message msg = new Message();
                    msg.what = 1012;
                    msg.obj = settlement;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void sendScore(final float score, final TCTCallback callback){
        if(status != 201 && callback != null){
            callback.error(status, "当前不是在线状态");
        }
        scoreCallBack = callback;
        String url = Config.BaseUrl + "sdk/game/score";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("score", score);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                        new Gson().toJson(data))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean game = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean>(){}.getType());
                if(game != null && game.code == 200) {
                    handler.sendEmptyMessage(1005);
                } else {
                    Message msg = new Message();
                    msg.what = 1006;
                    msg.obj = game;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void sendData(final String key, final float score, final TCTCallback callback){
        if(status != 201 && callback != null){
            callback.error(status, "当前不是在线状态");
        }
        dataCallBack = callback;
        String url = Config.BaseUrl + "sdk/game/data";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("key", key);
        data.put("score", score);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + Token)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
                        new Gson().toJson(data))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                HttpBean game = new Gson().fromJson(response.body().string(),
                        new TypeToken<HttpBean>(){}.getType());
                if(game != null && game.code == 200) {
                    handler.sendEmptyMessage(1007);
                } else {
                    Message msg = new Message();
                    msg.what = 1008;
                    msg.obj = game;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private String loadDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String did = "";
        try {
            did = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(did == null || did.length() == 0){
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if(info.getMacAddress() == null || info.getMacAddress().length() == 0){
                String str = "" + Build.BOARD + Build.MODEL + Build.VERSION.RELEASE
                        + Build.BRAND + Build.TIME + Build.MANUFACTURER + Build.ID
                        + Build.DEVICE + Build.SERIAL;
                return MD5.md5(str);
            } else {
                return MD5.md5(info.getMacAddress()); // 使用mac地址代替DEVICE_ID
            }
        } else {
            return MD5.md5(did); // DEVICE_ID
        }
    }

    public static int getResource(Context context, String type, String name) {
        try {
            Class<?> arrayClass = getResourceClass(context, type).getClass();
            Field intField = arrayClass.getField(name);
            return intField.getInt(arrayClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static HashMap<String, Object> ResourceClass = new HashMap<String, Object>();

    private static Object getResourceClass(Context context, String type) {
        if (ResourceClass.containsKey(type)) {
            return ResourceClass.get(type);
        } else {
            try {
                Class<?> resource = Class.forName(context.getPackageName() + ".R");
                Class<?>[] classes = resource.getClasses();
                for (Class<?> c : classes) {
                    int i = c.getModifiers();
                    String className = c.getName();
                    String s = Modifier.toString(i);
                    if (s.contains("static") && className.contains(type)) {
                        ResourceClass.put(type, c.getConstructor().newInstance());
                        return ResourceClass.get(type);
                    } else {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public float getDuration() {
        if(refresh == null){
            return 0;
        }
        return refresh.getAllDuration();
    }

    public float getExpectProfit() {
        if(refresh == null){
            return 0;
        }
        return refresh.getExpectProfit();
    }

    // TODO 用于Unity调用的一些方法
    public void login(String scene, String method){
        LoginScene = scene;
        LoginMethod = method;
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                login(null);
            }
        });
    }

    public void online(String scene, String method){
        OnlineScene = scene;
        OnlineMethod = method;
    }

    public void showMsg(final String msg){
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(noticeView != null){
                    noticeView.addNotice(msg);
                }
            }
        });
    }

    public String getId() {
        if(user == null){
            return "";
        }
        return user.getId();
    }

    public int getAge() {
        if(user == null){
            return 0;
        }
        return user.getAge();
    }

    public int getSex() {
        if(user == null){
            return -1;
        }
        return user.getSex();
    }

    public String getNickname() {
        if(user == null){
            return "";
        }
        return user.getNickname();
    }

    public String getAvatar() {
        if(user == null){
            return "";
        }
        return user.getAvatar();
    }

    public float getProfit() {
        if(user == null){
            return 0;
        }
        return user.getProfit();
    }

    public void sendData(String key, float score, String scene, String method){
        ScoreScene = scene;
        ScoreMethod = method;
        sendData(key, score, unityScoreCallBack);
    }

    public void sendScore(float score, String scene, String method){
        DataScene = scene;
        DataMethod = method;
        sendScore(score, unityDataCallBack);
    }

    private static String LoginScene, LoginMethod, OnlineScene, OnlineMethod,
            ScoreScene, ScoreMethod, DataScene, DataMethod, PayScene, PayMethod;
    private static TCTCallback unityLoginCallBack = new TCTCallback() {
        @Override
        public void success() {
            UnityPlayer.UnitySendMessage(LoginScene, LoginMethod, "success");
        }

        @Override
        public void error(int errCode, String msg) {
            UnityPlayer.UnitySendMessage(LoginScene, LoginMethod,
                    "[" + errCode + "]" + msg);
        }
    };
    private static TCTCallback unityPayCallBack = new TCTCallback() {
        @Override
        public void success() {
            UnityPlayer.UnitySendMessage(PayScene, PayMethod, "success");
        }

        @Override
        public void error(int errCode, String msg) {
            UnityPlayer.UnitySendMessage(PayScene, PayMethod,
                    "[" + errCode + "]" + msg);
        }
    };
    private static TCTCallback unityOnlineCallBack = new TCTCallback() {
        @Override
        public void success() {
            UnityPlayer.UnitySendMessage(OnlineScene, OnlineMethod, "success");
        }

        @Override
        public void error(int errCode, String msg) {
            UnityPlayer.UnitySendMessage(OnlineScene, OnlineMethod,
                    "[" + errCode + "]" + msg);
        }
    };
    private static TCTCallback unityScoreCallBack = new TCTCallback() {
        @Override
        public void success() {
            UnityPlayer.UnitySendMessage(ScoreScene, ScoreMethod, "success");
        }

        @Override
        public void error(int errCode, String msg) {
            UnityPlayer.UnitySendMessage(ScoreScene, ScoreMethod,
                    "[" + errCode + "]" + msg);
        }
    };
    private static TCTCallback unityDataCallBack = new TCTCallback() {
        @Override
        public void success() {
            UnityPlayer.UnitySendMessage(DataScene, DataMethod, "success");
        }

        @Override
        public void error(int errCode, String msg) {
            UnityPlayer.UnitySendMessage(DataScene, DataMethod,
                    "[" + errCode + "]" + msg);
        }
    };

    public interface OnPayResultListener{
        public void success(int type, float amount, float tct, String orderId);
        public void fail(int code, String msg);
    }
}
