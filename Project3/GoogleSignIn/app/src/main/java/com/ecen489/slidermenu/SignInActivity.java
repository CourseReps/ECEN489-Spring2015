package com.ecen489.slidermenu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecen489.googlesignin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SignInActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<LoadPeopleResult>,
         View.OnClickListener {

    public static UserInfo myUser = new UserInfo();

    // Check In Client
    private CheckInClient client = new CheckInClient();

    private static final String TAG = "Google-Sign-In";
    private static final int RC_SIGN_IN = 0;


    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;



    private static final String SAVED_PROGRESS = "sign_in_progress";

    private GoogleApiClient mGoogleApiClient;

    private int mSignInProgress;

    private PendingIntent mSignInIntent;

    private int mSignInError;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    private SignInButton mSignInButton;

    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;

    private boolean signout = false;
   boolean friends_flag =false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_sign_in);


        mSignInButton = (SignInButton) findViewById(R.id.myGoogleSignIn);


        // Button listeners
        mSignInButton.setOnClickListener(this);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);


        mCirclesList = new ArrayList<String>();
        mCirclesAdapter = new ArrayAdapter<String>(
                this, R.layout.circle_member, mCirclesList);

        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }



        mGoogleApiClient = buildGoogleApiClient();

        Intent intent = getIntent();

        signout = intent.getBooleanExtra("signout", false);



    }

    private GoogleApiClient buildGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            switch (v.getId()) {
                case R.id.myGoogleSignIn:
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                    break;
                }
        }
    }

    public class LoginTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String sessionID =null;
            Boolean flag = true;
            while(flag){
                if (mGoogleApiClient.isConnected()) {
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    myUser.setUserName(getProfileInformation().getEmail());
                    sessionID = client.clientLoginHandler(myUser);
                    if(sessionID.equals(null)){
                        System.out.println(" Login Failure! ");

                    }
                    else {
                        myUser.setSessionId(sessionID);
                    }

                    flag = false;
                }
                else {
                    flag = true;
                }
            }
            return sessionID;
        }

        @Override
        protected void onPostExecute(String result) {
            if (signout == false) {

                Intent myIntent = new Intent(SignInActivity.this, MainActivity.class);
                ProfileInformation profileInformation = getProfileInformation();
                myUser.setName(profileInformation.getName());
                myUser.setImageUrl(profileInformation.getImageUrl());

                myIntent.putExtra("UserInfo", (Serializable) myUser);
                startActivity(myIntent);


            }



        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if(signout == true){
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            signout = false;
        }

        else {
            // Reaching onConnected means we consider the user signed in.
            Log.i(TAG, "onConnected");
            Plus.PeopleApi.loadConnected(mGoogleApiClient).setResultCallback(this);
            new LoginTask().execute();



        }

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.GONE);

        } else {
            mSignInButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {

            Log.w(TAG, "API Unavailable.");
        } else if (mSignInProgress != STATE_IN_PROGRESS) {

            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {

                resolveSignInError();
            }
        }
        updateUI(false);
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {

            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {

            createErrorDialog().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }



    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    private Dialog createErrorDialog() {
        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    this,
                    RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e(TAG, "Google Play services resolution cancelled");
                            mSignInProgress = STATE_DEFAULT;
                        }
                    });
        } else {
            return new AlertDialog.Builder(this)
                    .setMessage(R.string.play_services_error)
                    .setPositiveButton(R.string.close,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError);
                                    mSignInProgress = STATE_DEFAULT;
                                }
                            }).create();
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        switch (peopleData.getStatus().getStatusCode()) {
            case CommonStatusCodes.SUCCESS:
                mCirclesList.clear();
                PersonBuffer personBuffer = peopleData.getPersonBuffer();
                try {
                    int count = personBuffer.getCount();
                    if (count == 0) {
                        mCirclesList.add("Your Friends are not Using this App :(");
                    }
                    else {
                        for (int i = 0; i < count; i++) {
                            mCirclesList.add(personBuffer.get(i).getDisplayName());

                            //mCirclesList.add(personBuffer.get(i).getImage().getUrl());
                        }
                        myUser.setFriendsList(mCirclesList);
                        friends_flag =true;
                    }
                } finally {
                    personBuffer.close();
                }

                mCirclesAdapter.notifyDataSetChanged();

                break;

            case CommonStatusCodes.SIGN_IN_REQUIRED:
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                break;

            default:
                Log.e(TAG, "Error when listing people: " + peopleData.getStatus());
                break;
        }

    }

    private ProfileInformation getProfileInformation() {
        ProfileInformation profile = new ProfileInformation();

        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                profile.setName(personName);
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;
                profile.setImageUrl(personPhotoUrl);

                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                profile.setEmail(email);




            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profile;
    }


}


