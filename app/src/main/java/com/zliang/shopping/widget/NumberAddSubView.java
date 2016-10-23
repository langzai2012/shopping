package com.zliang.shopping.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zliang.shopping.R;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Button mBtnAdd;
    private Button mBtnSub;
    private TextView mTvNumber;

    private int value;
    private int minValue;
    private int maxValue;
    private OnButtonClickListener mListener;


    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

        if (attrs != null) {
            initData(context, attrs, defStyleAttr);
        }
    }

    private void initData(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray tp = null;
        try {
            tp = context.obtainStyledAttributes(attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
            int defalutValue = tp.getInt(R.styleable.NumberAddSubView_defaultValue, 0);
            setValue(defalutValue);
            int maxValue = tp.getInt(R.styleable.NumberAddSubView_maxValue, 100);
            setMaxValue(maxValue);
            int minValue = tp.getInteger(R.styleable.NumberAddSubView_minValue, 0);
            setMinValue(minValue);

            Drawable addDrawable = tp.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable subDrawable = tp.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable textDrawable = tp.getDrawable(R.styleable.NumberAddSubView_textBackground);

            setBackGroundDrawable(mBtnAdd, addDrawable);
            setBackGroundDrawable(mBtnSub, subDrawable);
            setTextViewBackgroundDrawable(mTvNumber, textDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tp != null) {
                tp.recycle();
            }
        }
    }

    private void setBackGroundDrawable(Button btn, Drawable drawable) {
        btn.setBackgroundDrawable(drawable);
    }

    private void setTextViewBackgroundDrawable(TextView tv, Drawable drawable) {
        tv.setBackgroundDrawable(drawable);
    }

    private void initView(Context context) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.number_add_sub_view, this, true);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);
        mTvNumber = (TextView) view.findViewById(R.id.etxt_num);
        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    public int getValue() {
        String val = mTvNumber.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            value = Integer.parseInt(val);
        }
        return value;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            numberAdd();
            if (mListener != null) {
                mListener.onButtonAddClick(v, getValue());
            }
        } else if (v.getId() == R.id.btn_sub) {
            numberSub();
            if (mListener != null) {
                mListener.onButtonSubClick(v, getValue());
            }
        }
    }

    private void numberAdd() {
        if (value < maxValue) {
            value += 1;
        }
        mTvNumber.setText(value + "");
    }

    private void numberSub() {
        if (value > 1) {
            value -= 1;
        } else {
            value = 1;
        }
        mTvNumber.setText(value + "");
    }

    public interface OnButtonClickListener {
        void onButtonAddClick(View view, int value);

        void onButtonSubClick(View view, int value);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public void setValue(int value) {
        mTvNumber.setText(value + "");
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


}
