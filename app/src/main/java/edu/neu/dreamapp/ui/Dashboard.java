package edu.neu.dreamapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Dashboard extends BaseFragment {
    private static final String CLASS_TAG = "Dashboard";
    private static final String ASSET_PATH = "file:///android_asset/";

    @BindView(R.id.wv1)
    WebView wv1;

    @BindView(R.id.wv2)
    WebView wv2;

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
        /* Read Chart HTML */
        String fileContent = "";
        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream in = assetManager.open("piechart.html");
            byte[] bytes = loadHTMLasByteArray(in);
            fileContent = new String(bytes, "UTF-8");
        } catch (IOException ex) {
            Toast.makeText(getContext(), "Failed To Load Google Charts!", Toast.LENGTH_LONG).show();
        }
        final String content = fileContent;

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("SR_RESP_SET", new HashSet<String>());
        if (0 < set.size()) {
            Map<String, String> map = new HashMap<>();

            /* Iterate Surveys */
            for (final String value : set) {
                String[] values = value.split(";");
                map.put(values[0], String.valueOf(values[1].split(",").length) + ";" + String.valueOf(values[2].split(",").length));
            }

            /* Loop */
            for (final String key : map.keySet()) {
                String[] nums = map.get(key).split(";");
                String formattedContent = content.replace("width: 135px; height: 135px;", "width: 300px; height: 300px;")
                        .replace("[#DataTable#]", "['Attendance','Percentage'],['Present',"
                                + Integer.parseInt(nums[0])
                                + "],['Absent',"
                                + Integer.parseInt(nums[1]) + "]");
                wv1.setWebViewClient(new InAppBrowser());
                wv1.getSettings().setLoadsImagesAutomatically(true);
                wv1.getSettings().setJavaScriptEnabled(true);
                wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wv1.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "UTF-8", null);
                break;
            }
        }

        /* Read Chart HTML */
        fileContent = "";
        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream in = assetManager.open("piechart.html");
            byte[] bytes = loadHTMLasByteArray(in);
            fileContent = new String(bytes, "UTF-8");
        } catch (IOException ex) {
            Toast.makeText(getContext(), "Failed To Load Google Charts!", Toast.LENGTH_LONG).show();
        }
        final String contentx = fileContent;

        prefs = getActivity().getApplicationContext().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);
        set = prefs.getStringSet("SR_RESP_SET", new HashSet<String>());
        if (0 < set.size()) {
            Map<String, String> map = new HashMap<>();

            /* Iterate Surveys */
            for (final String value : set) {
                String[] values = value.split(";");
                map.put(values[0], String.valueOf(values[1].split(",").length) + ";" + String.valueOf(values[2].split(",").length));
            }

            /* Loop */
            for (final String key : map.keySet()) {
                String[] nums = map.get(key).split(";");
                String formattedContent = contentx.replace("width: 135px; height: 135px;", "width: 300px; height: 300px;")
                        .replace("[#DataTable#]", "['Attendance','Percentage'],['Present',"
                                + Integer.parseInt(nums[0])
                                + "],['Absent',"
                                + Integer.parseInt(nums[1]) + "]");
                wv2.setWebViewClient(new InAppBrowser());
                wv2.getSettings().setLoadsImagesAutomatically(true);
                wv2.getSettings().setJavaScriptEnabled(true);
                wv2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wv2.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "UTF-8", null);
                break;
            }
        }
    }

    private byte[] loadHTMLasByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    private class InAppBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
