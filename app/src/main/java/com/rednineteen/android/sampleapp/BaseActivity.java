package com.rednineteen.android.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created on 2/02/2018 by Juan Velasquez - email:  juan@rednineteen.com.
 */

public abstract class BaseActivity <T extends ViewDataBinding> extends AppCompatActivity {

    public T binding;

    public abstract int getActivityLayoutId();

    public abstract int getToolbarId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind and inflate the layout
        binding = DataBindingUtil.setContentView(this, getActivityLayoutId());
        // Set the activity Actionbar
        setActionToolBar( getToolbarId() );
    }

    public void setActionToolBar(int toolbarResId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarResId);
        if (toolbar != null) setSupportActionBar(toolbar);
    }

    public void setTitle(String title) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(title);
    }

    public void goTo(Class<? extends Activity> clazz, Bundle extras) {
        goTo(clazz, extras, false);
    }

    public void goTo(Class<? extends Activity> clazz, Bundle extras, boolean terminate) {
        Intent intent = new Intent(this, clazz);
        // Init extras if any
        if (extras != null) intent.putExtras(extras);
        startActivity(intent);
        if (terminate) finish();
    }
}
