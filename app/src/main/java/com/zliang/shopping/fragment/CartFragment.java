package com.zliang.shopping.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zliang.shopping.MainActivity;
import com.zliang.shopping.R;
import com.zliang.shopping.adapter.CartAdapter;
import com.zliang.shopping.adapter.DividerItemDecoration;
import com.zliang.shopping.bean.ShoppingCart;
import com.zliang.shopping.utils.CartProvider;
import com.zliang.shopping.widget.CnToolBar;

import java.util.List;


/**
 * Created by Ivan on 15/9/22.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.recyler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CartProvider mCartProvider;

    private CartAdapter mCartAdapter;
    private CnToolBar mToolBar;

    private final int STATE_EDIT = 1;
    public final int STATE_COMPLETE = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ViewUtils.inject(this, view);
        mCartProvider = new CartProvider(getContext());
        showData();
        initListener();
        return view;
    }

    private void initListener() {
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartAdapter.deleteCart();
            }
        });
    }

    private void showData() {
        List<ShoppingCart> carts = mCartProvider.getAll();
        mCartAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mToolBar = (CnToolBar) activity.findViewById(R.id.toolbar);
            changeToolbar();
        }
    }

    public void refreshData() {
        if (mCartAdapter != null) {
            mCartAdapter.clearDatas();
            List<ShoppingCart> carts = mCartProvider.getAll();
            mCartAdapter.addDatas(carts);
        }
    }

    public void changeToolbar() {
        mToolBar.hideSearchView();
        mToolBar.setTitle(R.string.cart);
        mToolBar.getRightButton().setVisibility(View.VISIBLE);
        mToolBar.getRightButton().setText("编辑");
        mToolBar.getRightButton().setOnClickListener(this);
        mToolBar.getRightButton().setTag(STATE_EDIT);
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (action == STATE_EDIT) {
            showDeleteControl();
        } else if (action == STATE_COMPLETE) {
            hideDeleteControl();
        }
    }

    private void hideDeleteControl() {
        mToolBar.getRightButton().setText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);

        mCartAdapter.checkAll(true);
        mCheckBox.setChecked(true);
        mToolBar.getRightButton().setTag(STATE_EDIT);

        mCartAdapter.showTotalPrice();

    }

    private void showDeleteControl() {
        mToolBar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);

        mCartAdapter.checkAll(false);
        mCheckBox.setChecked(false);

        mToolBar.getRightButton().setTag(STATE_COMPLETE);

    }
}
