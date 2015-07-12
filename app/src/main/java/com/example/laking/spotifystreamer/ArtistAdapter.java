package com.example.laking.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by laking on 6/26/15.
 */
public class ArtistAdapter extends ArrayAdapter<MyArtist> {

    private static class ViewHolder {
        TextView name;
        ImageView image;
    }

    public ArtistAdapter(Context context, ArrayList<MyArtist> data) {
        // Pass 0 in as resource as getView method hard codes the resource
        // to use
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        MyArtist a = getItem(position);

        if (convertView == null) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.item_artist, parent, false);

            vh.name = (TextView) convertView.findViewById(R.id.text_artist_name);
            vh.image = (ImageView) convertView.findViewById(R.id.image_artist);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String imageUrl = "https:/api.spotify.com/";
        if (! a.getImage().isEmpty()) {
            imageUrl = a.getImage();
        }

        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.loading_test).
                error(R.drawable.no_image).into(vh.image);

        vh.name.setText(a.getName());
        notifyDataSetChanged();
        return convertView;
    }
}

