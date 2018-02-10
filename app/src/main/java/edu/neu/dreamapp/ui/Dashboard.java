package edu.neu.dreamapp.ui;

import android.os.Bundle;

import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Dashboard extends BaseFragment {
    private static final String CLASS_TAG = "Dashboard";

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
        /* Do Nothing */
    }
}
