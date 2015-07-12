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
public class TrackAdapter extends ArrayAdapter<MyTrack> {

    private static class ViewHolder {
        TextView name;
        TextView album;
        ImageView image;
    }

    public TrackAdapter(Context context, ArrayList<MyTrack> data) {
        // Pass 0 in as resource as getView method hard codes the resource
        // to use
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        MyTrack t = getItem(position);

        if (convertView == null) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.item_track, parent, false);

            vh.name = (TextView) convertView.findViewById(R.id.text_track_name);
            vh.album = (TextView) convertView.findViewById(R.id.text_album_name);
            vh.image = (ImageView) convertView.findViewById(R.id.image_album);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        String imageUrl = "https:/api.spotify.com/";
        if (!t.getImage().isEmpty()) {
            imageUrl = t.getImage();
        }

        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.loading_test).
                error(R.drawable.no_image).into(vh.image);

        vh.name.setText(t.getName());
        vh.album.setText(t.getAlbum());
        //notifyDataSetChanged();
        return convertView;
    }
}

