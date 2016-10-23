package com.zliang.shopping.fragment;

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
public class CartFragment extends Fragment {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ViewUtils.inject(this, view);
        mCartProvider = new CartProvider(getContext());
        showData();
        return view;
    }

    private void showData() {
        List<ShoppingCart> carts = mCartProvider.getAll();
        mCartAdapter = new CartAdapter(getContext(), carts);
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
            mToolBar.hideSearchView();
            mToolBar.setTitle(R.string.cart);
            mToolBar.getRightButton().setVisibility(View.VISIBLE);
            mToolBar.getRightButton().setText("编辑");
        }
    }

    public void refreshData() {
        if (mCartAdapter != null) {
            mCartAdapter.clearDatas();
            List<ShoppingCart> carts = mCartProvider.getAll();
            mCartAdapter.addDatas(carts);
        }
    }
}
