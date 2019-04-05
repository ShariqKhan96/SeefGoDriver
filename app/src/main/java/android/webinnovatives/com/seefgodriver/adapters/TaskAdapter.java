package android.webinnovatives.com.seefgodriver.adapters;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 4/1/2019.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyVH> {
    List<Task> parcels = new ArrayList<>();
    Context context;

    public TaskAdapter(List<Task> parcels, Context context) {
        this.parcels = parcels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH myVH, int i) {
        Task parcel = parcels.get(i);
        myVH.price.setText(parcel.getPrice_range());
        myVH.source.setText("From: " + parcel.getStart_point());
        myVH.destination.setText("To: " + parcel.getEnd_point());
        myVH.date.setText(parcel.getDate_time());
        myVH.itemname.setText(parcel.getPackage_name());


        //different tabs
        //opportunities, accepted


        if (parcel.getPackage_status().equals("1")) {
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.pending_round_view));
            myVH.status.setText("Not Accepted");
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (parcel.getPackage_status().equals("2")) {
            myVH.status.setBackground(ContextCompat.getDrawable(context, R.drawable.accept_round_view));
            myVH.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            myVH.status.setText("Accepted");

        }


    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price, status;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);


        }
    }
}
