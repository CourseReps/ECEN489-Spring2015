package com.ecen489.slidermenu;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ecen489.googlesignin.R;
import com.google.android.gms.common.SignInButton;

import org.json.simple.JSONObject;

public class FindPeopleFragment extends Fragment implements View.OnClickListener {
    static UserInfo myUser;
    Button send_friends;
    TextView myFriends;



    public FindPeopleFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        myUser = (UserInfo) getArguments().getSerializable("UserInfo");
        send_friends = (Button) rootView.findViewById(R.id.sendFriendsToServer);
        myFriends = (TextView) rootView.findViewById(R.id.friendList);
        System.out.println(myUser.getFriendsList().toString());
        myFriends.setText(myUser.getFriendsList().toString());
        // Button listeners
        send_friends.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        new FriendTask().execute();
    }
    public class FriendTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            CheckInClient.clientFriendHandler(myUser);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
