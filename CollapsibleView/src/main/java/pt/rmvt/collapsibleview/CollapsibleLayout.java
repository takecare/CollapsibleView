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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CollapsibleLayout extends LinearLayout {

    public static final String LOG_TAG = "CollapsibleLayout";

    private static final int DEFAULT_CONTENT_VISIBILITY = GONE;

    private boolean isContentHidden = true;

    private View mActionView;
    private View mContentView;
    private ImageView mStateIndicatorView;
    private Drawable mHiddenState;
    private Drawable mVisibleState;

    // declare stylable: hidden and visible state drawables so we can get the drawables from the AttributeSet object
    // action, state and content views should be captured automatically by view ID/TAG, if not found

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
            mActionView = this.findViewWithTag(getResources().getString(R.string.collapsibleLayoutAction));
        }

        if (mStateIndicatorView == null) {
            mStateIndicatorView = (ImageView) this.findViewWithTag(getResources().getString(R.string.collapsibleLayoutStateIndicator));
        }

        if (mContentView == null) {
            mContentView = this.findViewWithTag(getResources().getString(R.string.collapsibleLayoutContent));
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
        } finally {
            typedArray.recycle();
        }

        return;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //mStateIndicatorView.setOnClickListener(actionClickListener);
    }

    //
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // when all child views have been positioned
        findViews();

        mStateIndicatorView.setOnClickListener(actionClickListener);

        mActionView.setClickable(true);
        mActionView.setOnClickListener(actionClickListener);

        if (isContentHidden) {
            mContentView.setVisibility(GONE);
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
                mContentView.setVisibility(VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.collapsible_show);
                mContentView.startAnimation(anim);

                mStateIndicatorView.setImageDrawable(mVisibleState);
                isContentHidden = false;
            } else {
                mContentView.setVisibility(GONE);

                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.collapsible_hide);
                mContentView.startAnimation(anim);

                mStateIndicatorView.setImageDrawable(mHiddenState);
                isContentHidden = true;
            }
        }
    };

}