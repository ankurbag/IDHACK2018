package edu.neu.dreamapp.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.neu.dreamapp.MainActivity;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseActivity;
import edu.neu.dreamapp.model.SurveyQuestion;
import edu.neu.dreamapp.widget.CustomProgressBar;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyActivity extends BaseActivity implements SurveyFragment.SelectionCallback {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.horizontalProgress)
    CustomProgressBar horizontalProgress;

    @BindView(R.id.btnPrevious)
    Button btnPrevious;

    @BindView(R.id.btnNext)
    Button btnNext;

    private List<SurveyQuestion> surveyQuestions;
    private int progress;
    private SurveyFragment surveyFragment;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_survey;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Initialize Views
     */
    @Override
    protected void initResAndListener() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEndDialog();
            }
        });

        tvTitle.setText(getResources().getString(R.string.question));

        /* Grab Fragment */
        surveyFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(R.id.surveyFragment);
        surveyFragment.setSelectionCallback(this);

        btnNext.setEnabled(false);

        /* Next Question Button */
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progress == surveyQuestions.size() - 1) {
                    pushAnswersOnFirebase();
                } else {
                    progress++;
                    surveyFragment.nextQuestion(progress, surveyQuestions.get(progress));
                    horizontalProgress.setProgress((int) (((double) progress / (double) surveyQuestions.size()) * 100));

                /* Enable previous button since there is always a question to go back to */
                    btnPrevious.setEnabled(true);
                }
                btnNext.setEnabled(isChecked(progress));
            }
        });

        /* Previous Question Button */
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress--;
                surveyFragment.previousQuestion(progress, surveyQuestions.get(progress));
                horizontalProgress.setProgress((int) (((double) progress / (double) surveyQuestions.size()) * 100));

                /* Disable previous button if progress is pushed back to 0 */
                if (progress == 0) {
                    btnPrevious.setEnabled(false);
                }
                btnNext.setEnabled(isChecked(progress));
            }
        });

        initQuestion();
    }

    /**
     * Initializing Survey Questions (Pre-entered)
     */
    private void initQuestion() {
        surveyQuestions = new ArrayList<>();
        surveyQuestions.add(new SurveyQuestion("How are you feeling? In general, compared to 12 months ago, my health is...", "",
                new SurveyQuestion.OptionBean("About the same", "Better", "Worse", "", "")));
        surveyQuestions.add(new SurveyQuestion("How would you rate your overall health and wellness?", "",
                new SurveyQuestion.OptionBean("Excellent", "Good", "Fair", "Poor", "")));
        surveyQuestions.add(new SurveyQuestion("Counting sessions of at least 10 minutes, how many minutes of accumulated physical activity do you participate in weekly?", "",
                new SurveyQuestion.OptionBean("I am not physically active", "less than 75 minutes", "75 - 149 minutes", "150 - 300 minutes", "more than 300 minutes")));
        surveyQuestions.add(new SurveyQuestion("How many days per week do you participate in stretching exercises?", "",
                new SurveyQuestion.OptionBean("I do not stretch often, if ever", "1 day/week", "2 days/week", "3 or more days/week", "")));
        surveyQuestions.add(new SurveyQuestion("How many days each week do you participate in strength-building or resistance training exercises? Each session needs to be at least 10 minutes in length.", "",
                new SurveyQuestion.OptionBean("I do not typically weight-lift or resistance train", "1 day/week", "2 days/week", "3 or more days/week", "")));
        surveyQuestions.add(new SurveyQuestion("On a typical day, how many hours do you watch television, play video games or sit at a computer?", "",
                new SurveyQuestion.OptionBean("About an hour or less", "Typically 2 - 3 hours", "4 hours or more", "", "")));
        surveyQuestions.add(new SurveyQuestion("Regarding your diet, which statement would be most accurate for you?", "",
                new SurveyQuestion.OptionBean("Within the last 6 months, I have begun to eat more healthfully", "More than 6 months ago, I began eating more health-consciously", "I intend to begin eating better within the nest 6 months", "I am not considering any changes to my diet", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of Grains you intake daily", "(1 slice of bread, 1 cup oif ready-to-eat cereal, 1/2 cup cooked pasta, cooked rice and cooked cereal)",
                new SurveyQuestion.OptionBean("I don't eat grains", "1 - 2 servings", "3 - 4 servings", "5 servings or more", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of Vegetable you intake daily", "(1 cup raw leafy vegetables (about the size of your fist), 1/2 cut-up raw or cooked vegetables or 1/2 cup of vegetable juice)",
                new SurveyQuestion.OptionBean("I don't eat vegetables", "1 - 2 servings", "3 - 4 servings", "5 servings or more", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of Fruits you intake daily", "(1 medium fruit (about the size of a baseball), 1/2 cup of fresh, frozen or canned fruit, 1/2 cup of juice)",
                new SurveyQuestion.OptionBean("I don't eat fruit", "1 - 2 servings", "3 - 4 servings", "5 servings or more", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of meats, poultry and seafood you intake daily", "(3 oz. cooked meat (about the size of a computer mouse), 3 oz. of grilled fish (about the size of a check))",
                new SurveyQuestion.OptionBean("I don't eat meat, poultry, or seafood", "1 servings", "2 servings (6 oz.)", "3 servings or more (9 oz. or more)", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of nuts, seeds and legumes you intake daily", "(1/3 cup or 1 1/2 oz. of nuts, 2 Tbsp. of peanut butter, 2 Tbsp. of seeds, 1/2 cup of dry beans or peas)",
                new SurveyQuestion.OptionBean("I don't eat nuts, seeds or legumes", "1 - 2 servings", "3 - 4 servings", "5 servings or more", "")));
        surveyQuestions.add(new SurveyQuestion("Please mark how many servings of fats and oils you intake daily", "(1 tsp. soft margarine, 1 Tbsp. mayonnaise, 1 tsp. vegetable oil, 1 Tbsp. regular or 2 Tbsp. of low fat salad dressing)",
                new SurveyQuestion.OptionBean("I try my best to avoid all fats and oils", "1 - 2 servings", "3 - 4 servings", "5 servings or more", "")));
        surveyQuestions.add(new SurveyQuestion("On a typical day, how many sodas or sweet drinks do you have?", "(1 drink = 1 can or 12 oz.)",
                new SurveyQuestion.OptionBean("None", "1 or 2", "3 or more", "", "")));
        surveyQuestions.add(new SurveyQuestion("On average, how many hours of uninterrupted sleep do you get per night?", "",
                new SurveyQuestion.OptionBean("< 4 hours", "5 - 6 hours", "7 - 8 hours", "9 hours or more", "")));
        surveyQuestions.add(new SurveyQuestion("On a typical day, how much alcohol do you drink?", "(1 drink = 1 beer/1 glass of wine/1.5 oz. liquor)",
                new SurveyQuestion.OptionBean("None", "1 drink/day", "2 drinks/day", "3 or more drinks/day", "")));
        surveyQuestions.add(new SurveyQuestion("How would you describe your tobacco use?", "",
                new SurveyQuestion.OptionBean("Still/currently user of tobacco", "Quit using tobacco less than 30 days ago", "Quit using tobacco 1 - 12 months ago", "Quit using tobacco more than 12 months ago", "Never used tobacco")));
        surveyQuestions.add(new SurveyQuestion("Have you felt down, depressed, hopeless or had little interest or pleasure in activities for two or more weeks in the past month?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "", "", "")));
        surveyQuestions.add(new SurveyQuestion("In the past year, how many days of work did you miss because of personal or health issues?", "(1 drink = 1 beer/1 glass of wine/1.5 oz. liquor)",
                new SurveyQuestion.OptionBean("I haven't missed any work in the last year", "1-5 days", "6-10 days", "11-15 days", "16 days or more")));
        surveyQuestions.add(new SurveyQuestion("In the last year, how often did your personal or health issues affect your productivity while you were working?", "",
                new SurveyQuestion.OptionBean("My work hasn't really been affected", "Some of the time", "Most of the time", "All of the time", "")));
        surveyQuestions.add(new SurveyQuestion("In general, how satisfied are you with your life", "",
                new SurveyQuestion.OptionBean("Life's good! I'm very satisfied and happy", "Things are good. I am mostly satisfied", "Life is tough... I'm not very satisfied", "I'm dissatisfied/unhappy with my life", "")));
        surveyQuestions.add(new SurveyQuestion("Do you have a family doctor or Primary Care Physician (PCP) you see periodically?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "", "", "")));
        surveyQuestions.add(new SurveyQuestion("How often do you feel stressed?", "",
                new SurveyQuestion.OptionBean("Hardly ever", "Seldom - its rare but it happens", "Sometimes", "Often", "Always")));
        surveyQuestions.add(new SurveyQuestion("Do you know your basic biometric values?", "(Blood Pressure, Cholesterol, Blood Sugar, etc.)",
                new SurveyQuestion.OptionBean("Yes, I know my numbers", "No, not sure I could tell you my current biometric values", "I plan on paying more attention to this", "", "")));
        surveyQuestions.add(new SurveyQuestion("Do you currently have Asthma?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "I prefer not to respond to this question", "", "")));
        surveyQuestions.add(new SurveyQuestion("Do you currently have Diabetes?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "I prefer not to respond to this question", "", "")));
        surveyQuestions.add(new SurveyQuestion("Do you currently have Coronary Artery Disease?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "I prefer not to respond to this question", "", "")));
        surveyQuestions.add(new SurveyQuestion("Do you currently have Congestive Heart Failure?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "I prefer not to respond to this question", "", "")));
        surveyQuestions.add(new SurveyQuestion("Do you currently have Chronic Obstructive Pulmonary Disease(COPD)?", "",
                new SurveyQuestion.OptionBean("Yes", "No", "I prefer not to respond to this question", "", "")));
        surveyQuestions.add(new SurveyQuestion("Rate your organization's interest in employee health and in creating a healthy work place.", "",
                new SurveyQuestion.OptionBean("Excellent - my employer goes above and beyond in creating a healthy work place.", "Good - my employer cares about creating a healthy culture", "Fair - my employer does an ok job of supporting a healthy culture", "Poor - it seems like my employer really doesn't care about creating a healthy workplace", "")));

        /* Set current Progress to 0 */
        progress = 0;

        /* Call nextQuestion on SurveyFragment to load out the first question */
        surveyFragment.nextQuestion(progress, surveyQuestions.get(progress));

        /* Set current Progressbar to show 0 */
        horizontalProgress.setProgress(0);

        /* Disable previous button since it is the first question */
        btnPrevious.setEnabled(false);
    }

    /**
     * Overwrites the interface and creates callback from SurveyFragment
     *
     * @param index     Index Location
     * @param selection Selection
     */
    @Override
    public void itemSelected(int index, int selection) {
        surveyQuestions.get(index).setSelected(selection);
        btnNext.setEnabled(true);
    }

    public void pushAnswersOnFirebase() {
        /*
        FirebaseDatabase.getInstance().getReference().child("survey").orderByChild("id")
                .equalTo(AppUtils.createPathString(UserSingleton.USERINFO.getId()))
                .limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    SurveyAnswer surveyAnswer = new SurveyAnswer(AppUtils.createPathString(UserSingleton.USERINFO.getId()), surveyQuestions);
                    FirebaseDatabase.getInstance().getReference().child("survey").push().setValue(surveyAnswer);
                }

                startActivity(new Intent(mContext, MainActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }

    /**
     * return if any of the options are selected
     *
     * @return Return Value
     */
    public boolean isChecked(int position) {
        return surveyQuestions.get(position).getSelected() != 0;
    }

    /**
     * Survey entry confirmation dialog initialization
     */
    @SuppressWarnings("all")
    private void initEndDialog() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(layout).show();
        TextView tvContent = (TextView) layout.findViewById(R.id.tvContent);
        tvContent.setText(getResources().getString(R.string.end_survey));
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        /* if confirm, launch MainActivity */
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, MainActivity.class);
                ((Activity) mContext).startActivity(intent);

                finish();
                overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500);
            }
        });
    }
}
