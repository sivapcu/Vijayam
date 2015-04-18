package com.avisit.vijayam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avisit.vijayam.R;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Topic;

import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class TopicListViewAdapter extends ArrayAdapter<Topic> {
    private Context context;
    private List<Topic> topicList;

    public TopicListViewAdapter(Context context, List<Topic> topicList) {
        super(context, R.layout.topics_list_view, topicList);
        this.context = context;
        this.topicList = topicList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.topics_list_view, parent, false);

        Topic topic = topicList.get(position);
        // TODO check how to download images from internet and use it in the list
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//        imageView.setImageResource(context.getResources().getIdentifier(topic.getImageName(), "drawable", context.getPackageName()));

        TextView topicName = (TextView) rowView.findViewById(R.id.content);
        topicName.setText(topic.getTopicName());

        // TODO decide whether to have a description for topic list
        // TODO  also decide whether to show any notifications like new_topics_available or pending_topics etc

        return rowView;
    }
}
