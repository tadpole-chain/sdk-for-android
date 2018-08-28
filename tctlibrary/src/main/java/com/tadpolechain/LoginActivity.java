package com.tadpolechain;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class LoginActivity extends Activity {

    /**--------------------登录功能------------------**/

    TextView title;

    TextView login;

    TextView tvCode;

    TextView code_china;

    TextView code_us;

    TextView code_ca;

    TextView code_aus;

    ImageView cancel;

    TextView summary;

    TextView times;

    TextView login_msg;

    EditText telephone;

    EditText sms_code;

    String msg;

    LinearLayout select_code;

    String code = "86";

    long sendTime = 0;

    Handler handler = new Handler();

    /**--------------------广告功能------------------**/

    RelativeLayout adMiddle;

    ImageView adCloseMiddle;

    ImageView ivAdPicMiddle;

    /**--------------------领取TCT功能------------------**/

    ImageView tctClose;

    TextView tvNickname;

    TextView tvDuration;

    TextView tvProfit;

    TextView tvProfitInfo;

    TextView tvGet;

    LinearLayout llProfit;

    LinearLayout llInvite;

    EditText etCode;

    TextView tvMsg;

    TextView tvApp;

    /**--------------------支付功能------------------**/

    TextView payInfo;

    TextView tctBalance;

    TextView scoreBalance;

    TextView payAmount;

    TextView tvPay;

    int payStatus = 0;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);

        type = getIntent().getStringExtra("type");
        if(type != null && type.equals("ad")){
            initAd();
        } else if(type != null && type.equals("tct")){
            initTct();
        } else if(type != null && type.equals("pay")){
            initPay();
        } else {
            initLogin();
        }
    }

    public void initTct(){
        if (TCT.instance().getOrientation().equals("1")) {
            setContentView(TCT.getResource(LoginActivity.this,
                    "layout", "activity_tctv"));
        } else {
            setContentView(TCT.getResource(LoginActivity.this,
                    "layout", "activity_tcth"));
        }

        tctClose = (ImageView)findViewById(getId("ivClose"));
        tvNickname = (TextView) findViewById(getId("tvNickname"));
        tvDuration = (TextView) findViewById(getId("tvDuration"));
        tvProfit = (TextView) findViewById(getId("tvProfit"));
        tvProfitInfo = (TextView) findViewById(getId("tvProfitInfo"));
        tvGet = (TextView) findViewById(getId("tvGet"));
        llProfit = (LinearLayout) findViewById(getId("llProfit"));
        llInvite = (LinearLayout) findViewById(getId("llInvite"));
        etCode = (EditText) findViewById(getId("etCode"));
        tvMsg = (TextView) findViewById(getId("tvMsg"));
        tvApp = (TextView) findViewById(getId("tvApp"));

        tctClose.setOnClickListener(tctClickListener);
        tvGet.setOnClickListener(tctClickListener);
        tvApp.setOnClickListener(tctClickListener);
        tvNickname.setText("" + TCT.instance().getNickname());
        tvDuration.setText(String.format("%.2f", TCT.instance().getDuration()) + "小时");
        tvProfit.setText(String.format("%.2f", TCT.instance().getExpectProfit()) + "TCT");
        if(TCT.instance().getUser().getStatus() < 200){
            tvGet.setText("激活账户");
            tvProfit.setTextColor(0xff6b6b6b);
            tvProfitInfo.setTextColor(0xff6b6b6b);
        } else {
            tvGet.setText("领取资产");
            tvProfit.setTextColor(0xffffffff);
            tvProfitInfo.setTextColor(0xffcacbcd);
        }
    }

    public void initPay(){
        setContentView(TCT.getResource(LoginActivity.this,
                "layout", "activity_pay"));

        tctClose = (ImageView)findViewById(getId("cancel"));
        payInfo = (TextView) findViewById(getId("payInfo"));
        tctBalance = (TextView) findViewById(getId("tctBalance"));
        scoreBalance = (TextView) findViewById(getId("scoreBalance"));
        payAmount = (TextView) findViewById(getId("payAmount"));
        tvPay = (TextView) findViewById(getId("tvPay"));
        tvMsg = (TextView) findViewById(getId("tvMsg"));

        tctClose.setOnClickListener(payClickListener);
        tvPay.setOnClickListener(payClickListener);
        payInfo.setText(TCT.content + "【" + TCT.game.getName() + "】");
        tvPay.setText("加载中");
        payStatus = 1;

        TCT.instance().loadUserBalance(new TCTCallback() {
            @Override
            public void success() {
                handler.post(getBanlance);
                payStatus = 2;
            }

            @Override
            public void error(int errCode, String msg) {
                handler.post(new BanlanceError(errCode, msg));
            }
        });
    }

    public class BanlanceError implements Runnable {
        private int errCode;
        private String msg;

        public BanlanceError(int errCode, String msg){
            this.errCode = errCode;
            this.msg = msg;
        }

        @Override
        public void run() {
            if(TCT.onPayResultListener != null){
                TCT.onPayResultListener.fail(errCode, msg);
            } else {
                Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    };

    Runnable getBanlance = new Runnable() {
        @Override
        public void run() {
            tctBalance.setText(String.format("可用：%.4f tct", TCT.user.getTct_balance()));
            scoreBalance.setText("可用：" + TCT.user.getScore());
            payAmount.setText(String.format("%.4f", TCT.amount / TCT.user.getRmb_rate()));
            if(TCT.amount / TCT.user.getRmb_rate() > TCT.user.getTct_balance()) {
                tvMsg.setVisibility(View.VISIBLE);
                tvPay.setTextColor(0xff606b8d);
                tvPay.setBackgroundResource(getDrawable("btn_pay_un"));
            }
            tvPay.setText("支付");
        }
    };

    public void initAd(){
        setContentView(TCT.getResource(LoginActivity.this,
                "layout", "activity_ad"));
        adMiddle = (RelativeLayout) findViewById(getId("adMiddle"));
        adCloseMiddle = (ImageView)findViewById(getId("ivCloseMiddle"));
        ivAdPicMiddle = (ImageView)findViewById(getId("ivAdPicMiddle"));

        adCloseMiddle.setOnClickListener(adClickListener);
        ivAdPicMiddle.setOnClickListener(adClickListener);

        if(TCT.instance().getAd().getAd_position() == null ||
                TCT.instance().getAd().getAd_position().equals("CENTER")){
            String url = TCT.instance().getAd().getImage();
            if(TCT.orientation.equals("1")){
                url = TCT.instance().getAd().getImage_h();
            }
            adMiddle.setVisibility(View.VISIBLE);
            Glide.with(LoginActivity.this).load(url)
                    .transform(new GlideCircleTransform(LoginActivity.this, false, 0.05F))
                    .error(getDrawable("ic_ad_default")).placeholder(getDrawable("ic_ad_default"))
                    .into(ivAdPicMiddle);
        } else {
            finish();
            TCT.instance().setAd(null);
        }
    }

    public void initLogin(){
        setContentView(TCT.getResource(LoginActivity.this,
                "layout", "activity_login"));

        login = (TextView)findViewById(getId("login"));
        cancel = (ImageView)findViewById(getId("cancel"));
        times = (TextView)findViewById(getId("times"));
        title = (TextView)findViewById(getId("title"));
        tvCode = (TextView)findViewById(getId("code"));
        summary = (TextView)findViewById(getId("summary"));
        code_china = (TextView)findViewById(getId("code_china"));
        code_us = (TextView)findViewById(getId("code_us"));
        code_ca = (TextView)findViewById(getId("code_ca"));
        code_aus = (TextView)findViewById(getId("code_aus"));
        login_msg = (TextView)findViewById(getId("login_msg"));
        telephone = (EditText)findViewById(getId("telephone"));
        sms_code = (EditText)findViewById(getId("sms_code"));
        select_code = (LinearLayout) findViewById(getId("select_code"));

        cancel.setOnClickListener(clickListener);
        tvCode.setOnClickListener(clickListener);
        code_china.setOnClickListener(clickListener);
        code_us.setOnClickListener(clickListener);
        code_ca.setOnClickListener(clickListener);
        code_aus.setOnClickListener(clickListener);

        String tt = TCT.instance().getGameName();
        if(tt != null && tt.length() > 0){
            title.setText(tt);
        }
        String sm = TCT.instance().getGameSummary();
        if(sm != null && sm.length() > 0){
            summary.setText(sm);
        }
        if(TCT.spt.getString("tct_login_telephone", "").length() > 0){
            telephone.setText(TCT.spt.getString("tct_login_telephone", ""));
            times.setOnClickListener(clickListener);
            times.setBackgroundResource(getDrawable("btn_send_code"));
        }
        if(TCT.spt.getString("tct_login_code", "").length() > 0){
            code = TCT.spt.getString("tct_login_code", "");
            tvCode.setText("+" + code + "▼");
        }

        telephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(telephone.getText().toString().length() > 0){
                    times.setOnClickListener(clickListener);
                    times.setBackgroundResource(getDrawable("btn_send_code"));
                } else {
                    times.setOnClickListener(null);
                    times.setBackgroundResource(getDrawable("btn_send_code_hint"));
                }
            }
        });

        sms_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(sms_code.getText().toString().length() > 0){
                    login.setOnClickListener(clickListener);
                    login.setTextColor(0xff333333);
                } else {
                    login.setOnClickListener(null);
                    login.setTextColor(0xff99ccff);
                }
            }
        });
    }

    View.OnClickListener payClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == tctClose.getId()) {
                if(TCT.onPayResultListener != null){
                    TCT.onPayResultListener.fail(100, "您取消了支付");
                } else {
                    Toast.makeText(LoginActivity.this, "您取消了支付", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else if(view.getId() == tvPay.getId()){
                if(payStatus < 2) {
                    Toast.makeText(LoginActivity.this, "正在加载数据", Toast.LENGTH_SHORT).show();
                } else if(TCT.amount / TCT.user.getRmb_rate() > TCT.user.getTct_balance()) {
                    Toast.makeText(LoginActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    if(payStatus == 3){
                        Toast.makeText(LoginActivity.this, "正在支付中……", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    payStatus = 3;
                    TCT.instance().pay(TCT.amount, TCT.content, new TCTCallback() {
                        @Override
                        public void success() {
                            payStatus = 4;
                            handler.post(paySuccess);
                        }

                        @Override
                        public void error(int errCode, String msg) {
                            payStatus = 4;
                            handler.post(new BanlanceError(errCode, msg));
                        }
                    });
                }
            }
        }
    };

    Runnable paySuccess = new Runnable() {
        @Override
        public void run() {
            if(TCT.onPayResultListener != null){
                if(TCT.payType == null){
                    TCT.onPayResultListener.success(0, 0, 0, "");
                } else {
                    TCT.onPayResultListener.success(TCT.payType.getType(),
                            TCT.payType.getAmount(), TCT.payType.getProfit(), TCT.payType.getId());
                }
            }
            finish();
        }
    };

    View.OnClickListener tctClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == tctClose.getId()) {
                finish();
            } else if(view.getId() == tvGet.getId()){
                if(TCT.instance().getUser().getStatus() < 200){
                    if(llProfit.getVisibility() == View.VISIBLE) {
                        llProfit.setVisibility(View.GONE);
                        llInvite.setVisibility(View.VISIBLE);
                    } else {
                        // 激活账号的操作
                        if(etCode.getText().toString().length() == 0){
                            tvMsg.setText("邀请码不能为空");
                        } else {
                            tvMsg.setText("正在激活……");
                            TCT.instance().active(etCode.getText().toString(), activeTCTCallback);
                            tvGet.setOnClickListener(null);
                        }
                    }
                } else {
                    if(TCT.game != null && TCT.game.getStatus() < 200) {
                        Toast.makeText(LoginActivity.this, "游戏尚未上链，不能领取", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        // 领取TCT的操作
                        TCT.instance().settlement(settleTCTCallback);
                        tvGet.setOnClickListener(null);
                    }
                }
            } else {
                if(TCT.instance().getUser() == null) {
                    return;
                }
                String url = TCT.instance().getUser().getAppUrl();
                if(url != null && url.length() > 0){
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                finish();
            }
        }
    };

    TCTCallback activeTCTCallback = new TCTCallback() {
        @Override
        public void success() {
            tvMsg.setText(" ");
            Toast.makeText(LoginActivity.this, "激活成功", Toast.LENGTH_LONG).show();
            llProfit.setVisibility(View.VISIBLE);
            llInvite.setVisibility(View.GONE);
            tvGet.setText("领取资产");
            tvProfit.setTextColor(0xffffffff);
            tvProfitInfo.setTextColor(0xffcacbcd);
            tvGet.setOnClickListener(tctClickListener);
        }

        @Override
        public void error(int errCode, String msg) {
            tvMsg.setText("" + msg);
            tvGet.setOnClickListener(tctClickListener);
        }
    };

    TCTCallback settleTCTCallback = new TCTCallback() {
        @Override
        public void success() {
            Toast.makeText(LoginActivity.this, String.format("领取成功：%.6fTCT", TCT.settlement.getProfit()),
                    Toast.LENGTH_LONG).show();
            tvDuration.setText("0小时");
            tvProfit.setText("0tct");
            tvGet.setOnClickListener(tctClickListener);
        }

        @Override
        public void error(int errCode, String msg) {
            Toast.makeText(LoginActivity.this, "领取失败：" + msg,
                    Toast.LENGTH_LONG).show();
            tvGet.setOnClickListener(tctClickListener);
        }
    };

    View.OnClickListener adClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == adCloseMiddle.getId()) {
                finish();
            } else if(view.getId() == ivAdPicMiddle.getId()) {
                if(TCT.instance().getAd() == null) {
                    return;
                }
                String url = TCT.instance().getAd().getAd_url();
                if(url != null && url.length() > 0){
                    TCT.instance().clickAd();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                finish();
            }
            TCT.instance().setAd(null);
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == login.getId()) {
                login.setOnClickListener(null);
                login.setTextColor(0xff99ccff);
                login.setText("正在登录...");
                TCT.instance().login(code, telephone.getText().toString(),
                        sms_code.getText().toString(), loginTCTCallback);
            } else if(view.getId() == cancel.getId()) {
                if(select_code.getVisibility() == View.VISIBLE){
                    select_code.setVisibility(View.GONE);
                } else {
                    finish();
                    Message msg = new Message();
                    msg.what = 998;
                    msg.obj = "您取消了登录";
                    TCT.handler.sendMessage(msg);
                }
            } else if(view.getId() == times.getId()) {
                if(code.equals("86")){
                    if(!MD5.isChinaPhone(telephone.getText().toString())){
                        login_msg.setText("请输入正确的手机号");
                        return;
                    }
                } else {
                    if(!MD5.isOtherPhone(telephone.getText().toString(), 10)){
                        login_msg.setText("请输入正确的手机号");
                        return;
                    }
                }
                times.setOnClickListener(null);
                times.setBackgroundResource(getDrawable("btn_send_code_hint"));
                times.setText("正在发送...");
                TCT.instance().sendSms(code, telephone.getText().toString(), smsTCTCallback);
            } else if(view.getId() == tvCode.getId()) {
                select_code.setVisibility(View.VISIBLE);
                code_china.setBackgroundResource(getDrawable("btn_con_code_hint"));
                code_china.setTextColor(0xff3d476b);
                code_us.setBackgroundResource(getDrawable("btn_con_code_hint"));
                code_us.setTextColor(0xff3d476b);
                code_ca.setBackgroundResource(getDrawable("btn_con_code_hint"));
                code_ca.setTextColor(0xff3d476b);
                code_aus.setBackgroundResource(getDrawable("btn_con_code_hint"));
                code_aus.setTextColor(0xff3d476b);
                if(code.equals("86")){
                    code_china.setBackgroundResource(getDrawable("btn_con_code"));
                    code_china.setTextColor(0xff1dddc6);
                } else if(code.equals("1")){
                    code_us.setBackgroundResource(getDrawable("btn_con_code"));
                    code_us.setTextColor(0xff1dddc6);
                } else if(code.equals("61")){
                    code_aus.setBackgroundResource(getDrawable("btn_con_code"));
                    code_aus.setTextColor(0xff1dddc6);
                }
            } else if(view.getId() == code_china.getId()) {
                select_code.setVisibility(View.GONE);
                code = "86";
                tvCode.setText("+86▼");
            } else if(view.getId() == code_us.getId()) {
                select_code.setVisibility(View.GONE);
                code = "1";
                tvCode.setText("+1▼");
            } else if(view.getId() == code_ca.getId()) {
                select_code.setVisibility(View.GONE);
                code = "1";
                tvCode.setText("+1▼");
            } else if(view.getId() == code_aus.getId()) {
                select_code.setVisibility(View.GONE);
                code = "61";
                tvCode.setText("+61▼");
            }
            if(select_code.getVisibility() == View.VISIBLE){
                telephone.setVisibility(View.INVISIBLE);
                tvCode.setVisibility(View.INVISIBLE);
                sms_code.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                times.setVisibility(View.INVISIBLE);
            } else {
                telephone.setVisibility(View.VISIBLE);
                tvCode.setVisibility(View.VISIBLE);
                sms_code.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                times.setVisibility(View.VISIBLE);
            }
        }
    };

    TCTCallback smsTCTCallback = new TCTCallback() {
        @Override
        public void success() {
            LoginActivity.this.msg = "短信发送成功";
            sendTime = System.currentTimeMillis();
            handler.post(show);
            handler.post(update);
        }

        @Override
        public void error(int errCode, String msg) {
            LoginActivity.this.msg = msg;
            handler.post(show);
        }
    };

    TCTCallback loginTCTCallback = new TCTCallback() {
        @Override
        public void success() {
            LoginActivity.this.msg = "登录成功";
            handler.post(loginSuccess);
        }

        @Override
        public void error(int errCode, String msg) {
            LoginActivity.this.msg = msg;
            handler.post(show);
        }
    };

    Runnable update = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() - sendTime < 60000){
                times.setOnClickListener(null);
                times.setBackgroundResource(getDrawable("btn_send_code_hint"));
                times.setText((60 - (System.currentTimeMillis() - sendTime) / 1000) + "秒后重发");
                handler.postDelayed(update, 200);
            } else {
                times.setText("发送验证码");
                if(telephone.getText().toString().length() > 0){
                    times.setOnClickListener(clickListener);
                    times.setBackgroundResource(getDrawable("btn_send_code"));
                } else {
                    times.setOnClickListener(null);
                    times.setBackgroundResource(getDrawable("btn_send_code_hint"));
                }
            }
        }
    };

    Runnable show = new Runnable() {
        @Override
        public void run() {
            if(msg != null && msg.length() > 0){
                login_msg.setText(msg);
            } else {
                login_msg.setText("");
            }
            if(sms_code.getText().toString().length() > 0){
                login.setOnClickListener(clickListener);
                login.setTextColor(0xff333333);
                login.setText("登录");
            }
            if(telephone.getText().toString().length() > 0){
                times.setOnClickListener(clickListener);
                times.setBackgroundResource(getDrawable("btn_send_code"));
                times.setText("发送验证码");
            }
        }
    };

    Runnable loginSuccess = new Runnable() {
        @Override
        public void run() {
            finish();
            TCT.handler.sendEmptyMessage(997);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(type != null && type.equals("tct") && TCT.instance().getOrientation().equals("0")){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = screenWidth * 3 / 4;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
        }
    }

    public int getId(String id){
        return TCT.getResource(LoginActivity.this,
                "id", id);
    }

    public int getDrawable(String id){
        return TCT.getResource(LoginActivity.this,
                "drawable", id);
    }
}
