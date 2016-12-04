package com.example.dangfiztssi.newyorktime.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dangfiztssi.newyorktime.R;
import com.example.dangfiztssi.newyorktime.models.Article;
import com.example.dangfiztssi.newyorktime.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DangF on 10/21/16.
 */

public class ItemArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NORMAL = 0;
    private static final int NO_IMAGE = 1;
    private List<Article> articleList;
    private Listener mListener;

    public interface Listener{
        void onLoadMore();
        void onClick(Article article);
    }

    public void setListener(Listener listener){
        this.mListener = listener;
    }

    public ItemArticleAdapter(){
        articleList = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
        articleList.clear();
        articleList.addAll(articles);
        notifyDataSetChanged();
    }

    public void addArticles(List<Article> articles){
        articleList.addAll(articles);
        notifyItemRangeInserted(articleList.size(), articles.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == NORMAL) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article, parent, false);
            return new ViewHolder(itemView);
        } else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article_no_image, parent, false);

        return new NoImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articleList.get(position);

        if(holder instanceof NoImageViewHolder){
            bindNoImage((NoImageViewHolder)holder, article);
        }
        else{
            bindNormal((ViewHolder) holder, article);
        }

        holder.itemView.setOnClickListener(v -> mListener.onClick(article));

        if(position == articleList.size()-1 && mListener != null){
            mListener.onLoadMore();
        }
    }

    private void bindNormal(ViewHolder holder, Article article) {

        int height = article.getHeight();

        holder.imageView.getLayoutParams().height = (int) AppUtils.convertDpToPixel(
                height, holder.itemView.getContext());

        if(height < 200)
            holder.tvSnippet.setMaxLines(1);
        else if(height <500)
            holder.tvSnippet.setMaxLines(3);
        else
            holder.tvSnippet.setMaxLines(4);
        holder.tvSnippet.setText(article.getSnippet());

        Glide.with(holder.itemView.getContext())
                .load(article.getMultimedia().get(1).getUrl())
                .into(holder.imageView);
    }

    private void bindNoImage(NoImageViewHolder holder, Article article) {
        holder.tvSnippet.setText(article.getSnippet());
    }


    @Override
    public int getItemViewType(int position) {
        Article article = articleList.get(position);

        if (article.getMultimedia() != null && !article.getMultimedia().isEmpty())
            return NORMAL;

        return NO_IMAGE;

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView imageView;

        @BindView(R.id.tvSnippet)
        TextView tvSnippet;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class NoImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSnippet)
        TextView tvSnippet;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
