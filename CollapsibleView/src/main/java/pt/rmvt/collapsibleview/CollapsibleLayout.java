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
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CollapsibleLayout extends LinearLayout {
    public static final String LOG_TAG = "CollapsibleLayout";

    private static final int DEFAULT_ANIMATION_DURATION = 500;

    private boolean isContentHidden = false;

    private int mActionViewId;
    private int mContentViewId;
    private int mStateIndicatorViewId;

    private View mActionView;
    private View mContentView;
    private ImageView mStateIndicatorView;
    private Drawable mHiddenState;
    private Drawable mVisibleState;

    private int mAnimationDuration = DEFAULT_ANIMATION_DURATION;

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

    private void findViews() {
        if (mActionView == null) {
            mActionView = this.findViewById(mActionViewId);
        }

        if (mStateIndicatorView == null) {
            mStateIndicatorView = (ImageView) this.findViewById(mStateIndicatorViewId);
        }

        if (mContentView == null) {
            mContentView = this.findViewById(mContentViewId);
            if (mContentView.getVisibility() == GONE || mContentView.getVisibility() == INVISIBLE) {
                isContentHidden = true;
            }
            else {
                isContentHidden = false;
            }
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

            mActionViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutActionId,-1);
            mContentViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutContentId,-1);
            mStateIndicatorViewId = typedArray.getResourceId(R.styleable.CollapsibleLayout_collapsibleLayoutStateIndicatorId,-1);

            mAnimationDuration = typedArray.getResourceId(R.styleable.CollapsibleLayout_animationDuration,-1);
            if (mAnimationDuration < 0) {
                mAnimationDuration = DEFAULT_ANIMATION_DURATION;
            }
        }
        finally {
            typedArray.recycle();
        }

        return;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // when all child views have been positioned...
        findViews();

        // this should be optimized somehow...
        mStateIndicatorView.setOnClickListener(actionClickListener);

        mActionView.setClickable(true);
        mActionView.setOnClickListener(actionClickListener);
    }

    private OnClickListener actionClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContentView == null) {
                return;
            }

            Animation animation;

            if (isContentHidden) {
                animation = new ExpandAnimation(mContentView,mAnimationDuration);
                mStateIndicatorView.setImageDrawable(mVisibleState);
                isContentHidden = false;
            } else {
                animation = new CollapseAnimation(mContentView,mAnimationDuration);
                mStateIndicatorView.setImageDrawable(mHiddenState);
                isContentHidden = true;
            }

            animation.startNow();
            mContentView.startAnimation(animation);
        }
    };

    // getters & setters
    public int getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }
}