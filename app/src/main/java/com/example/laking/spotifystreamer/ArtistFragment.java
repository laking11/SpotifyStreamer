package com.example.laking.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * Artist search fragment for Spotify.
 */
public class ArtistFragment extends Fragment {
    private static final String LOG_TAG = ArtistFragment.class.getSimpleName();
    private static final String STATE_ARTIST = "state_artist";

    private ArtistAdapter mArtistAdapter;
    private ArrayList<MyArtist> mArtistList;
    private SearchView sv;
    private ListView lv;

    public ArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if have instanceState
        if (savedInstanceState != null) {
            mArtistList = savedInstanceState.getParcelableArrayList(STATE_ARTIST);
        } else {
            mArtistList = new ArrayList<MyArtist>();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_ARTIST, mArtistList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root  = inflater.inflate(R.layout.fragment_artist, container, false);

        //  Setup the text enter view
        sv = (SearchView) root.findViewById(R.id.edit_search);
        sv.setQueryHint(getString(R.string.enter_artist));
        sv.setIconified(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String keyWord = sv.getQuery().toString();
                if (!keyWord.isEmpty()) {
                    startSearch(keyWord);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mArtistAdapter = new ArtistAdapter(getActivity(), mArtistList);
        lv = (ListView) root.findViewById(R.id.list_search_results);
        lv.setAdapter(mArtistAdapter);

        // Add item click listener that will launch top tracks activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artistId = mArtistAdapter.getItem(position).getId();
                Intent trkIntent = new Intent(getActivity(), TracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artistId);
                startActivity(trkIntent);
            }
        });

        return root;
    }

    private void startSearch(String name) {
        SearchTask st = new SearchTask();
        st.execute(name);
    }

    public class SearchTask extends AsyncTask<String, Void, ArtistsPager> {
        protected ArtistsPager artists;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                artists = spotify.searchArtists(params[0]);
            } catch (RetrofitError e) {
                Log.e(LOG_TAG, "Error: ", e);
                artists = null;
            }
            return artists;
        }

        @Override
        protected void onPostExecute(ArtistsPager ap) {
//            Toast.makeText(getActivity(),"PostExecuting...",
//                    Toast.LENGTH_SHORT).show();
            mArtistAdapter.clear();

            if (ap != null && ap.artists.items.size() > 0) {
                for (Artist a : ap.artists.items) {
                    String imgUrl = "";
                    if (a.images.size() > 0 ) {
                        int imgPos=0;
                        if (a.images.size() > 2) {
                            imgPos = a.images.size() - 2;
                        }
                        imgUrl = a.images.get(imgPos).url;
                    }
                    mArtistList.add(new MyArtist(a.name, a.id, imgUrl));
                }
                mArtistAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(),sv.getQuery().toString()
                                + " " + getString(R.string.artist_not_found),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
