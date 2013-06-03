package pt.rmvt.collapsibleview;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by rmvt on 6/3/13.
 */
public abstract class CollapsibleAnimation extends Animation {

    protected static final int FINAL_INTERPOLATED_TIME = 1;

    protected View mView;
    protected int mInitialHeight;

}
