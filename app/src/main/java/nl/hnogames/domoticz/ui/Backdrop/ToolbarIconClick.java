package nl.hnogames.domoticz.ui.Backdrop;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.appcompat.widget.AppCompatImageButton;

public class ToolbarIconClick implements View.OnClickListener {

    private final Context context;
    private final View backlayer;
    private final View frontlayer;
    private final Drawable hambergerIcon;
    private final Drawable closeIcon;
    private final int translate;
    private final Interpolator interpolator;
    private final int anim_duration;
    private final AnimatorSet animatorSet = new AnimatorSet();
    private boolean dropped = false;
    private AppCompatImageButton toolbaricon;

    public ToolbarIconClick(Context context, View frontview, View backview, Drawable mMenuicon,
                            Drawable mCloseicon, int height, Interpolator interpolator, int duration) {

        this.context = context;
        this.frontlayer = frontview;
        this.backlayer = backview;
        this.hambergerIcon = mMenuicon;
        this.closeIcon = mCloseicon;
        this.interpolator = interpolator;
        anim_duration = duration;
        this.translate = height;
    }

    public void open() {
        if (!dropped) {
            onClick(toolbaricon);
        }
    }

    public void close() {
        if (dropped) {
            onClick(toolbaricon);
        }
    }

    @Override
    public void onClick(View v) {
        if (toolbaricon == null) {
            this.toolbaricon = (AppCompatImageButton) v;
        }
        dropped = !dropped;
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        updateIcon(v);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frontlayer, "translationY",
                dropped ? translate : 0);
        animatorSet.play(objectAnimator);
        objectAnimator.setDuration(anim_duration);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.start();
    }

    private void updateIcon(View v) {
        if (toolbaricon != null && hambergerIcon != null && closeIcon != null) {
            if (dropped) {
                toolbaricon.setImageDrawable(closeIcon);
            } else {
                toolbaricon.setImageDrawable(hambergerIcon);
            }
        }
    }
}