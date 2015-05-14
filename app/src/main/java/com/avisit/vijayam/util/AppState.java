package com.avisit.vijayam.util;

import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.model.Topic;

/**
 * Created by User on 5/13/2015.
 */
public class AppState {
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
