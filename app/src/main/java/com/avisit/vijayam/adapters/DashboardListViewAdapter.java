package com.avisit.vijayam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avisit.vijayam.R;
import com.avisit.vijayam.model.Dashboard;

import java.util.List;

/**
 * Created by User on 4/15/2015.
 */
public class DashboardListViewAdapter extends ArrayAdapter<Dashboard> {
    private Context context;
    private List<Dashboard> dashboardList;

    public DashboardListViewAdapter(Context context, List<Dashboard> dashboardList) {
        super(context, R.layout.dashboard_list_view, dashboardList);
        this.context = context;
        this.dashboardList = dashboardList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dashboard dashboard = dashboardList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.dashboard_list_view, parent, false);
        TextView itemNameTextView = (TextView) rowView.findViewById(R.id.content);
        itemNameTextView.setText(dashboard.getItemName());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(context.getResources().getIdentifier(dashboard.getImageName(), "drawable", context.getPackageName()));

        TextView notificationTextView = (TextView) rowView.findViewById(R.id.notification);
        notificationTextView.setText(""+(position+1));

        return rowView;
    }

}
