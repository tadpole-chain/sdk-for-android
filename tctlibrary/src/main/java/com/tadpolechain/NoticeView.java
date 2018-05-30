package com.tadpolechain;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        noticeText.setOnShowFinish(onShowFinish);
        TCT.instance().setNoticeView(this);
        if(TCT.instance().getStatus() == 201){
            noticeText.setText("价值奖励已经开始计算，奖励数量每五分钟公告一次，也可以返回APP查询。");
        } else {
            noticeText.setVisibility(View.GONE);
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
}
