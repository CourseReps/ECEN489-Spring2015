package com.ecen489.googlesignin;

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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SignIn extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener,
        ResultCallback<LoadPeopleResult>, View.OnClickListener {

    String location;
   static Boolean setCheckInButton = false;
    // Check In Client
    private CheckInClient client = new CheckInClient();
    private User user = new User();

    private static final String TAG = "Google-Sign-In";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    private GoogleApiClient mGoogleApiClient;

    private int mSignInProgress;

    private PendingIntent mSignInIntent;

    private int mSignInError;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private Button mAddFriends;
    private Button mFriendsLocations;
    private Button mCheckInLocation;
    private TextView mStatus;
    private ListView mCirclesListView;
    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;

    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    private TextView mCirclesTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mAddFriends = (Button) findViewById(R.id.addFriend);
        mCheckInLocation = (Button) findViewById(R.id.checkInLocation);
        mFriendsLocations = (Button) findViewById(R.id.recentLocations);

        mStatus = (TextView) findViewById(R.id.sign_in_status);
        mCirclesTitle = (TextView) findViewById(R.id.circles_title);
        mCirclesListView = (ListView) findViewById(R.id.circles_list);

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        // Button listeners
        mAddFriends.setOnClickListener(this);
        mCheckInLocation.setOnClickListener(this);
        mFriendsLocations.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);


        mCirclesList = new ArrayList<String>();
        mCirclesAdapter = new ArrayAdapter<String>(
                this, R.layout.circle_member, mCirclesList);
        mCirclesListView.setAdapter(mCirclesAdapter);


        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClient();
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
        mStatus.setText("Status: Signed out");
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
                case R.id.sign_in_button:
                    //mStatus.setText("Status: Signing In to Google+...");
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                    new LoginTask().execute();
                    break;
                case R.id.sign_out_button:
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        //client.clientLogoutHandler(user);
                        mStatus.setText("Status: Signed out");
                    }
                    updateUI(false);
                    break;
                case R.id.revoke_access_button:
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    mStatus.setText("Status: Signed out");
                    updateUI(false);
                    break;
                case R.id.addFriend:
                    Intent myIntent = new Intent(SignIn.this, FriendActivity.class);
                    startActivity(myIntent);

                    break;
                case R.id.checkInLocation:
                    Intent cVIntent = new Intent(SignIn.this, Digit_Recognition.class);
                    setCheckInButton = true;
                    startActivity(cVIntent);
                    // New Activity for OpenCV
                    break;
                case R.id.recentLocations:
                    break;
            }
        }
    }


    public class LoginTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String response ="";
            Boolean flag = true;
            while(flag){
                if (mGoogleApiClient.isConnected()) {
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    user.setUserName(email);
                    response = client.clientLoginHandler(user);
                    flag = false;
                }
                else {
                    flag = true;
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            if (result.equals("success")){

                mStatus.setText("Login Successful, SessionID: " + user.getSessionId());
            }
            else {
                mStatus.setText("LoginFailure: Session Closed");
                mStatus.setText(result);
                mAddFriends.setEnabled(false);
                mCheckInLocation.setEnabled(false);
                mFriendsLocations.setEnabled(false);
            }
        }
    }




    @Override
    public void onConnected(Bundle connectionHint) {

        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");
        //mStatus.setText("Status: Signed In");

        // Get user's information
        getProfileInformation();

        Plus.PeopleApi.loadConnected(mGoogleApiClient).setResultCallback(this);

        //ArrayList<String> addFriendsToDatabase = mCirclesList;
        if (setCheckInButton) {
            Intent intent = getIntent();
            location = (intent.getStringExtra(Digit_Recognition.location));

            Log.i(TAG, "Inside OnConnect");
            mStatus.setText(location);
            setCheckInButton = false;
        }
        updateUI(true);


        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;

    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.VISIBLE);
            mCheckInLocation.setVisibility(View.VISIBLE);
            mAddFriends.setVisibility(View.VISIBLE);
            mFriendsLocations.setVisibility(View.VISIBLE);
            mCirclesTitle.setVisibility(View.VISIBLE);
            mCirclesListView.setVisibility(View.VISIBLE);
            mAddFriends.setVisibility(View.VISIBLE);
        } else {
            mSignInButton.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.GONE);
            mCheckInLocation.setVisibility(View.GONE);
            mAddFriends.setVisibility(View.GONE);
            mFriendsLocations.setVisibility(View.GONE);
            mCirclesTitle.setVisibility(View.GONE);
            mCirclesListView.setVisibility(View.GONE);
            mCirclesList.clear();
            mCirclesAdapter.notifyDataSetChanged();
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
                            mStatus.setText("Status: " + R.string.status_signed_out);
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
                                    mStatus.setText("Status: " +R.string.status_signed_out);
                                }
                            }).create();
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
