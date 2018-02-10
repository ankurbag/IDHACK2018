package edu.neu.dreamapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.model.News;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Reports extends BaseFragment {
    private static final String CLASS_TAG = "Reports";

    @BindView(R.id.news_list)
    RecyclerView rv;

    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.reports_main;
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
                SharedPreferences prefs = getActivity().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);

                /* Get Responses */
                Set<String> set = prefs.getStringSet("SR", null);
                if (null != set) {
                    List<News> newsRecords = new ArrayList<>();

                    /* Iterate Surveys */
                    for (final String value : set) {
                        String[] values = value.split(";");

                        News n = new News();
                        n.setHeader("Survey Response");
                        n.setAuthor("Date: " + new Date(Long.parseLong(values[0])));
                        n.setContent("Total Students: " + values[1].split(",").length
                                + "\n"
                                + "Students Present: " + values[2].split(",").length);
                        newsRecords.add(n);
                    }
                    rv.setAdapter(new Reports.NewsListAdapter(newsRecords));
                }
            }
        });

        /* Auto Refreshes The Layout In-Order To Fetch The NEWS */
        refreshLayout.autoRefresh();
    }

    /**
     * News List Adapter
     */
    class NewsListAdapter extends RecyclerView.Adapter<Reports.NewsViewHolder> {
        private final List<News> news;

        NewsListAdapter(List<News> news) {
            this.news = news;
        }

        @Override
        public Reports.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View v = layoutInflater.inflate(R.layout.news_card, viewGroup, false);
            return new Reports.NewsViewHolder(v);
        }

        @Override
        @SuppressWarnings("all")
        public void onBindViewHolder(Reports.NewsViewHolder newsViewHolder, final int i) {
            /* Set Attributes */
            newsViewHolder.newsImage.setWebViewClient(new InAppBrowser());
            newsViewHolder.newsImage.getSettings().setLoadsImagesAutomatically(true);
            newsViewHolder.newsImage.getSettings().setJavaScriptEnabled(true);
            newsViewHolder.newsImage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            newsViewHolder.newsImage.loadUrl("https://www.google.com/");
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
        private WebView newsImage;
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

    /**
     * InApp Browser
     */
    private class InAppBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
