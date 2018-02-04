package com.rednineteen.android.sampleapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.rednineteen.android.sampleapp.adapters.RowAdapter;
import com.rednineteen.android.sampleapp.databinding.MainActivityBinding;
import com.rednineteen.android.sampleapp.models.Content;
import com.rednineteen.android.sampleapp.models.Row;
import com.rednineteen.android.sampleapp.net.ApiRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    public static final String API_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";

    private RowAdapter adapter;

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Initialize the list adapter
        adapter = new RowAdapter(this, queue, new ArrayList<Row>());
        binding.list.setAdapter(adapter);

        // Configure the api request to retrieve the content from the URL.
        ApiRequest request = new ApiRequest(Request.Method.GET, API_URL,
                new Response.Listener<Content>() {
                    @Override
                    public void onResponse(Content response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response != null && response.rows != null) {
                            adapter.addAll(response.rows);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        System.out.println("ERROR: " + error);
                    }
                });
        // Prevent request caching
        request.setShouldCache(false);
        // Trigger request by adding to the queue.
        queue.add(request);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
