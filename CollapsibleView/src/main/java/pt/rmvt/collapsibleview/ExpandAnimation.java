package pt.rmvt.collapsibleview;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by rmvt on 6/1/13.
 */
public class ExpandAnimation extends CollapsibleAnimation {

    public static String LOG_TAG = "ExpandAnimation";

    private int mFinalHeight;

    public ExpandAnimation(View view) {
        mView = view;
    }

    public ExpandAnimation(View view, int duration) {
        this(view);
        setDuration(duration);
    }

    private void initAnimation() {
        mView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mFinalHeight = mView.getMeasuredHeight();

        mView.getLayoutParams().height = 0;
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void start() {
        initAnimation();
        super.start();
    }

    @Override
    public void startNow() {
        initAnimation();
        super.startNow();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        mView.getLayoutParams().height = interpolatedTime == FINAL_INTERPOLATED_TIME
                ? LinearLayout.LayoutParams.WRAP_CONTENT
                : (int)(mFinalHeight * interpolatedTime);
        mView.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
