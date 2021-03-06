package android.webinnovatives.com.seefgodriver.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.models.Notification;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 4/1/2019.
 */

public class NotificaitonAdapter extends RecyclerView.Adapter<NotificaitonAdapter.MyVH> {
    List<Task> notifications = new ArrayList<>();
    Context context;

    public NotificaitonAdapter(List<Task> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_view, viewGroup, false));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull MyVH myVH, int i) {
        final Task notification = notifications.get(i);
        myVH.source.setText("From: " + notification.getStart_point());
        myVH.destination.setText("To: " + notification.getWarehouse_name());
        myVH.date.setText(notification.getDate_time());
        myVH.itemname.setText(notification.getPackage_name());

        myVH.track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                callService(notification.getId());
            }
        });
        //myVH.status.setText(parcel.getStatus());
    }

    private void callService(String id) {

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price, status;
        FrameLayout track_order;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            track_order = itemView.findViewById(R.id.track_order);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
        }
    }
}
