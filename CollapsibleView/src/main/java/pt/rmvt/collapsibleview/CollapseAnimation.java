package pt.rmvt.collapsibleview;

import android.view.View;
import android.view.animation.Transformation;

/**
 * Created by rmvt on 6/1/13.
 */
public class CollapseAnimation extends CollapsibleAnimation {

    public static final String LOG_TAG = "CollapseAnimation";

    public CollapseAnimation(View view) {
        mView = view;
    }

    public CollapseAnimation(View view, int duration) {
        this(view);
        setDuration(duration);
    }

    private void initAnimation() {
        mInitialHeight = mView.getMeasuredHeight();
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
        if (interpolatedTime == FINAL_INTERPOLATED_TIME) {
            mView.setVisibility(View.GONE);
        }
        else {
            mView.getLayoutParams().height = mInitialHeight - (int)(mInitialHeight * interpolatedTime);
            mView.requestLayout();
        }
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

}
