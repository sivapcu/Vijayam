package com.avisit.vijayam.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.avisit.vijayam.model.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionDao extends DataBaseHelper{
    private static final String TAG = OptionDao.class.getSimpleName();
	private static final String TBL_OPTIONS = "Option";
	private static final String CLMN_QUES_ID = "question_id";
    private static final String CLMN_OPT_ID = "option_id";
	private static final String CLMN_OPT_TEXT = "option_text";
	private static final String CLMN_CORRECT_FLAG = "correct_flag";
	private static final String CLMN_MARKED_FLAG = "marked_flag";

	
	public OptionDao(Context context) {
		super(context);

	}
	
	public void persistOption(Option option) {
		ContentValues values = new ContentValues();
		values.put(CLMN_OPT_ID, option.getOptionId());
	    values.put(CLMN_QUES_ID, option.getQuestionId());
	    values.put(CLMN_OPT_TEXT, option.getOptionText());
	    values.put(CLMN_CORRECT_FLAG, option.isCorrectFlag());
	    values.put(CLMN_MARKED_FLAG, option.isSelectedFlag());
	    openWritableDataBase();
	    try{
	    	myDataBase.insertOrThrow(TBL_OPTIONS, null, values);	
	    } catch(SQLiteException se){
	    	
	    } finally{
	    	close();	    	
	    }
	}

	public List<Option> fetchOptions(int questionId) {
		openReadableDataBase();
    	List<Option> optionList = new ArrayList<Option>();
    	Cursor cursor = null;
    	try {
	    	cursor = myDataBase.rawQuery("SELECT option_id, question_id, option_text, correct_flag, marked_flag FROM Option WHERE question_id = ? order by option_id", new String[] {Integer.toString(questionId)});
	    	if (cursor != null ) {
	    		if(cursor.moveToFirst()) {
	    			do {
	    				Option option = new Option();
	    				option.setOptionId(cursor.getInt(cursor.getColumnIndex("option_id")));
	    				option.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
	    				option.setOptionText(cursor.getString(cursor.getColumnIndex("option_text")));
	    				option.setCorrectFlag(cursor.getInt(cursor.getColumnIndex("correct_flag")) == 1 ? true :false);
	    				option.setSelectedFlag(cursor.getInt(cursor.getColumnIndex("marked_flag")) == 1 ? true :false);
	    				optionList.add(option);
	    			} while (cursor.moveToNext());
	    		}
	    	}
	    } catch (SQLiteException se ) {
	    	System.out.println("Could not Open and Query the database");
	    } finally {
	    	if(cursor!=null){
	    		cursor.close();
	    	}
	    	close();
	    }
		return optionList;
	}

	public void updateSelected(int questionId, int optionId) {
		openWritableDataBase();
		ContentValues cv1 = new ContentValues();
		cv1.put(CLMN_MARKED_FLAG, 1);
		myDataBase.update(TBL_OPTIONS, cv1, CLMN_QUES_ID+" = ? AND "+CLMN_OPT_ID+" = ?", new String[] {Integer.toString(questionId), Integer.toString(optionId)});

		ContentValues cv2 = new ContentValues();
		cv2.put(CLMN_MARKED_FLAG, 0);
        myDataBase.update(TBL_OPTIONS, cv2, CLMN_QUES_ID+" = ? AND "+CLMN_OPT_ID+" != ?", new String[] {Integer.toString(questionId), Integer.toString(optionId)});

        close();
	}

	public int fetchScore(int examCode) {
	    // refer the mitra project for code
        return 0;
    }
}
