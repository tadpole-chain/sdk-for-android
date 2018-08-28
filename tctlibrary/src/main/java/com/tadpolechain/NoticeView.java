package com.tadpolechain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsq on 2018/4/11.
 */
public class NoticeView extends RelativeLayout {

    MarqueeTextView noticeText;

    RelativeLayout rlWelcome;

    ImageView avatar;

    TextView nickname;

    RelativeLayout adTop;

    ImageView adCloseTop;

    ImageView ivAdPicTop;

    RelativeLayout adBottom;

    ImageView adCloseBottom;

    ImageView ivAdPicBottom;

    List<String> notices = new ArrayList<>();

    Handler handler = new Handler();

    public NoticeView(Context context) {
        super(context);
        init(context);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoticeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        View.inflate(context, TCT.getResource(NoticeView.this.getContext(),
                "layout", "view_notice"), NoticeView.this);
        noticeText = (MarqueeTextView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "noticeText"));
        rlWelcome = (RelativeLayout) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "rlWelcome"));
        avatar = (ImageView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "avatar"));
        nickname = (TextView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "nickname"));
        adTop = (RelativeLayout) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "adTop"));
        adBottom = (RelativeLayout) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "adBottom"));
        adCloseTop = (ImageView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "ivCloseTop"));
        ivAdPicTop = (ImageView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "ivAdPicTop"));
        adCloseBottom = (ImageView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "ivCloseBottom"));
        ivAdPicBottom = (ImageView) this.findViewById(TCT.getResource(NoticeView.this.getContext(),
                "id", "ivAdPicBottom"));

        noticeText.setOnShowFinish(onShowFinish);
        TCT.instance().setNoticeView(this);
        if(TCT.instance().getStatus() == 201){
            noticeText.setText("价值奖励已经开始计算，奖励数量每五分钟公告一次，也可以返回APP查询。");
        } else {
            noticeText.setVisibility(View.GONE);
        }
    }

    public void showAd(){
        if(TCT.instance().getAd() == null){
            return;
        }
        adCloseTop.setOnClickListener(adClickListener);
        ivAdPicTop.setOnClickListener(adClickListener);
        adCloseBottom.setOnClickListener(adClickListener);
        ivAdPicBottom.setOnClickListener(adClickListener);
        if(TCT.instance().getAd().getAd_position().equals("LINK")){
            String url = TCT.instance().getAd().getAd_url();
            if(url != null && url.length() > 0){
                TCT.instance().clickAd();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
            TCT.instance().setAd(null);
        } else if(TCT.instance().getAd().getAd_position().equals("TOP")){
            TCT.instance().showAd();
            adTop.setVisibility(View.VISIBLE);
            String url = TCT.instance().getAd().getImage();
            if(TCT.orientation.equals("1")){
                url = TCT.instance().getAd().getImage_h();
            }
            Glide.with(getContext()).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            int w = resource.getIntrinsicWidth();
                            int h = resource.getIntrinsicHeight();
                            ViewGroup.LayoutParams lp = ivAdPicTop.getLayoutParams();
                            if(w < getWidth() * 0.8){
                                lp.width = w;
                                lp.height = h;
                            } else {
                                lp.width = getWidth() * 4 / 5;
                                lp.height = h * getWidth() * 4 / 5 / w;
                            }
                            ivAdPicTop.setLayoutParams(lp);
                            return false;
                        }
                    }).transform(new GlideCircleTransform(getContext(), false, 0.02F))
                    .error(getDrawable("ic_ad_bar")).placeholder(getDrawable("ic_ad_bar"))
                    .into(ivAdPicTop);
        } else if(TCT.instance().getAd().getAd_position().equals("BOTTOM")){
            TCT.instance().showAd();
            adBottom.setVisibility(View.VISIBLE);
            String url = TCT.instance().getAd().getImage();
            if(TCT.orientation.equals("1")){
                url = TCT.instance().getAd().getImage_h();
            }
            Glide.with(getContext()).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            int w = resource.getIntrinsicWidth();
                            int h = resource.getIntrinsicHeight();
                            ViewGroup.LayoutParams lp = ivAdPicBottom.getLayoutParams();
                            if(w < getWidth() * 0.8){
                                lp.width = w;
                                lp.height = h;
                            } else {
                                lp.width = getWidth() * 4 / 5;
                                lp.height = h * getWidth() * 4 / 5 / w;
                            }
                            ivAdPicBottom.setLayoutParams(lp);
                            return false;
                        }
                    }).transform(new GlideCircleTransform(getContext(), false, 0.02F))
                    .error(getDrawable("ic_ad_bar")).placeholder(getDrawable("ic_ad_bar"))
                    .into(ivAdPicBottom);
        } else if(TCT.instance().getAd().getAd_position().equals("CENTER")){
            TCT.instance().showAd();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type", "ad");
            getContext().startActivity(intent);
        }
    }

    public void addNotice(String notice) {
        if(TCT.instance().getStatus() == TCT.Status_Unavailable) {
            return;
        }
        if(notice == null || notice.length() == 0){
            return;
        }
        if(rlWelcome.getVisibility() == View.VISIBLE){
            notices.add(notice);
            return;
        }
        if(noticeText.getVisibility() == View.GONE){
            noticeText.setText(notice);
        } else {
            notices.add(notice);
        }
    }

    public void online(){
        if(TCT.instance().getStatus() == TCT.Status_Unavailable) {
            return;
        }
        if(noticeText.getVisibility() == View.VISIBLE){
            notices.add(0, noticeText.getText());
            noticeText.setVisibility(View.GONE);
        }
        rlWelcome.setVisibility(View.VISIBLE);
        nickname.setText(TCT.instance().getUser().getNickname());
        Glide.with(NoticeView.this.getContext()).load(TCT.instance().getUser().getAvatar())
                .transform(new GlideCircleTransform(NoticeView.this.getContext(), 0.2F))
                .error(TCT.getResource(NoticeView.this.getContext(),
                        "drawable", "ic_user_default")).placeholder(TCT.getResource(NoticeView.this.getContext(),
                "drawable", "ic_user_default"))
                .into(avatar);
        handler.postDelayed(hide, 3000);
    }

    MarqueeTextView.OnShowFinish onShowFinish = new MarqueeTextView.OnShowFinish() {
        @Override
        public void finish() {
            if(notices.size() > 0){
                noticeText.setText(notices.get(0));
                notices.remove(0);
            } else {
                noticeText.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener adClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == ivAdPicTop.getId()
                    || view.getId() == ivAdPicBottom.getId()) {
                if(TCT.instance().getAd() == null) {
                    return;
                }
                String url = TCT.instance().getAd().getAd_url();
                if(url != null && url.length() > 0){
                    TCT.instance().clickAd();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            }
            TCT.instance().setAd(null);
            adTop.setVisibility(View.GONE);
            adBottom.setVisibility(View.GONE);
        }
    };

    Runnable hide = new Runnable() {
        @Override
        public void run() {
            rlWelcome.setVisibility(View.GONE);
            if(notices.size() > 0){
                noticeText.setText(notices.get(0));
                notices.remove(0);
            }
        }
    };

    public int getId(String id){
        return TCT.getResource(getContext(),
                "id", id);
    }

    public int getDrawable(String id){
        return TCT.getResource(getContext(),
                "drawable", id);
    }
}
