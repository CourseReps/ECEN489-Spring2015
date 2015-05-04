package com.ecen489.slidermenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecen489.googlesignin.R;

public class PagesFragment extends Fragment {

    private boolean signout = true;
	
	public PagesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pages, container, false);
        Intent intent = new Intent(getActivity(),SignInActivity.class);
        intent.putExtra("signout", signout);
        System.out.println(signout);
        startActivity(intent);


        return rootView;
    }
}
