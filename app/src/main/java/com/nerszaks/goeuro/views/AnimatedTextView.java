package com.nerszaks.goeuro.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nerszaks.goeuro.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 11.06.2016.
 */
public class AnimatedTextView extends TextView {

    public int timeBetweenRedraw = 50;
    public int defAnimationPeriod = 2500;


    private Timer timer;
    private int from = 0;
    private String fullText;

    private int color1;
    private int color2;

    public AnimatedTextView(Context context) {
        this(context, null);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        timer = new Timer();

        color1 = getResources().getColor(R.color.colorPrimary);
        color2 = getResources().getColor(R.color.colorAccent);
    }

    public void setAnimationPeriod(int period) {
        this.defAnimationPeriod = period;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        startAnimation((Activity) getContext());
    }

    public void startAnimation(final Activity activity) {
        from = getText().length();
        fullText = getText().toString();
        final int moveStep = getWidth() / (defAnimationPeriod / timeBetweenRedraw);
        timer.schedule(new TimerTask() {
            public LinearGradient textShader;

            @Override
            public void run() {
                textShader = new LinearGradient(from, 0, from + getWidth(), 0,
                        new int[]{color1, color2, color1},
                        null, Shader.TileMode.REPEAT);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPaint().setShader(textShader);
                        invalidate();
                    }
                });


                if (from >= getWidth()) {
                    from = 0;
                } else {
                    from += moveStep;
                }
            }
        }, timeBetweenRedraw, timeBetweenRedraw);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }
}
