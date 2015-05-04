package com.ecen489.slidermenu;

import com.ecen489.slidermenu.MainActivity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecen489.googlesignin.R;

import org.w3c.dom.Text;

import java.io.InputStream;

public class HomeFragment extends Fragment {
    ProfileInformation profileInformation;
    ImageView profileImage;
    TextView email;
    TextView name;
    // Profile pic image size in pixels

	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        profileInformation = (ProfileInformation) getArguments().getSerializable("profileInformation");

        profileImage = (ImageView) rootView.findViewById(R.id.imgProfilePicture);
        new LoadProfileImage(profileImage).execute(profileInformation.getImageUrl());

        email = (TextView) rootView.findViewById(R.id.txtEmail);
        name = (TextView) rootView.findViewById(R.id.txtName);

        email.setText(profileInformation.getEmail());
        name.setText(profileInformation.getName());



        return rootView;

    }
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
