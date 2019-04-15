package android.webinnovatives.com.seefgodriver.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.models.Opportunities;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.widget.FrameLayout;
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
 * Created by hp on 4/14/2019.
 */

public class OpportunitiesAdapter extends RecyclerView.Adapter<OpportunitiesAdapter.ViewHolder> {
    List<Opportunities> notifications = new ArrayList<>();
    Context context;
    Driver driver;

    public OpportunitiesAdapter(List<Opportunities> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
        driver = Paper.book().read(ConstantManager.CURRENT_USER);
    }

    @NonNull
    @Override
    public OpportunitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OpportunitiesAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.opp_layout, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final OpportunitiesAdapter.ViewHolder myVH, int i) {

        Opportunities parcel = notifications.get(i);

        myVH.price.setText(parcel.getPrice_range());
        myVH.source.setText("From: " + parcel.getStart_point());
        myVH.destination.setText("To: " + parcel.getEnd_point());
        myVH.date.setText(parcel.getDate_time());
        myVH.itemname.setText(parcel.getPackage_name());

        myVH.accept_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusToServer("1", myVH.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView source, destination, itemname, date, price;
        FrameLayout accept_task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            itemname = itemView.findViewById(R.id.itemname);
            date = itemView.findViewById(R.id.date);
            accept_task = itemView.findViewById(R.id.accept_task);
            price = itemView.findViewById(R.id.price);
        }
    }

    private void updateStatusToServer(final String status, final int position) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Accepting");
        dialog.setMessage("Please wait");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "packagestatus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE :", response);
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {
                        //task.setStatus(status);
                        Toast.makeText(context, "Opportunity Accepted", Toast.LENGTH_SHORT).show();
                        try {
                            notifications.remove(position);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("RemovingException", e.getMessage());
                        }


                    }
                } catch (JSONException e) {
                    Log.e(TaskAdapter.class.getSimpleName(), "" + e.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR :", error.getMessage());
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status", status);
                map.put("driverid", driver.getDriver_id());
                map.put("id", notifications.get(position).getPackage_id());
                map.put("check", "0");

                Log.e("All", status+" "+driver.getDriver_id()+" "+notifications.get(position).getPackage_id());

                return map;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
