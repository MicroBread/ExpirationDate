package com.meng.expirationdate.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.meng.expirationdate.R;
import com.meng.expirationdate.base.BaseApplication;

public class ActionSheet extends Fragment implements View.OnClickListener {

    private static final String ARG_CANCEL_BUTTON_TITLE = "cancel_button_title";
    private static final String ARG_OTHER_BUTTON_TITLES = "other_button_titles";
    private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
    private static final String ISHAS_PROMIT = "has_promit";
    private static final String COLOR_ITEM_POSITION = "color_item_position";
    private static final String ITEM_TEXT_COLOR = "item_text_color";
    private static final int CANCEL_BUTTON_ID = 100;
    private static final int BG_VIEW_ID = 10;
    private static final int TRANSLATE_DURATION = 200;
    private static final int ALPHA_DURATION = 300;

    private static final String EXTRA_DISMISSED = "extra_dismissed";
    private boolean mDismissed = true;
    private ActionSheetListener mListener;
    private ActionSheetWithTextListener mWithTextLister;
    private View mView;
    public LinearLayout mPanel;
    private ViewGroup mGroup;
    private View mBg;
    private Attributes mAttrs;
    private boolean isCancel = true;

    public void show(FragmentManager manager, String tag) {
        if (!mDismissed || manager.isDestroyed()) {
            return;
        }
        mDismissed = false;
        ActionSheet fragment = (ActionSheet) manager.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.dismiss();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void dismiss() {
        if (mDismissed) {
            return;
        }
        mDismissed = true;
        if (getFragmentManager() != null && isAdded() && getActivity() != null && !getActivity().isFinishing()) {
            getFragmentManager().popBackStack();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitAllowingStateLoss();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DISMISSED, mDismissed);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDismissed = savedInstanceState.getBoolean(EXTRA_DISMISSED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = getActivity().getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        mAttrs = readAttribute();

        mGroup = Builder.getmGroup();
        mView = createView();

        createItems();
        if (mGroup != null && mView != null)
            mGroup.addView(mView);
        mBg.startAnimation(createAlphaInAnimation());
        mPanel.startAnimation(createTranslationInAnimation());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mPanel.startAnimation(createTranslationOutAnimation());
        mBg.startAnimation(createAlphaOutAnimation());
//        if (mGroup != null && mView != null)
//            mGroup.removeView(mView);
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGroup != null && mView != null) {
                    mGroup.removeView(mView);
                }

            }
        }, ALPHA_DURATION);
        if (mListener != null) {
            mListener.onDismiss(this, isCancel);
        }
        if (mWithTextLister != null) {
            mWithTextLister.onDismiss(this, isCancel);
        }
        super.onDestroyView();
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private View createView() {
        FrameLayout parent = new FrameLayout(getActivity());
        parent.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mBg = new View(getActivity());
        mBg.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        mBg.setId(ActionSheet.BG_VIEW_ID);
        mBg.setOnClickListener(this);

        mPanel = new LinearLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
//        params.bottomMargin=BarUtils.isNavBarVisible(getActivity())?BarUtils.getNavBarHeight():0;
        //设置导航栏高度
//        View root=((ViewGroup)mGroup.findViewById(android.R.id.content)).getChildAt(0);
//        params.bottomMargin= root.getPaddingBottom();
        mPanel.setLayoutParams(params);
        mPanel.setOrientation(LinearLayout.VERTICAL);
        parent.setPadding(0, 0, 0, 0);
        parent.addView(mBg);
        parent.addView(mPanel);
        return parent;
    }

    public int getNavBarHeight(Context c) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            if (!hasMenuKey && !hasBackKey) {
                //The device has a navigation bar
                Resources resources = c.getResources();

                int orientation = getResources().getConfiguration().orientation;
                int resourceId;
                if (isTablet(c)) {
                    resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
                } else {
                    resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
                }

                if (resourceId > 0) {
                    return getResources().getDimensionPixelSize(resourceId);
                }
            }
        }
        return result;
    }

    private boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private String specialText;

    public void setSpecial(String special) {
        specialText = special;
    }

    private void createItems() {
        String[] titles = getOtherButtonTitles();
        if (titles != null) {
            for (int i = 0; i < titles.length; i++) {
                Button bt = new Button(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    bt.setTextAppearance(R.style.public_diy_text_style);
                }
                bt.setId(CANCEL_BUTTON_ID + i + 1);
                bt.setOnClickListener(this);
                bt.setBackground(getOtherButtonBg(titles, i));
                bt.setText(titles[i]);
                if (i == 0 && getHasPromit()) {
                    bt.setTextColor(ContextCompat.getColor(getContext(), R.color.public_grayActionSheet));
                    bt.setTextSize(14);
                    bt.setEnabled(false);
                } else {
                    if (i == 0) {
                        bt.setTextColor(mAttrs.topButtonTextColor);
                    } else {
                        if (titles[i].equals(specialText)) {
                            bt.setTextColor(ContextCompat.getColor(getContext(), R.color.public_vip_color));
                        } else {
                            bt.setTextColor(mAttrs.otherButtonTextColor);
                        }


                    }
                }
                if (getItemTextColor() > 0 && i == getColorItemPosition()) {
                    bt.setTextColor(ContextCompat.getColor(getContext(), getItemTextColor()));
                }
                bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
                if (i > 0) {
                    LinearLayout.LayoutParams params = createButtonLayoutParams();
                    params.topMargin = mAttrs.otherButtonSpacing;
                    mPanel.addView(bt, params);
                } else {
                    mPanel.addView(bt);
                }
                if (i < titles.length - 1) {
                    View view = new View(getActivity());
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.public_line));
                    LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT, 1);
                    mPanel.addView(view, paramsLine);
                }
            }
        }
        Button bt = new Button(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bt.setTextAppearance(R.style.public_diy_text_style);
        }
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
        bt.setId(ActionSheet.CANCEL_BUTTON_ID);
        bt.setBackground(mAttrs.cancelButtonBackground);
        bt.setText(getCancelButtonTitle());
        bt.setTextColor(mAttrs.cancelButtonTextColor);
        bt.setOnClickListener(this);
        LinearLayout.LayoutParams params = createButtonLayoutParams();
        params.topMargin = mAttrs.cancelButtonMarginTop;
        mPanel.addView(bt, params);

        mPanel.setBackground(mAttrs.background);

        int paddingBottom = 0;
        if (mGroup != null && mGroup.findViewById(android.R.id.content) != null) {
            ViewGroup root = mGroup.findViewById(android.R.id.content);
            if (root.getChildCount() > 0) {
                paddingBottom = root.getChildAt(0).getPaddingBottom();
            }
        }

        mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
                mAttrs.padding + paddingBottom);
    }

    public LinearLayout.LayoutParams createButtonLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private Drawable getOtherButtonBg(String[] titles, int i) {
        if (titles.length == 1) {
            return mAttrs.otherButtonSingleBackground;
        }
        if (titles.length == 2) {
            switch (i) {
                case 0:
                    return mAttrs.otherButtonTopBackground;
                case 1:
                    return mAttrs.otherButtonBottomBackground;
            }
        }
        if (titles.length > 2) {
            if (i == 0) {
                return mAttrs.otherButtonTopBackground;
            }
            if (i == (titles.length - 1)) {
                return mAttrs.otherButtonBottomBackground;
            }
            return mAttrs.getOtherButtonMiddleBackground();
        }
        return null;
    }

    private Attributes readAttribute() {
        Attributes attrs = new Attributes(getActivity());
        TypedArray a = getActivity().getTheme().obtainStyledAttributes(null,
                R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
        Drawable background = a
                .getDrawable(R.styleable.ActionSheet_actionSheetBackground);
        if (background != null) {
            attrs.background = background;
        }
        Drawable cancelButtonBackground = a
                .getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
        if (cancelButtonBackground != null) {
            attrs.cancelButtonBackground = cancelButtonBackground;
        }
        Drawable otherButtonTopBackground = a
                .getDrawable(R.styleable.ActionSheet_otherButtonTopBackground);
        if (otherButtonTopBackground != null) {
            attrs.otherButtonTopBackground = otherButtonTopBackground;
        }
        Drawable otherButtonMiddleBackground = a
                .getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
        if (otherButtonMiddleBackground != null) {
            attrs.otherButtonMiddleBackground = otherButtonMiddleBackground;
        }
        Drawable otherButtonBottomBackground = a
                .getDrawable(R.styleable.ActionSheet_otherButtonBottomBackground);
        if (otherButtonBottomBackground != null) {
            attrs.otherButtonBottomBackground = otherButtonBottomBackground;
        }
        Drawable otherButtonSingleBackground = a
                .getDrawable(R.styleable.ActionSheet_otherButtonSingleBackground);
        if (otherButtonSingleBackground != null) {
            attrs.otherButtonSingleBackground = otherButtonSingleBackground;
        }
        attrs.cancelButtonTextColor = a.getColor(
                R.styleable.ActionSheet_cancelButtonTextColor,
                attrs.cancelButtonTextColor);
        attrs.otherButtonTextColor = a.getColor(
                R.styleable.ActionSheet_otherButtonTextColor,
                attrs.otherButtonTextColor);
        attrs.topButtonTextColor = a.getColor(
                R.styleable.ActionSheet_topButtonTextColor,
                attrs.topButtonTextColor);
        attrs.padding = (int) a.getDimension(
                R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
        attrs.otherButtonSpacing = (int) a.getDimension(
                R.styleable.ActionSheet_otherButtonSpacing,
                attrs.otherButtonSpacing);
        attrs.cancelButtonMarginTop = (int) a.getDimension(
                R.styleable.ActionSheet_cancelButtonMarginTop,
                attrs.cancelButtonMarginTop);
        attrs.actionSheetTextSize = a.getDimensionPixelSize(R.styleable.ActionSheet_actionSheetTextSize, (int) attrs.actionSheetTextSize);

        a.recycle();
        return attrs;
    }

    private String getCancelButtonTitle() {
        return getArguments().getString(ARG_CANCEL_BUTTON_TITLE);
    }

    private String[] getOtherButtonTitles() {
        return getArguments().getStringArray(ARG_OTHER_BUTTON_TITLES);
    }

    private boolean getCancelableOnTouchOutside() {
        return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
    }

    private boolean getHasPromit() {
        return getArguments().getBoolean(ISHAS_PROMIT);
    }

    private int getColorItemPosition() {
        return getArguments().getInt(COLOR_ITEM_POSITION);
    }

    private int getItemTextColor() {
        return getArguments().getInt(ITEM_TEXT_COLOR);
    }

    public void setActionSheetListener(ActionSheetListener listener) {
        mListener = listener;
    }

    public void setActionSheetHasTextListener(ActionSheetWithTextListener listener) {
        mWithTextLister = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ActionSheet.BG_VIEW_ID && !getCancelableOnTouchOutside()) {
            return;
        }
        if (v.getId() - CANCEL_BUTTON_ID - 1 == 0 && getHasPromit()) {
            return;
        }
        dismiss();
        if (v.getId() != ActionSheet.CANCEL_BUTTON_ID && v.getId() != ActionSheet.BG_VIEW_ID) {
            if (mListener != null) {
                mListener.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
                        - 1);
            }
            if (mWithTextLister != null) {
                if (v instanceof Button) {
                    Button bt = (Button) v;
                    mWithTextLister.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
                            - 1, bt.getText().toString());
                }

            }
            isCancel = false;
        }
    }

    public static Builder createBuilder(
            FragmentManager fragmentManager, ViewGroup mg) {
        return new Builder(fragmentManager, mg);
    }

    private static class Attributes {
        private Context mContext;

        public Attributes(Context context) {
            mContext = context;
            this.background = new ColorDrawable(Color.TRANSPARENT);
            this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
            ColorDrawable gray = new ColorDrawable(Color.GRAY);
            this.otherButtonTopBackground = gray;
            this.otherButtonMiddleBackground = gray;
            this.otherButtonBottomBackground = gray;
            this.otherButtonSingleBackground = gray;
            this.cancelButtonTextColor = Color.WHITE;
            this.otherButtonTextColor = Color.BLACK;
            this.padding = dp2px(20);
            this.otherButtonSpacing = dp2px(2);
            this.cancelButtonMarginTop = dp2px(10);
            this.actionSheetTextSize = dp2px(16);
        }

        private int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dp, mContext.getResources().getDisplayMetrics());
        }

        public Drawable getOtherButtonMiddleBackground() {
            if (otherButtonMiddleBackground instanceof StateListDrawable) {
                TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
                        R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
                otherButtonMiddleBackground = a
                        .getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
                a.recycle();
            }
            return otherButtonMiddleBackground;
        }

        Drawable background;
        Drawable cancelButtonBackground;
        Drawable otherButtonTopBackground;
        Drawable otherButtonMiddleBackground;
        Drawable otherButtonBottomBackground;
        Drawable otherButtonSingleBackground;
        int cancelButtonTextColor;
        int otherButtonTextColor;
        int topButtonTextColor;
        int padding;
        int otherButtonSpacing;
        int cancelButtonMarginTop;
        float actionSheetTextSize;
    }

    public static class Builder {

        private FragmentManager mFragmentManager;
        private static ViewGroup mGroup;
        private String mCancelButtonTitle;
        private String[] mOtherButtonTitles;
        private boolean isHasPromit = true;
        private int colorItemPosition = 0;
        private int itemTextColor = 0;
        private String mTag = "actionSheet";
        private boolean mCancelableOnTouchOutside;
        private ActionSheetListener mListener;
        private ActionSheetWithTextListener mWithTextLister;
        private String specialText;

        public Builder(FragmentManager fragmentManager, ViewGroup mg) {
            mFragmentManager = fragmentManager;
            mGroup = mg;
//            ViewGroup content=mg.findViewById(android.R.id.content);
//            mGroup = content==null?mg:content;
        }

        public static ViewGroup getmGroup() {
            return mGroup;
        }

        public void setmGroup(ViewGroup mGroup) {
            Builder.mGroup = mGroup;
        }

        public Builder setCancelButtonTitle(String title) {
            mCancelButtonTitle = title;
            return this;
        }

        public Builder setSpecial(String special) {
            specialText = special;
            return this;
        }

        public Builder setCancelButtonTitle(int strId) {
            return setCancelButtonTitle(BaseApplication.Companion.instance().getString(strId));
        }

        public Builder setOtherButtonTitles(String[] titles) {
            mOtherButtonTitles = titles;
            return this;
        }

        public Builder setTag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder setListener(ActionSheetListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setHasTextListener(ActionSheetWithTextListener listener) {
            this.mWithTextLister = listener;
            return this;
        }


        public Builder setItemTextColor(int itemPosition, int itemTextColor) {
            this.colorItemPosition = itemPosition;
            this.itemTextColor = itemTextColor;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            mCancelableOnTouchOutside = cancelable;
            return this;
        }

        public Builder setHasPromit(boolean hasPromit) {
            isHasPromit = hasPromit;
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_CANCEL_BUTTON_TITLE, mCancelButtonTitle);
            bundle.putStringArray(ARG_OTHER_BUTTON_TITLES, mOtherButtonTitles);
            bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE,
                    mCancelableOnTouchOutside);
            bundle.putBoolean(ISHAS_PROMIT, isHasPromit);
            bundle.putInt(COLOR_ITEM_POSITION, colorItemPosition);
            bundle.putInt(ITEM_TEXT_COLOR, itemTextColor);
            return bundle;
        }

        public ActionSheet show() {
            ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
                    BaseApplication.Companion.instance(), ActionSheet.class.getName(), prepareArguments());
            actionSheet.setActionSheetListener(mListener);
            actionSheet.setActionSheetHasTextListener(mWithTextLister);
            actionSheet.setSpecial(specialText);
            actionSheet.show(mFragmentManager, mTag);
            return actionSheet;
        }

    }

    public interface ActionSheetListener {

        void onDismiss(ActionSheet actionSheet, boolean isCancel);

        void onOtherButtonClick(ActionSheet actionSheet, int index);
    }

    public interface ActionSheetWithTextListener {

        void onDismiss(ActionSheet actionSheet, boolean isCancel);

        void onOtherButtonClick(ActionSheet actionSheet, int index, String text);
    }


}
