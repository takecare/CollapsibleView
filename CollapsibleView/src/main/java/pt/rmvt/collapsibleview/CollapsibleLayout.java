/**
 * @date May 30, 2013
 * @author Rui Teixeira, rui.teixeira@blip.pt
 * @copyright Copyright 2013 Betfair. All rights reserved.
 */
package pt.rmvt.collapsibleview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CollapsibleLayout extends LinearLayout {

    public static final String LOG_TAG = "CollapsibleLayout";

    private static final int DEFAULT_CONTENT_VISIBILITY = GONE;

    private boolean isContentHidden = true;

    private int mActionViewId;
    private int mContentViewId;
    private int mStateIndicatorViewId;

    private View mActionView;
    private View mContentView;
    private ImageView mStateIndicatorView;
    private Drawable mHiddenState;
    private Drawable mVisibleState;

    public CollapsibleLayout(Context context) {
        super(context);

    }

    public CollapsibleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWithAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CollapsibleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initWithAttrs(attrs);
    }

    //
    private void findViews() {
        if (mActionView == null) {
            mActionView = this.findViewById(mActionViewId);
        }

        if (mStateIndicatorView == null) {
            mStateIndicatorView = (ImageView) this.findViewById(mStateIndicatorViewId);
        }

        if (mContentView == null) {
            mContentView = this.findViewById(mContentViewId);
        }
    }

    private void initWithAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CollapsibleLayout,
                0,
                0);

        try {
            mHiddenState = typedArray.getDrawable(R.styleable.CollapsibleLayout_hiddenStateDrawable);
            mVisibleState = typedArray.getDrawable(R.styleable.CollapsibleLayout_visibleStateDrawable);

            int x = R.id.header_RelativeLayout;
            int y = R.id.content_LinearLayout;
            int z = R.id.arrow_ImageView;

            mActionViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutActionId,-1);
            mContentViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutContentId,-1);
            mStateIndicatorViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutStateIndicatorId,-1);
        } finally {
            typedArray.recycle();
        }

        return;
    }

    protected void startAnimation(final View view, final int visibility, Animation animation) {
        Handler animationHandler = new Handler();
        Runnable animationFinished = new Runnable() {
            @Override
            public void run() {
                onAnimationFinished(view,visibility);
            }
        };
        animationHandler.postDelayed(animationFinished,animation.getDuration());
        view.startAnimation(animation);
    }

    protected void onAnimationFinished(View view, int visibility) {
        view.setVisibility(visibility);
    }

    //
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // when all child views have been positioned...
        findViews();

        mStateIndicatorView.setOnClickListener(actionClickListener);

        mActionView.setClickable(true);
        mActionView.setOnClickListener(actionClickListener);

        if (isContentHidden) {
            mContentView.setVisibility(GONE);
            Log.d("ANIM_ONLAYOUT","set to gone!");
            // set mStateIndicatorView to HIDDEN state
        }
        else {
            mContentView.setVisibility(VISIBLE);
            // set mStateIndicatorView to VISIBLE state
        }

    }

    //
    private OnClickListener actionClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContentView == null) {
                return;
            }

            if (isContentHidden) {
                expand(mContentView);
                mStateIndicatorView.setImageDrawable(mVisibleState);
                isContentHidden = false;
            } else {
                collapse(mContentView);
                mStateIndicatorView.setImageDrawable(mHiddenState);
                isContentHidden = true;
            }
        }
    };

    // --- x ---

    public void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    Log.d("ANIM","BE GONE!");
                }
                else {
                    Log.d("ANIM","time="+interpolatedTime+" "+(initialHeight-(int)(initialHeight * interpolatedTime)));
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(5000);
        v.startAnimation(a);
    }

}