package android.webinnovatives.com.seefgodriver.drawer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.adapters.TaskAdapter;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.models.Task;
import android.webinnovatives.com.seefgodriver.network.VolleySingleton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class TaskActivity extends AppCompatActivity {

    RecyclerView parcelList;
    List<Task> parcels = new ArrayList<>();
    TaskAdapter adapter;
    FrameLayout no_records;
    Driver user;

    SwipeRefreshLayout swipe_layout;


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
        user = Paper.book().read(ConstantManager.CURRENT_USER);
        parcelList = findViewById(R.id.parcels_list);
        parcelList.setLayoutManager(new LinearLayoutManager(this));

        swipe_layout = findViewById(R.id.swipe_layout);
        int Colors[] = {android.R.color.holo_red_dark, android.R.color.holo_orange_light};
        swipe_layout.setColorSchemeResources(Colors);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });
        getList();


    }

    private void getList() {
        final ProgressDialog dialog = new ProgressDialog(TaskActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Getting Tasks");
        dialog.setMessage("Please Wait");
        // dialog.show();
        swipe_layout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "driveraccepted.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  dialog.dismiss();
                        swipe_layout.setRefreshing(false);
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<Task>>() {
                                }.getType();
                                List<Task> parcels = gson.fromJson(response, listType);

                                parcelList.setAdapter(new TaskAdapter(parcels, TaskActivity.this, user.getDriver_id()));

                            } else {
                                no_records.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dialog.dismiss();
                        swipe_layout.setRefreshing(false);
                        Toast.makeText(TaskActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", user.getDriver_id());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//        if (parcels.size() > 0)

    }
}
