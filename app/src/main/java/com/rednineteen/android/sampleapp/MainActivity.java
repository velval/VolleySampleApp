package com.rednineteen.android.sampleapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.rednineteen.android.sampleapp.adapters.RowAdapter;
import com.rednineteen.android.sampleapp.databinding.MainActivityBinding;
import com.rednineteen.android.sampleapp.models.Content;
import com.rednineteen.android.sampleapp.models.Row;
import com.rednineteen.android.sampleapp.net.ApiRequest;
import com.rednineteen.android.sampleapp.net.OkHttpStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    public static final String API_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
    //public static final String API_URL = "https://dev.correllink.com/test.json";

    private RowAdapter adapter;
    private RequestQueue queue;
    private ApiRequest request;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show progress when first initialise.
        binding.swipeRefresh.setRefreshing(true);
        // Instantiate the RequestQueue. Use a custom HTTP Stack to allow for http to https redirects
        queue = Volley.newRequestQueue(this, new OkHttpStack());
        // Initialize the list adapter
        adapter = new RowAdapter(this, queue, new ArrayList<Row>());
        binding.list.setAdapter(adapter);

        // Configure the api request to retrieve the content from the URL.
        request = new ApiRequest(Request.Method.GET, API_URL,
                new Response.Listener<Content>() {
                    @Override
                    public void onResponse(Content response) {
                        if (response != null && response.rows != null) {
                            adapter.clear();
                            adapter.addAll(response.rows);
                            adapter.notifyDataSetChanged();
                            // Set ActionBar title
                            setTitle(response.title);
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR: " + error);
                        binding.swipeRefresh.setRefreshing(false);
                    }
                });
        // Prevent request caching
        request.setShouldCache(false);
        // Trigger request by adding to the queue.
        queue.add(request);

        binding.swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Prevent request caching
                        request.setShouldCache(false);
                        // Trigger request when swipe down to refresh
                        queue.add(request);
                    }
                }
        );

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.swipeRefresh.setRefreshing(true);
                // Prevent request caching
                request.setShouldCache(false);
                // Trigger request when refresh button pressed
                queue.add(request);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
