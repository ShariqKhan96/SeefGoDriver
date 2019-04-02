package android.webinnovatives.com.seefgodriver.drawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.adapters.TaskAdapter;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    RecyclerView parcelList;
    List<Task> parcels = new ArrayList<>();
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Task History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        parcelList = findViewById(R.id.parcels_list);
        parcelList.setLayoutManager(new LinearLayoutManager(this));
        parcelList.setAdapter(new TaskAdapter (parcels, this));


    }

    private List<Task> getList() {
        return null;
    }
}
