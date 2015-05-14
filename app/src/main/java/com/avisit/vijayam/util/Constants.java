package com.avisit.vijayam.util;

/**
 * Created by User on 4/28/2015.
 */
public class Constants {
/*    public static final String CREATE_TABLE_COURSE = "CREATE TABLE course (id INTEGER PRIMARY KEY NOT NULL, name TEXT, description TEXT, imageName TEXT, enabledFlag INTEGER, contentProviderId INTEGER, sortOrder INTEGER);";
    public static final String CREATE_TABLE_TOPIC = "CREATE TABLE topic (id INTEGER PRIMARY KEY NOT NULL, name TEXT, description TEXT, courseId INTEGER, enabledFlag INTEGER,  sortOrder INTEGER);";
    public static final String CREATE_TABLE_QUESTION = "CREATE TABLE question (id INTEGER PRIMARY KEY NOT NULL, topicId INTEGER, content TEXT, type INTEGER, sortOrder INTEGER, reviewFlag TEXT, winFlag TEXT);";
    public static final String CREATE_TABLE_OPTION = "CREATE TABLE option(optionId INTEGER NOT NULL, questionId NUMERIC, content TEXT, correct TEXT, markedFlag TEXT, sortOrder INTEGER, PRIMARY KEY(questionId, optionId));";
    public static final String CREATE_TABLE_QUESTION_IMAGE = "CREATE TABLE question_image (imageId INTEGER NOT NULL, questionId INTEGER NOT NULL, imageName TEXT, PRIMARY KEY(questionId, imageId));";
    public static final String CREATE_TABLE_APP_PARAM = "CREATE TABLE app_param (key TEXT PRIMARY KEY NOT NULL, value TEXT);";*/

    public static final String DB_NAME = "vijayam.db";

    public static final String COURSE_COMPLETED = "courseCompleted";
    public static final String COURSE_COUNT = "courseCount";
    public static final String CONTENT_PROVIDER_ID = "contentProviderId";
    public static final String TOPIC_COMPLETED ="topicCompleted";
    public static final String TOPIC_COUNT ="topicCount";
    public static final String QUESTION_COUNT ="questionCount";
    public static final String QUESTION_COMPLETED ="questionCompleted";

    public static final String URL_COURSES = "http://192.168.43.243:8080/vijayam/rest/courses/contentProvider";
    public static final String DB_ASSET_FILE_NAME = "vijayam_initial_db_setup.sqlite";

    public static final String DEVICE_REG_ID = "deviceRegId";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASSWORD = "userPassword";
    public static final String REGISTERED_VERSION = "registeredVersion";
    public static final String REGISTRATION_FLAG = "registeredWithServer";

}
