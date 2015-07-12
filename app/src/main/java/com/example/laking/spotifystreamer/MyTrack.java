package com.example.laking.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  Class to store off artist track info
 */
public class MyTrack implements Parcelable {

    private String _name;
    private String _album;
    private String _imageUri;

    public MyTrack(String name, String album, String uri) {
        _name = name;
        _album = album;
        _imageUri = uri;
    }

    public String getAlbum () { return _album; }
    public String getName () {return _name; }
    public String getImage () {return _imageUri; }

    private MyTrack(Parcel in) {
        _name = in.readString();
        _album = in.readString();
        _imageUri = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_name);
        dest.writeString(_album);
        dest.writeString(_imageUri);
    }

    public static final Parcelable.Creator<MyTrack> CREATOR
            = new Parcelable.Creator<MyTrack>() {

        @Override
        public MyTrack createFromParcel(Parcel parcel) {
            return new MyTrack(parcel);
        }

        @Override
        public MyTrack[] newArray(int size) {
            return new MyTrack[size];
        }
    };
}
