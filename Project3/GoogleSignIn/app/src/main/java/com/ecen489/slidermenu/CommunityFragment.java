package com.ecen489.slidermenu;

import android.app.Fragment;
import android.app.job.JobScheduler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecen489.googlesignin.R;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    static UserInfo myUser;
    private ArrayList<String> locationList;
    ArrayList<LocWithCheckIns> checkIns = new ArrayList<LocWithCheckIns>();
    private static ArrayAdapter<LocWithCheckIns> mFriendAdapter;
    private TextView myText;

    ListView locations1;

	public CommunityFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        myUser = (UserInfo) getArguments().getSerializable("UserInfo");
        locationList = (ArrayList<String>) getArguments().get("locationList");
        mFriendAdapter = new ArrayAdapter<LocWithCheckIns>(
                this.getActivity(), R.layout.circle_member, checkIns);
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        locations1 = (ListView) rootView.findViewById(R.id.recentFriendsLocations1);
        myText = (TextView) rootView.findViewById(R.id.txtLabel1);


        new recentLocationsTask().execute();




        return rootView;
    }


    public class recentLocationsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            checkIns.clear();
            checkIns = CheckInClient.clientRecentLocations(myUser, locationList);


            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            //locations1.setAdapter(mFriendAdapter);
            //mFriendAdapter.notifyDataSetChanged();
            String string = "";
            for(LocWithCheckIns locWithCheckIn:checkIns){
                string = string+locWithCheckIn.toString()+"\n\n";

            }
            myText.setText(string);
            checkIns.clear();
            locationList.clear();
        }
    }
}
