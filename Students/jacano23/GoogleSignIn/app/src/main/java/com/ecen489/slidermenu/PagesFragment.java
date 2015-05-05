package com.ecen489.slidermenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecen489.googlesignin.R;

public class PagesFragment extends Fragment {
    static UserInfo myUser;
    private boolean signout = true;
    private boolean flag = true;
    private static boolean asyncFlag = false;
    View rootView = null;
	
	public PagesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        myUser = (UserInfo) getArguments().getSerializable("UserInfo");
        new logOutTask().execute();
        while(flag) {
            if (asyncFlag) {
                rootView = inflater.inflate(R.layout.fragment_pages, container, false);
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.putExtra("signout", signout);
                System.out.println("signout"+ signout);
                flag = false;
                asyncFlag = false;
                startActivity(intent);
            }

        }

        return rootView;
    }

    public class logOutTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            CheckInClient.clientLogoutHandler(myUser);
            asyncFlag = true;
            return response;
        }
    }
}
