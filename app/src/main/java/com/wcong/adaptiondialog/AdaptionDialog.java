package com.wcong.adaptiondialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.wcong.adaptiondialog.databinding.DialogAdaptionBinding;

/**
 * Created by wangcong on 2017/6/12.
 * <p>
 */

public class AdaptionDialog extends PopupWindow {

    private Context context;

    private View baseView;
    private View window;
    private View triangle;

    private LinearLayout windowLayout;

    private int baseViewLocation[];

    private DialogAdaptionBinding windowBinding;

    /**
     * @param context
     * @param baseView
     * @param resLayout
     */
    public AdaptionDialog(Context context, View baseView, @LayoutRes int resLayout
    ) {
        super(context);
        this.context = context;
        this.baseView = baseView;

        setBaseViewLocation();
        init(LayoutInflater.from(context).inflate(resLayout, null));
    }

    /**
     * @param context
     * @param baseView
     * @param windowView
     */
    public AdaptionDialog(Context context, View baseView, View windowView
    ) {
        super(context);
        this.context = context;
        this.baseView = baseView;

        setBaseViewLocation();
        init(windowView);
    }

    private void init(View windowView) {
        windowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_adaption,
                null, false);
        windowBinding.setPresenter(new Presenter());
        window = windowView;

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        //初始化三角形
        triangle = new View(context);
        triangle.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.triangle));
        LinearLayout.LayoutParams triangleParams = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
        //可以动态去修改三角的左右边距，已达到想要的效果，默认15dp
        triangleParams.setMargins(dip2px(15), 0, 0, 0);
        triangle.setLayoutParams(triangleParams);

        //初始化用于放置悬浮框的layout，包含三角及提示框两个元素
        windowLayout = new LinearLayout(context);
        windowLayout.setOrientation(LinearLayout.VERTICAL);
        windowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        windowLayout.setPadding(dip2px(10), dip2px(5), dip2px(10), dip2px(5));
        windowLayout.addView(triangle);
        windowLayout.addView(window);
        windowBinding.rootView.addView(windowLayout);

        setContentView(windowBinding.getRoot());

        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setBaseViewLocation() {
        baseViewLocation = new int[2];
        baseView.getLocationOnScreen(baseViewLocation);
    }

    public void show() {
        if (isPlaced())
            showAtBottom();
        else {
            showAtTop();
        }
    }

    /**
     * 判断下方是否有充足空间显示悬浮框
     *
     * @return
     */
    private boolean isPlaced() {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y - (baseViewLocation[1] + baseView.getMeasuredHeight()) > getContentView().getMeasuredHeight();
    }

    /**
     * 修改悬浮框位置
     *
     * @param view
     * @param x
     * @param y
     */
    private void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, x + margin.width, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public void showAtTop() {
        windowLayout.removeView(triangle);
        windowLayout.addView(triangle);
        triangle.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.inverted_triangle));
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_in));
        setLayout(windowLayout, 0, baseViewLocation[1] - getContentView().getMeasuredHeight());
        showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }

    public void showAtBottom() {
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        setLayout(windowLayout, 0, baseViewLocation[1] + baseView.getMeasuredHeight());
        showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        super.setOnDismissListener(onDismissListener);
    }

    public class Presenter {
        public void onOutsideTouch() {
            dismiss();
        }
    }
}
