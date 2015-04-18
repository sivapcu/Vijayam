package com.avisit.vijayam.model;

/**
 * Created by User on 4/16/2015.
 */
public class Course {
    private int courseId;
    private String courseName;
    private String description;
    private int imageId;
    private String imageName;

    public Course(){

    }

    public Course(int courseId, String courseName, String imageName) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.imageName = imageName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String toString() {
        return this.courseName;
    }
}
