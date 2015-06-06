package com.avisit.vijayam.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avisit.vijayam.R;
import com.avisit.vijayam.dao.OptionDao;
import com.avisit.vijayam.dao.QuestionDao;
import com.avisit.vijayam.model.Option;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.util.VijayamApplication;

import java.util.List;

public class QuestionsActivity extends ActionBarActivity {
    private int checkedOptionIndex = -1;
    private TextView quesNoTextView;
    private TextView quesContentTextView;
    private int questionIndex;
    private Question question;
    View.OnClickListener btnListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setTitle(((VijayamApplication) getApplication()).getAppState().getSelectedTopic().getName());
        quesNoTextView = ((TextView) findViewById(R.id.question_number));
        questionIndex = ((VijayamApplication) getApplication()).getAppState().getCurrentQuestionIndex();
        Bundle intentExtrasBundle = getIntent().getExtras();
        boolean isSubmitted = false;
        if (intentExtrasBundle != null) {
            isSubmitted = intentExtrasBundle.getBoolean("afterSubmission");
            checkedOptionIndex = intentExtrasBundle.getInt("checkedOptionIndex");
        }
        fetchQuestion(this.questionIndex);
        if (question!=null) {
            quesNoTextView.setText(getString(R.string.question_no, questionIndex + 1, ((VijayamApplication) getApplication()).getAppState().getTotalQuestions()));
            quesContentTextView = ((TextView) findViewById(R.id.question_textview));
            quesContentTextView.setText(question.getContent());
            createImages();
            createOptions(isSubmitted, checkedOptionIndex);
            createReviewCheckBox();
        } else {
            quesNoTextView.setText(R.string.no_results);
            quesNoTextView.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void fetchQuestion(int questionIndex) {
        question = new QuestionDao(this).fetchQuestion(((VijayamApplication)getApplication()).getAppState().getSelectedTopic().getId(), questionIndex);
        if(question!=null){
            question.setOptionsList(new OptionDao(this).fetchOptions(question.getQuestionId()));
            question.setImagesList(new QuestionDao(this).fetchImages(question.getQuestionId()));
        }
    }

    private void createImages() {
        List<String> imagesList = this.question.getImagesList();
        if(imagesList!=null){
            for(int i = 0; i < imagesList.size(); i++) {
                Button button = new Button(this);
                button.setText("Image " + (i + 1));
                button.setTextColor(getResources().getColor(R.color.button_material_light));
                button.setId(i);
                button.setBackgroundColor(getResources().getColor(R.color.button_material_dark));
                button.setOnClickListener(btnListener);
                ((LinearLayout)findViewById(R.id.images_layout)).addView(button);
            }
        }
    }

    private void createOptions(boolean isSubmitted, int checkedOptionIndex) {
        List<Option> optionList = question.getOptionsList();
        if(optionList.size()>0){
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setPadding(10, 10, 5, 5);
            radioGroup.setSoundEffectsEnabled(true);

            for(int i=0; i<optionList.size(); i++){
                Option option = optionList.get(i);
                if (i == checkedOptionIndex) {
                    option.setSelected(true);
                }
                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(i);
                radioButton.setText(option.getContent());
                radioButton.setCursorVisible(true);
                radioButton.setTextSize(getResources().getDimension(R.dimen.level14));

                if(isSubmitted){
                    if(option.isCorrect()){
                        radioButton.setTextColor(getResources().getColorStateList(R.color.googleGreen));
                    }else if(option.isSelected() && !option.isCorrect()){
                        radioButton.setTextColor(getResources().getColorStateList(R.color.red));
                    }
                }
                radioGroup.addView(radioButton);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    QuestionsActivity.this.checkedOptionIndex = checkedId;
                }
            });
            ((LinearLayout) findViewById(R.id.options_layout)).addView(radioGroup);
        }
    }

    private void createReviewCheckBox() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.mark_for_review);
        checkBox.setChecked(question.isMarkedForReview());
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                question.setMarkedForReview(isChecked);
                new QuestionDao(getApplicationContext()).markQuestion(question.getQuestionId(), isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.questions, menu);

        MenuItem previous = menu.findItem(R.id.action_previous);
        final int totalQuestions = ((VijayamApplication) getApplication()).getAppState().getTotalQuestions();
        previous.setVisible(questionIndex > 0 && totalQuestions > 0);

        MenuItem next = menu.findItem(R.id.action_next);
        next.setVisible((questionIndex != (totalQuestions-1)) && totalQuestions > 0);

        MenuItem list = menu.findItem(R.id.list);
        list.setVisible(totalQuestions > 2);

        MenuItem verify = menu.findItem(R.id.verify);
        verify.setVisible(totalQuestions > 0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_previous:
                goToPrevQuestion();
                return true;
            case R.id.action_next:
                goToNextQuestion();
                return true;
            case R.id.list:
                showQuestionsListDialog();
                return true;
            case R.id.verify:
                if (checkedOptionIndex == -1) {
                    AlertDialog.Builder verifyAlert = new AlertDialog.Builder(this);
                    verifyAlert.setTitle("Show Answer...");
                    verifyAlert.setMessage("You haven't selected any option. Do you want to show the answer? ");
                    verifyAlert.setIcon(R.mipmap.vijayam_ic_launcher);
                    verifyAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(QuestionsActivity.this, QuestionsActivity.class);
                            intent.putExtra("afterSubmission", true);
                            intent.putExtra("checkedOptionIndex", checkedOptionIndex);
                            startActivity(intent);
                            finish();
                        }
                    });
                    verifyAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    verifyAlert.show();
                } else {
                    Intent intent = new Intent(this, QuestionsActivity.class);
                    intent.putExtra("afterSubmission", true);
                    intent.putExtra("checkedOptionIndex", checkedOptionIndex);
                    startActivity(intent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void goToPrevQuestion() {
        if(questionIndex!=0){
            ((VijayamApplication) getApplication()).getAppState().setCurrentQuestionIndex(--questionIndex);
            Intent prevQuestion = new Intent(this, QuestionsActivity.class);
            startActivity(prevQuestion);
            finish();
        }else{
            Toast.makeText(this, "Swipe Right for Next Question!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToNextQuestion() {
        if(questionIndex < ((VijayamApplication) getApplication()).getAppState().getTotalQuestions()-1){
            ((VijayamApplication) getApplication()).getAppState().setCurrentQuestionIndex(++questionIndex);
            Intent nextQuestion = new Intent(this, QuestionsActivity.class);
            startActivity(nextQuestion);
            finish();
        }else{
            Toast.makeText(QuestionsActivity.this, "Great! You have finished all Questions!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showQuestionsListDialog() {
        AlertDialog.Builder allQuestionDialog = new AlertDialog.Builder(this);
        allQuestionDialog.setCancelable(true);
        List<Question> questions = getQuestionsMarked(((VijayamApplication)getApplication()).getAppState().getSelectedTopic().getId());
        final String [] items = new String[questions.size()];
        for(int i=0; i< items.length; i++){
            if(questions.get(i).isMarkedForReview()){
                items[i]= String.valueOf(i+1) + "\t Marked";
            }else{
                items[i]= String.valueOf(i+1);
            }
        }

        allQuestionDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ((VijayamApplication) getApplication()).getAppState().setCurrentQuestionIndex(item);
                Intent randomQuestion = new Intent(QuestionsActivity.this, QuestionsActivity.class);
                startActivity(randomQuestion);
                finish();
            }
        });

        allQuestionDialog.show();
    }

    /*initialization block for button onClickListener*/
    {
        btnListener = new View.OnClickListener() {
            public void onClick(View view) {
                List<String> imagesList = question.getImagesList();
                Intent intent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                intent.putExtra("imageName", (String) imagesList.get(((Button) view).getId()));
                startActivity(intent);
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateTopicQuestionMap();
        finish();
    }

    private void updateTopicQuestionMap() {
        new QuestionDao(this).updateTopicQuestionMap(((VijayamApplication) getApplication()).getAppState().getSelectedTopic().getId(), questionIndex);
    }

    private List<Question> getQuestionsMarked(int topicId) {
        return new QuestionDao(this).fetchMarkedQuestions(topicId);
    }
}
