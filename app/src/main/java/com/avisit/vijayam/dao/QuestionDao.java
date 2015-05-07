package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
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
    private Context context;
    private static final String TAG = QuestionDao.class.getSimpleName();
    public static final String TBL_QUESTION = "Question";
    public static final String CLMN_QUES_QUES_ID = "_id";
    public static final String CLMN_TOPIC_ID = "topic_id";
    public static final String CLMN_QUES_TEXT = "question_text";
    public static final String CLMN_SORT_ORDER = "sort_order";
    public static final String CLMN_MARKED_FLAG = "review_flag";
    public static final String CLMN_WIN_FLAG = "win_flag";


    public QuestionDao(Context context){
        super(context);
        this.context = context;
    }

    public long persistQuestion(Question question) {
        long rowId = 0;
        ContentValues values = new ContentValues();
        values.put(CLMN_QUES_QUES_ID, question.getQuestionId());
        values.put(CLMN_QUES_TEXT, question.getQuestionText());
        values.put(CLMN_MARKED_FLAG, question.isMarkedForReview());
        values.put(CLMN_TOPIC_ID, question.getTopicId());
        values.put(CLMN_SORT_ORDER, question.getSortOrder());
        values.put(CLMN_WIN_FLAG, question.isWinFlag());
        SQLiteDatabase myDataBase = getWritableDatabase();
        try{
            rowId = myDataBase.insert(TBL_QUESTION, null, values);
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
            cursor = myDataBase.rawQuery("SELECT count(_id) as count FROM Question where topic_id = ?", new String[]{Integer.toString(topicId)});
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
            cursor = myDataBase.rawQuery("SELECT question_id FROM TopicQuestionMap WHERE topic_id = ?", new String[]{Integer.toString(topicId)});
            if (cursor != null ) {
                if (cursor.moveToFirst()) {
                    questionId = cursor.getInt(cursor.getColumnIndex("question_id"));
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
            cursor = myDataBase.rawQuery("SELECT _id, question_text, review_flag FROM Question where topic_id = ? AND sort_order = ?", new String[] {Integer.toString(topicId), Integer.toString(index+1)});
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    question = new Question();
                    question.setQuestionId(cursor.getInt(cursor.getColumnIndex("_id")));
                    question.setQuestionText(cursor.getString(cursor.getColumnIndex("question_text")));
                    question.setMarkedForReview(cursor.getInt(cursor.getColumnIndex("review_flag")) == 1 ? true : false);
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
        String query = "select _id, review_flag from Question where topic_id = ? order by sort_order";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(query, new String[] {Integer.toString(topicId)});
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    do {
                        Question question = new Question();
                        question.setQuestionId(cursor.getInt(cursor.getColumnIndex("_id")));
                        question.setMarkedForReview(cursor.getInt(cursor.getColumnIndex("review_flag")) == 1 ? true :false);
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
        String query = "select image_name FROM QuestionImage WHERE question_id = ?";
        try {
            cursor = myDataBase.rawQuery(query, new String[] {Integer.toString(questionId)});
            if ((cursor != null) && (cursor.moveToFirst())) {
                do {
                    imagesList.add(cursor.getString(cursor.getColumnIndex("image_name")));
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
        cv.put(CLMN_MARKED_FLAG, markFlag ? 1 : 0);
        myDataBase.update("Question", cv, CLMN_QUES_QUES_ID+" =?", new String[] {Integer.toString(questionId)});
        close();
    }

    public void updateTopicQuestionMap(int topicId, int questionId) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("question_id", Integer.valueOf(questionId));

        if (myDataBase.update("TopicQuestionMap", cv, "topic_id = ?", new String[]{Integer.toString(topicId)}) == 0) {
            ContentValues cv2 = new ContentValues();
            cv2.put("question_id", Integer.valueOf(questionId));
            cv2.put("topic_id", Integer.valueOf(topicId));
            myDataBase.insert("TopicQuestionMap", null, cv2);
        }
        close();
    }


}
