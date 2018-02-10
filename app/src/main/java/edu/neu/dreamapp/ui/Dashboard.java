package edu.neu.dreamapp.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Dashboard extends BaseFragment {
    private static final String CLASS_TAG = "Dashboard";

    @BindView(R.id.webview)
    WebView inAppBrowser;

    @Override
    public int getContentViewId() {
        return R.layout.dashboard_main;
    }

    @Override
    protected String getTAG() {
        return CLASS_TAG;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
       /* Load Survey */
        inAppBrowser.setWebViewClient(new InAppBrowser());
        inAppBrowser.getSettings().setLoadsImagesAutomatically(true);
        inAppBrowser.getSettings().setJavaScriptEnabled(true);
        inAppBrowser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        inAppBrowser.loadUrl("http://www.google.com/");
    }

    private class InAppBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }



    }
}
