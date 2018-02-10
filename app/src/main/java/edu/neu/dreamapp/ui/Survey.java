package edu.neu.dreamapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.model.News;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Survey extends BaseFragment {
    private static final String CLASS_TAG = "Survey";

    @BindView(R.id.news_list)
    RecyclerView rv;

    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.survey_main;
    }

    @Override
    protected String getTAG() {
        return CLASS_TAG;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /* Setup RefreshLayout Listener, When Page Refreshes, Load Again */
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                rv.setLayoutManager(new LinearLayoutManager(context));
                List<News> newsRecords = new ArrayList<>();
                News n = new News();
                n.setAuthor("foo");
                n.setHeader("bar");
                n.setContent("dfkjdbgvjkbc");
                newsRecords.add(n);
                rv.setAdapter(new NewsListAdapter(newsRecords));
            }
        });

        /* Auto Refreshes The Layout In-Order To Fetch The NEWS */
        refreshLayout.autoRefresh();
    }

    /**
     * News List Adapter
     */
    class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder> {
        private final List<News> news;

        NewsListAdapter(List<News> news) {
            this.news = news;
        }

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View v = layoutInflater.inflate(R.layout.news_card, viewGroup, false);
            return new NewsViewHolder(v);
        }

        @Override
        @SuppressWarnings("all")
        public void onBindViewHolder(NewsViewHolder newsViewHolder, final int i) {
            /* Set Attributes */
            if (null != news.get(i).getImage() && !"".equalsIgnoreCase(news.get(i).getImage())) {
                Picasso.with(getActivity().getApplicationContext()).load(news.get(i).getImage()).resize(135, 135).centerCrop().into(newsViewHolder.newsImage);
            }
            newsViewHolder.newsHeader.setText(news.get(i).getHeader());
            newsViewHolder.newsAuthor.setText(news.get(i).getAuthor());
            newsViewHolder.newsContent.setText(news.get(i).getContent());

            /* Add Listener */
            newsViewHolder.holder.setBackground(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.selector_new_card_white));
            newsViewHolder.newsHeader.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));
            newsViewHolder.newsAuthor.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));
            newsViewHolder.newsContent.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));

            newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(news.get(i).getUrl()));
                    startActivity(browserIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return news.size();
        }
    }

    /**
     * News View Holder
     */
    class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsHeader;
        private TextView newsAuthor;
        private TextView newsContent;
        private View holder;

        NewsViewHolder(View itemView) {
            super(itemView);
            holder = itemView.findViewById(R.id.holder);
            newsImage = itemView.findViewById(R.id.news_image);
            newsHeader = itemView.findViewById(R.id.news_header);
            newsAuthor = itemView.findViewById(R.id.news_author);
            newsContent = itemView.findViewById(R.id.news_body);
        }
    }
}
