package com.example.laking.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * Fragment for tracks given a specific artistId.
 */
public class TracksFragment extends Fragment {
    private final String LOG_TAG = TracksFragment.class.getSimpleName();
    private static final String STATE_TRACKS = "state_tracks";

    private TrackAdapter mTrackAdapter;
    private ArrayList<MyTrack> mTrackList;
    private String artistId;

    public TracksFragment() {
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if have instanceState
        if (savedInstanceState != null) {
            mTrackList = savedInstanceState.getParcelableArrayList(STATE_TRACKS);
        } else {
            mTrackList = new ArrayList<MyTrack>();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_TRACKS, mTrackList);
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        if (!artistId.isEmpty()) {
            startSearch(artistId);
        }
        View root  = inflater.inflate(R.layout.fragment_tracks, container, false);

        mTrackAdapter = new TrackAdapter(getActivity(), mTrackList);

        ListView lv = (ListView) root.findViewById(R.id.list_artist_results);
        lv.setAdapter(mTrackAdapter);

        return root;
    }

    private void startSearch(String name) {
        SearchTask st = new SearchTask();
        st.execute(name);
    }

    public class SearchTask extends AsyncTask<String, Void, Tracks> {
        protected Tracks tracks;

        @Override
        protected Tracks doInBackground(String... params) {
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                Map<String, Object> options = new HashMap<>();
                options.put("country", "US");
                tracks = spotify.getArtistTopTrack(params[0], options);
            } catch (RetrofitError e) {
                Log.e(LOG_TAG, "Error: ", e);
                tracks = null;
            }
            return tracks;
        }

        @Override
        protected void onPostExecute(Tracks tp) {
            mTrackAdapter.clear();
            if (tp != null && tp.tracks.size() > 0) {
                for (Track t : tp.tracks) {
                    String imgUrl = "";
                    if (t.album.images.size() > 0 ) {
                        imgUrl = t.album.images.get(0).url;
                    }
                    mTrackList.add(new MyTrack(t.name, t.album.name,imgUrl));
                }
                mTrackAdapter.notifyDataSetChanged();
                String title = tp.tracks.get(0).artists.get(0).name + "'s Top 10 Tracks";
                getActivity().setTitle(title);
            } else {
                Toast.makeText(getActivity(),R.string.tracks_not_found,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
