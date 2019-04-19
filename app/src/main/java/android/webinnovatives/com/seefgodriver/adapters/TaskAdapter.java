package android.webinnovatives.com.seefgodriver.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by hp on 4/1/2019.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyVH> {
    List<Task> parcels = new ArrayList<>();
    Context context;
    String driver_id;
    Driver driver;

    public TaskAdapter(List<Task> parcels, Context context, String driver_id) {
        this.parcels = parcels;
        this.context = context;
        this.driver_id = driver_id;
        driver = Paper.book().read(ConstantManager.CURRENT_USER);
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH myVH, int i) {
        final Task parcel = parcels.get(i);
        myVH.price.setText(parcel.getPackage_price());
        myVH.source.setText("From: " + parcel.getStart_point());
        myVH.destination.setText("Destination: " + parcel.getWarehouse_name());
        myVH.date.setText(parcel.getDate_time());
        myVH.itemname.setText(parcel.getPackage_name());


        //different tabs
        //opportunities, accepted

        if (parcel.getStatus().equals("0")) {

            myVH.deciderClockBtns.setVisibility(View.GONE);
        } else if (parcel.getStatus().equals("1")) {
            myVH.deciderClockBtns.setVisibility(View.VISIBLE);
            //myVH.acceptTask.setVisibility(View.GONE);
            myVH.endTask.setBackgroundResource(R.drawable.disable_sheet);
            myVH.endTask.setEnabled(false);
            myVH.startTask.setBackgroundColor(Color.parseColor("#FF4E7E49"));
        } else if (parcel.getStatus().equals("2")) {
            myVH.deciderClockBtns.setVisibility(View.VISIBLE);
            //myVH.acceptTask.setVisibility(View.GONE);
            myVH.startTask.setBackgroundResource(R.drawable.disable_sheet);
            myVH.startTask.setEnabled(false);
            myVH.endTask.setEnabled(true);
            myVH.endTask.setBackgroundResource(R.drawable.end_sheet);
        }

        myVH.startTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusToServer(parcels.get(myVH.getAdapterPosition()), "2", myVH.getAdapterPosition());
            }
        });
        myVH.endTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusToServer(parcels.get(myVH.getAdapterPosition()), "3", myVH.getAdapterPosition());
            }
        });


    }

    private void updateStatusToServer(final Task task, final String status, final int position) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setMessage("Please wait..");
        dialog.show();
        ;
        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "packagestatus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE :", response);
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {

                        parcels.get(position).setStatus(status);
                        notifyDataSetChanged();
                        if (status.equals("3")) {
                            parcels.remove(task);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Task Completed!", Toast.LENGTH_SHORT).show();
                        }
                        //task.setStatus(status);

                    }
                } catch (JSONException e) {
                    Log.e(TaskAdapter.class.getSimpleName(), "" + e.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR :", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status", status);
                map.put("driverid", driver.getDriver_id());
                map.put("id", parcels.get(position).getPackage_id());
                map.put("check", "1");
                return map;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price;
        FrameLayout acceptTask, startTask, endTask;
        LinearLayout deciderClockBtns;


        public MyVH(@NonNull View itemView) {
            super(itemView);
            deciderClockBtns = itemView.findViewById(R.id.clock_btns_layout);
            startTask = itemView.findViewById(R.id.start_task);
            endTask = itemView.findViewById(R.id.end_task);

            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);


        }
    }
}
