package com.smg.itemmarket.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smg.itemmarket.R;
import com.smg.itemmarket.Util;
import com.smg.itemmarket.databinding.ItemArticleBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private ArrayList<ArticleModel> articleModels = new ArrayList<ArticleModel>();

    // 생성자
    public ArticleAdapter(ArrayList<ArticleModel> articleModels) {
        this.articleModels = articleModels;
    }

    // onCreateViewHolder에서는 어떤 레이아웃과 연결해야되는지 설정하고 view를 만들어준다.
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(view);
        return articleViewHolder;
    }

    // 받아온 데이터를 item_layout에  set해주는 곳
    @Override
    public void onBindViewHolder(ArticleAdapter.ArticleViewHolder holder, int position) {
        holder.bind(articleModels.get(position));
    }

    @Override
    public int getItemCount() {
        return articleModels.size();
    }

    // item_layout의 데이터 초기화해주는 클래스
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private ItemArticleBinding itemArticleBinding;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            itemArticleBinding = ItemArticleBinding.bind(itemView);
        }

        public void bind(ArticleModel articleModel) {
            SimpleDateFormat format = new SimpleDateFormat("MM월 dd일");
            Date date = new Date(articleModel.getCreateAt());


            itemArticleBinding.titleTextView.setText(articleModel.getTitle());
            itemArticleBinding.dateTextView.setText(format.format(date).toString());
            itemArticleBinding.priceTextView.setText(articleModel.getPrice() + "원");

            Util.log(articleModel.getImageUrl());

            if(articleModel.getImageUrl() != null) {
                Glide.with(itemArticleBinding.thumbnailImageView).load(articleModel.getImageUrl()).into(itemArticleBinding.thumbnailImageView);
            }
        }
    }
}
