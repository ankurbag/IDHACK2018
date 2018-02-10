package edu.neu.dreamapp.survey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.model.SurveyQuestion;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyFragment extends BaseFragment {

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @BindView(R.id.tvSubQuestion)
    TextView tvSubQuestion;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private SurveyQuestion surveyQuestion;
    private int previousCount = 0;
    private int buttonCount = 0;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_survey;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }

    /**
     * Called when loading previous question
     *
     * @param index    Index Location
     * @param question Question Text
     */
    public void previousQuestion(final int index, SurveyQuestion question) {
        this.surveyQuestion = question;

        /* Remove all buttons in the radioGroup also interface attached */
        removeButtons();

        /* Collect the previous button count for further button identification */
        previousCount = buttonCount;

        /* Set Question */
        tvQuestion.setText(surveyQuestion.getQuestion());

        /* Set SubQuestion if any */
        if (!surveyQuestion.getSubQuestion().equals("")) {
            tvSubQuestion.setVisibility(View.VISIBLE);
            tvSubQuestion.setText(surveyQuestion.getSubQuestion());
        }

        /* Add first two radioButtons, the minimum of options is 2 */
        addOptionButton(surveyQuestion.getOption().getOne());
        addOptionButton(surveyQuestion.getOption().getTwo());

        /* Add the extra options if exists */
        if (!surveyQuestion.getOption().getThree().equals("")) {
            addOptionButton(surveyQuestion.getOption().getThree());
            if (!surveyQuestion.getOption().getFour().equals("")) {
                addOptionButton(surveyQuestion.getOption().getFour());
                if (!surveyQuestion.getOption().getFive().equals("")) {
                    addOptionButton(surveyQuestion.getOption().getFive());
                }
            }
        }

        /* Check the option that were selected before, if any */
        if (surveyQuestion.getSelected() != 0) {
            ((RadioButton) radioGroup.getChildAt(surveyQuestion.getSelected() - 1)).setChecked(true);
        }

        /* Set a new onCheckedChangeListener for radioGroup */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedID = radioGroup.getCheckedRadioButtonId();
                checkedID -= previousCount;
                selectionCallback.itemSelected(index, checkedID);
            }
        });
    }


    /**
     * Called when loading the next Question
     *
     * @param index    Index Location
     * @param question Question Text
     */
    public void nextQuestion(final int index, SurveyQuestion question) {
        this.surveyQuestion = question;

        /* Remove all buttons in the radioGroup also interface attached */
        removeButtons();

        /* Collect the previous button count for further button identification */
        previousCount = buttonCount;

        /* Set Question */
        tvQuestion.setText(surveyQuestion.getQuestion());

        /* Set SubQuestion if any */
        if (!surveyQuestion.getSubQuestion().equals("")) {
            tvSubQuestion.setVisibility(View.VISIBLE);
            tvSubQuestion.setText(surveyQuestion.getSubQuestion());
        }

        /* Add first two radioButtons, the minimum of options is 2 */
        addOptionButton(surveyQuestion.getOption().getOne());
        addOptionButton(surveyQuestion.getOption().getTwo());

        /* Add the extra options if exists */
        if (!surveyQuestion.getOption().getThree().equals("")) {
            addOptionButton(surveyQuestion.getOption().getThree());
            if (!surveyQuestion.getOption().getFour().equals("")) {
                addOptionButton(surveyQuestion.getOption().getFour());
                if (!surveyQuestion.getOption().getFive().equals("")) {
                    addOptionButton(surveyQuestion.getOption().getFive());
                }
            }
        }

        /* Check the option that were selected before, if any */
        if (surveyQuestion.getSelected() != 0) {
            ((RadioButton) radioGroup.getChildAt(surveyQuestion.getSelected() - 1)).setChecked(true);
        }

        /* Set a new onCheckedChangeListener for radioGroup */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedID = radioGroup.getCheckedRadioButtonId();
                checkedID -= previousCount;
                selectionCallback.itemSelected(index, checkedID);
            }
        });
    }


    /**
     * Helper function for adding radioButton into radioGroup
     *
     * @param option Selected Option
     */
    public void addOptionButton(String option) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(option);
        radioButton.setTextSize(16);
        radioButton.setTextColor(getResources().getColor(R.color.black));
        radioButton.setPadding(10, 10, 10, 10);
        radioGroup.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /* Increment buttonCount by 1 after adding a new radioButton */
        buttonCount++;
    }

    /**
     * Helper function for removing out-of-date radioButtons
     */
    public void removeButtons() {

        /* First remove all child views in radioGroup */
        radioGroup.removeAllViews();

        /* Overwrite onCheckedChangeListener for radioGroup and make it do nothing */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                /* Do nothing in order for assigning new one later on */
            }
        });
    }


    /**
     * Interface for communicating with SurveyActivity
     * for updating SurveyQuestion Selections
     */
    public interface SelectionCallback {
        void itemSelected(int index, int selection);
    }

    public void setSelectionCallback(SelectionCallback listener) {
        this.selectionCallback = listener;
    }

    private SelectionCallback selectionCallback;
}
