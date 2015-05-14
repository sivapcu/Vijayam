package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avisit.vijayam.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/18/2015.
 */
public class QuestionDao extends DataBaseHelper {
    private static final String TAG = QuestionDao.class.getSimpleName();

    public QuestionDao(Context context){
        super(context);
    }

    public long persistQuestion(Question question) {
        long rowId = 0;
        ContentValues values = new ContentValues();
        values.put("id", question.getQuestionId());
        values.put("content", question.getQuestionText());
        values.put("reviewFlag", question.isMarkedForReview());
        values.put("topicId", question.getTopicId());
        values.put("sortOrder", question.getSortOrder());
        values.put("winFlag", question.isWinFlag());
        SQLiteDatabase myDataBase = getWritableDatabase();
        try{
            rowId = myDataBase.insert("question", null, values);
        } catch(SQLiteException se){
            Log.e(TAG, "Could not persist the question");
        } finally{
            close();
        }
        return rowId;
    }

    public int fetchTotalQuestionCount(int topicId){
        int count = 0;
        SQLiteDatabase myDataBase = getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = myDataBase.rawQuery("SELECT count(id) as count FROM question where topicId = ?", new String[]{Integer.toString(topicId)});
            if (cursor != null ) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("count"));
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return count;
    }

    public int fetchLastSessionQuesId(int topicId) {
        int questionId = 0;
        SQLiteDatabase myDataBase = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT questionId FROM topic_current_question WHERE topicId = ?", new String[]{Integer.toString(topicId)});
            if (cursor != null ) {
                if (cursor.moveToFirst()) {
                    questionId = cursor.getInt(cursor.getColumnIndex("id"));
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return questionId;
    }

    public Question fetchQuestion(int topicId, int index) {
        SQLiteDatabase myDataBase = getReadableDatabase();
        Question question = null;
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT id, content, reviewFlag FROM question where topicId = ? AND sortOrder = ?", new String[] {Integer.toString(topicId), Integer.toString(index+1)});
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    question = new Question();
                    question.setQuestionId(cursor.getInt(cursor.getColumnIndex("id")));
                    question.setQuestionText(cursor.getString(cursor.getColumnIndex("content")));
                    question.setMarkedForReview(cursor.getInt(cursor.getColumnIndex("reviewFlag")) == 1 ? true : false);
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return question;
    }

    public List<Question> fetchMarkedQuestions(int topicId) {
        SQLiteDatabase myDataBase = getReadableDatabase();
        List<Question> questionList = new ArrayList<Question>();
        String query = "select id, reviewFlag from question where topicId = ? order by sortOrder";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(query, new String[] {Integer.toString(topicId)});
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    do {
                        Question question = new Question();
                        question.setQuestionId(cursor.getInt(cursor.getColumnIndex("id")));
                        question.setMarkedForReview(cursor.getInt(cursor.getColumnIndex("reviewFlag")) == 1 ? true :false);
                        questionList.add(question);
                    }while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return questionList;
    }

    public List<String> fetchImages(int questionId) {
        SQLiteDatabase myDataBase = getReadableDatabase();
        List<String> imagesList = new ArrayList<String>();
        Cursor cursor = null;
        String query = "select imageName FROM question_image WHERE questionId = ?";
        try {
            cursor = myDataBase.rawQuery(query, new String[] {Integer.toString(questionId)});
            if ((cursor != null) && (cursor.moveToFirst())) {
                do {
                    imagesList.add(cursor.getString(cursor.getColumnIndex("imageName")));
                }while (cursor.moveToNext());
            }
        } catch (SQLiteException se) {
            Log.e(TAG, "Exception occurred while fetching images list");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }
        return imagesList;
    }

    public void markQuestion(int questionId, boolean markFlag){
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("reviewFlag", markFlag ? 1 : 0);
        myDataBase.update("question", cv, "id =?", new String[] {Integer.toString(questionId)});
        close();
    }

    /**
     * Saves the last viewed question in the topic to the database to enable continuing from where left
     * @param topicId
     * @param questionId
     */
    public void updateTopicQuestionMap(int topicId, int questionId) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("questionId", Integer.valueOf(questionId));

        if (myDataBase.update("topic_current_question", cv, "topicId = ?", new String[]{Integer.toString(topicId)}) == 0) {
            ContentValues cv2 = new ContentValues();
            cv2.put("questionId", Integer.valueOf(questionId));
            cv2.put("topicId", Integer.valueOf(topicId));
            myDataBase.insert("topic_current_question", null, cv2);
        }
        myDataBase.close();
    }


}
