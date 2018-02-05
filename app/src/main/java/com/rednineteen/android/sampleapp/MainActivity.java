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
import android.widget.AdapterView;

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
import com.google.gson.Gson;
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

    public static final String KEY_DATA = "KEY_DATA";
    public static final String API_URL  = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
    //public static final String API_URL  = "https://dl.dropboxusercontent.co";
    //public static final String API_URL = "https://dev.correllink.com/test.json";

    private RowAdapter adapter;
    private ApiRequest request;
    private Content content;

    private Gson gson = new Gson();

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

        binding.swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        createSendRequest();
                    }
                }
        );

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.swipeRefresh.setRefreshing(true);
                createSendRequest();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize the list adapter
        if (content != null) {
            // If content already loaded, use it.
            adapter = new RowAdapter(this, content.rows);
            // Set ActionBar title
            setTitle(content.title);
        } else {
            // If no content fetch it from the API.
            adapter = new RowAdapter(this, new ArrayList<Row>());
            // Show progress as we are making a network call.
            binding.swipeRefresh.setRefreshing(true);
            createSendRequest();
        }
        // Bind adapter to the listView
        binding.list.setAdapter(adapter);
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Row item = content.rows.get(position);
                Bundle extras = new Bundle();
                extras.putString(DetailActivity.KEY_TITLE, item.title);
                extras.putString(DetailActivity.KEY_DESC, item.description);
                extras.putString(DetailActivity.KEY_IMAGE, item.imageHref);
                goTo(DetailActivity.class, extras);
            }
        });
    }

    private void createSendRequest() {
        // Configure the api request to retrieve the content from the URL.
        if (request == null) {
            request = new ApiRequest(Request.Method.GET, API_URL,
                    new Response.Listener<Content>() {
                        @Override
                        public void onResponse(Content response) {
                            binding.list.setVisibility(View.VISIBLE);
                            binding.errorContent.setVisibility(View.GONE);
                            if (response != null && response.rows != null) {
                                //Set content for future use for example if screen is rotated
                                content = response;
                                // Clear and refresh adapter data
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
                            binding.swipeRefresh.setRefreshing(false);
                            binding.errorContent.setVisibility(View.VISIBLE);
                            binding.list.setVisibility(View.GONE);
                        }
            });
        }
        // Prevent request caching
        request.setShouldCache(false);
        // Trigger request by adding to the queue.
        App.getInstance().addToRequestQueue(request);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DATA, gson.toJson(content));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        content = gson.fromJson( savedInstanceState.getString(KEY_DATA), Content.class );
    }
}
