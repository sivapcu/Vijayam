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

    public static final String DB_ASSET_FILE_NAME = "vijayam_initial_db_setup.sqlite";

    public static final String DEVICE_REG_ID = "deviceRegId";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASSWORD = "userPassword";
    public static final String REGISTERED_VERSION = "registeredVersion";
    public static final String REGISTRATION_FLAG = "registeredWithServer";

//    public static final String HOST_NAME = "10.0.2.2"; //emulator
//    public static final String HOST_NAME = "192.168.1.4"; //WIFI
    public static final String HOST_NAME = "192.168.43.243"; //mobile data
//    public static final String HOST_NAME = "ec2-52-25-72-42.us-west-2.compute.amazonaws.com"; //production server

    public static final String USER_REGISTER_URL = "http://"+HOST_NAME+":8080/vijayam/rest/user/register";
    public static final String COURSES_BY_CONTENT_PROVIDER = "http://"+HOST_NAME+":8080/vijayam/rest/courses/contentProvider/";
    public static final String QUES_BY_TOPIC = "http://"+HOST_NAME+":8080/vijayam/rest/questions/byTopic/";
}
