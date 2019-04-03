package android.webinnovatives.com.seefgodriver.drawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.adapters.TaskAdapter;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    RecyclerView parcelList;
    List<Task> parcels = new ArrayList<>();
    TaskAdapter adapter;
    FrameLayout no_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("My Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no_records = findViewById(R.id.no_records);

        parcelList = findViewById(R.id.parcels_list);
        parcelList.setLayoutManager(new LinearLayoutManager(this));
        getList();


    }

    private void getList() {
        if (parcels.size() > 0)
            parcelList.setAdapter(new TaskAdapter(parcels, this));
        else no_records.setVisibility(View.VISIBLE);
    }
}
