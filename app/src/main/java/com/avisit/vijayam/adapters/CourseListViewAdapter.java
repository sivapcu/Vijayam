package com.avisit.vijayam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avisit.vijayam.R;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Dashboard;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class CourseListViewAdapter extends ArrayAdapter<Course> {
    private Context context;
    private List<Course> courseList;

    public CourseListViewAdapter(Context context, List<Course> courseList) {
        super(context, R.layout.courses_list_view, courseList);
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.courses_list_view, parent, false);

        Course course = courseList.get(position);
        // TODO check how to download images from internet and use it in the list
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//        imageView.setImageResource(context.getResources().getIdentifier(course.getImageName(), "drawable", context.getPackageName()));

        TextView courseName = (TextView) rowView.findViewById(R.id.content);
        courseName.setText(course.getCourseName());

        // TODO decide whether to have a description for course list
        // TODO  also decide whether to show any notifications like new_topics_available or pending_topics etc

        return rowView;
    }
}
