package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.avisit.vijayam.model.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionDao extends DataBaseHelper{
    private static final String TAG = OptionDao.class.getSimpleName();

	public OptionDao(Context context){
		super(context);
	}
	
	public void persistOption(Option option) {
		ContentValues values = new ContentValues();
		values.put("optionId", option.getOptionId());
	    values.put("questionId", option.getQuestionId());
	    values.put("content", option.getOptionText());
	    values.put("correct", option.isCorrectFlag());
	    values.put("markedFlag", option.isSelectedFlag());
        SQLiteDatabase myDataBase = getWritableDatabase();
	    try{
	    	myDataBase.insertOrThrow("option", null, values);
	    } catch(SQLiteException se){
	    	
	    } finally{
	    	close();	    	
	    }
	}

	public List<Option> fetchOptions(int questionId) {
        SQLiteDatabase myDataBase = getReadableDatabase();
    	List<Option> optionList = new ArrayList<Option>();
    	Cursor cursor = null;
    	try {
	    	cursor = myDataBase.rawQuery("SELECT id, questionId, content, correct, markedFlag FROM option WHERE questionId = ? order by id", new String[] {Integer.toString(questionId)});
	    	if (cursor != null ) {
	    		if(cursor.moveToFirst()) {
	    			do {
	    				Option option = new Option();
	    				option.setOptionId(cursor.getInt(cursor.getColumnIndex("id")));
	    				option.setQuestionId(cursor.getInt(cursor.getColumnIndex("questionId")));
	    				option.setOptionText(cursor.getString(cursor.getColumnIndex("content")));
	    				option.setCorrectFlag(cursor.getInt(cursor.getColumnIndex("correct")) == 1 ? true :false);
	    				option.setSelectedFlag(cursor.getInt(cursor.getColumnIndex("markedFlag")) == 1 ? true :false);
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
        SQLiteDatabase myDataBase = getWritableDatabase();
		ContentValues cv1 = new ContentValues();
		cv1.put("markedFlag", 1);
		myDataBase.update("option", cv1, "questionId = ? AND optionId = ?", new String[]{Integer.toString(questionId), Integer.toString(optionId)});

		ContentValues cv2 = new ContentValues();
		cv2.put("markedFlag", 0);
        myDataBase.update("option", cv2, "questionId = ? AND optionId != ?", new String[]{Integer.toString(questionId), Integer.toString(optionId)});

        close();
	}

	public int fetchScore(int examCode) {
	    // refer the mitra project for code
        return 0;
    }
}
