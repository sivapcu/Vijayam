package com.avisit.vijayam.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avisit.vijayam.R;
import com.avisit.vijayam.adapters.DashboardListViewAdapter;
import com.avisit.vijayam.dao.DashboardDao;
import com.avisit.vijayam.model.Dashboard;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Dashboard> dashboardList = new ArrayList<Dashboard>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        listView = (ListView) findViewById(R.id.dashBoardList);
        dashboardList = new DashboardDao(this).fetchDashboardItems();
        DashboardListViewAdapter dashboardAdapter = new DashboardListViewAdapter(this, dashboardList);
        listView.setAdapter(dashboardAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit?");
        alertDialog.setMessage("Do you really want to exit the Application?");
        alertDialog.setIcon(R.mipmap.vijayam_ic_launcher);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int dashboardItemId = dashboardList.get(position).getItemId();
        Intent intent = null;
        if(dashboardItemId == 2){
            intent = new Intent(parent.getContext(), MyCoursesActivity.class);
            parent.getContext().startActivity(intent);
            MainActivity.this.finish();
        } else if(dashboardItemId == 4){
            intent = new Intent(parent.getContext(), LoginActivity.class);
            parent.getContext().startActivity(intent);
            MainActivity.this.finish();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Yet to be implemented!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
