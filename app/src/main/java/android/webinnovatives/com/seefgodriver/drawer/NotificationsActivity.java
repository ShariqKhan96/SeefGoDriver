package android.webinnovatives.com.seefgodriver.drawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.adapters.NotificaitonAdapter;
import android.webinnovatives.com.seefgodriver.models.Notification;


import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView notificationsList;
    NotificaitonAdapter adapter;
    List<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels);

        notificationsList = findViewById(R.id.parcels_list);
        notificationsList.setLayoutManager(new LinearLayoutManager(this));
        notificationsList.setAdapter(new NotificaitonAdapter(notifications, this));

    }
}
