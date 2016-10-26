package com.zliang.shopping.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zliang.shopping.R;
import com.zliang.shopping.bean.Campaign;
import com.zliang.shopping.bean.HomeCampaign;

import java.util.List;


/**
 * Created by Ivan on 15/9/30.
 */
public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {

    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;
    private Context mContext;
    private boolean isSlideBottom;

    private List<HomeCampaign> mDatas;
    private OnItemClickListener mListener;

    public HomeCatgoryAdapter(Context context, List<HomeCampaign> datas) {
        mContext = context;
        mDatas = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_R) {
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, viewGroup, false));
        }
        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        HomeCampaign homeCampaign = mDatas.get(i);
        viewHolder.textTitle.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);
        if (i == getItemCount() - 1) {
            isSlideBottom = true;
        }
    }

    public boolean isSlideBottom() {
        return isSlideBottom;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    //根据view item的类型，在getView中创建正确的convertView
    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else {
            return VIEW_TYPE_L;
        }


    }

    public void clears() {
        if (mDatas != null && mDatas.size() > 0) {
            int itemCount = mDatas.size();
            mDatas.clear();
            notifyItemRangeChanged(0, itemCount);
        }
    }

    public void addDatas(List<HomeCampaign> campaigns) {
        addDatas(0, campaigns);
    }

    public void addDatas(int position, List<HomeCampaign> campaigns) {
        if (mDatas != null) {
            if (campaigns != null && campaigns.size() > 0) {
                mDatas.addAll(campaigns);
                notifyItemRangeChanged(position, campaigns.size());
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            anim(v);
        }

        private void anim(final View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 180.0F, 0);
            animator.setDuration(200);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    switch (v.getId()) {
                        case R.id.imgview_big:
                            if (mListener != null) {
                                mListener.onItemClickListener(v, mDatas.get(getLayoutPosition()).getCpOne());
                            }
                            break;
                        case R.id.imgview_small_top:
                            if (mListener != null) {
                                mListener.onItemClickListener(v, mDatas.get(getLayoutPosition()).getCpTwo());
                            }
                            break;
                        case R.id.imgview_small_bottom:
                            if (mListener != null) {
                                mListener.onItemClickListener(v, mDatas.get(getLayoutPosition()).getCpThree());
                            }
                            break;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            animator.start();
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, Campaign campaign);
    }
}
