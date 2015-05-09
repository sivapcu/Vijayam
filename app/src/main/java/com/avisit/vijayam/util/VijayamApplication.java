package com.avisit.vijayam.util;

import android.app.Application;

import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.model.Topic;


/**
 * Created by User on 4/15/2015.
 */
public class VijayamApplication extends Application {
    private Course selectedCourse;
    private Topic selectedTopic;
    private Question selectedQuestion;
    private int currentQuestionIndex;
    private int totalQuestions;

    public void resetAll() {
        this.currentQuestionIndex = 0;
        this.selectedCourse = null;
        this.selectedQuestion = null;
        this.selectedTopic = null;
        this.totalQuestions = 0;
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public Topic getSelectedTopic() {
        return selectedTopic;
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic = selectedTopic;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public Question getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
