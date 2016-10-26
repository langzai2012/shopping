package com.zliang.shopping.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zliang.shopping.R;
import com.zliang.shopping.utils.DisplayUtil;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public class CnToolBar extends Toolbar {
    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;

    public CnToolBar(Context context) {
        this(context, null);
    }

    public CnToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        //设置toolbar 左右间距
        setContentInsetsRelative(10, 20);
        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolBar, defStyleAttr, 0);
            final Drawable rightButtonIcon = a.getDrawable(R.styleable.CnToolBar_rightButtonIcon);
            setRightButtonIcon(rightButtonIcon);

            boolean isShowSeachview = a.getBoolean(R.styleable.CnToolBar_isShowSeachview, false);
            if (isShowSeachview) {
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }


    }

    private void initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }

    }

    public Button getRightButton() {
        return mRightButton;
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView() {

        if (mSearchView != null) {
            mSearchView.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIconListener(OnClickListener li) {
        if (mRightButton != null) {
            mRightButton.setOnClickListener(li);
        }
    }


    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

    public void setRightButtonIcon(Drawable icon) {
        if (mRightButton != null) {
            mRightButton.setBackgroundDrawable(icon);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }

    public void setRightButtonIcon(int iconId) {
        setRightButtonIcon(getResources().getDrawable(iconId));
    }

}
