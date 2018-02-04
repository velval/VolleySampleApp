package com.rednineteen.android.sampleapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rednineteen.android.sampleapp.R;
import com.rednineteen.android.sampleapp.models.Row;

import java.util.List;

/**
 * Created on 2/02/2018 by Juan Velasquez - email:  juan@rednineteen.com.
 */

public class RowAdapter extends ArrayAdapter<Row> {

    ImageLoader imageLoader;

    public RowAdapter(@NonNull Context context, RequestQueue queue, @NonNull List<Row> objects) {
        super(context, 0, objects);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        });
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RowViewHolder holder;
        // Get the item for this position
        Row row = getItem(position);
        // Check for existing views to reuse them
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new RowViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RowViewHolder) convertView.getTag();
        }

        holder.titleText.setText(row.title);
        holder.descText.setText(row.description);
        holder.imageView.setDefaultImageResId(R.drawable.default_image);
        holder.imageView.setErrorImageResId(R.drawable.ic_launcher_background);
        holder.imageView.setImageUrl(row.imageHref, imageLoader);

        return convertView;
    }

    static class RowViewHolder {
        TextView titleText;
        TextView descText;
        NetworkImageView imageView;

        public RowViewHolder(View view) {
            this.titleText  = (TextView) view.findViewById(R.id.itemTitle);
            this.descText   = (TextView) view.findViewById(R.id.itemDescription);
            this.imageView  = (NetworkImageView) view.findViewById(R.id.itemImage);
        }
    }
}
