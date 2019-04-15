package android.webinnovatives.com.seefgodriver.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.drawer.NotificationsActivity;
import android.webinnovatives.com.seefgodriver.models.Earning;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 4/12/2019.
 */

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.myVH> {
    List<Earning> parcels = new ArrayList<>();
    Context context;

    public EarningAdapter(List<Earning> parcels, Context context) {
        this.parcels = parcels;
        this.context = context;
    }

    class myVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price;

        public myVH(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
        }
    }

    @NonNull
    @Override
    public EarningAdapter.myVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EarningAdapter.myVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EarningAdapter.myVH myVH, int i) {

        Earning notification = parcels.get(i);
        myVH.source.setText("From: " + notification.getStart_point());
        myVH.destination.setText("To: " + notification.getEnd_point());
        myVH.date.setText(notification.getDate_time());
        myVH.itemname.setText(notification.getPackage_name());
        myVH.price.setText(notification.getPackage_price() + " rs");
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }
}
