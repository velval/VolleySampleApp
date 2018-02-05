package com.rednineteen.android.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rednineteen.android.sampleapp.databinding.DetailActivityBinding;
import com.rednineteen.android.sampleapp.databinding.MainActivityBinding;

public class DetailActivity extends BaseActivity<DetailActivityBinding> {

    public static final String KEY_TITLE    = "KEY_TITLE";
    public static final String KEY_DESC     = "KEY_DESC";
    public static final String KEY_IMAGE    = "KEY_IMAGE";

    private String title;
    private String description;
    private String imageUrl;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title       = getIntent().getStringExtra(KEY_TITLE);
        description = getIntent().getStringExtra(KEY_DESC);
        imageUrl    = getIntent().getStringExtra(KEY_IMAGE);

        setTitle(title);
        binding.itemDescription.setText(description);
        binding.itemImage.setDefaultImageResId(R.drawable.default_image);
        binding.itemImage.setErrorImageResId(R.drawable.image_not_found);
        binding.itemImage.setImageUrl(imageUrl, App.getInstance().getImageLoader());
    }
}
