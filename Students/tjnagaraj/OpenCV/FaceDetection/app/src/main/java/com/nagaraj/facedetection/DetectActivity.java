package com.nagaraj.facedetection;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Subdiv2D;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.photo.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class DetectActivity extends Activity
        implements CameraBridgeViewBase.CvCameraViewListener {

    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayImage;
    private int absoluteFaceSize;
    public static boolean flag = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    // Function that bridges the OpenCV library and the FaceDetection app.
    private void initializeOpenCVDependencies() {

        try {
            // Defining the path to XML file where the features are stored
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            // Loading the cascade classifier to detect faces
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("DetectActivity", "Error loading cascade", e);
        }
        // Creating the view
        openCvCameraView.enableView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
       new ConnectToServer().execute();
        super.onCreate(savedInstanceState);
        // Prevent the screen from locking when the application is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        openCvCameraView = new JavaCameraView(this, -1);
        openCvCameraView.setCameraIndex(1);
        setContentView(openCvCameraView);
        openCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

        grayImage = new Mat(height, width, CvType.CV_8UC4);

        // Normalizing the face size to 20% of the screen height
        absoluteFaceSize = (int) (height*0.2);
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        // Create a grayscale image
        Imgproc.cvtColor(aInputFrame, grayImage, Imgproc.COLOR_RGBA2RGB);
        // Initializing the faces array where the faces will be  stored
        MatOfRect faces = new MatOfRect();
        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // Draw a rectangle around the faces, if found any
        Rect[] facesArray = faces.toArray();
        Rect rectCrop;
        for (int i = 0; i <facesArray.length; i++) {
            Core.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
            rectCrop = new Rect(facesArray[i].x, facesArray[i].y, facesArray[i].width, facesArray[i].height);
            Mat image_roi = new Mat(aInputFrame,rectCrop);
            if(flag==true) {
                if (facesArray.length == 1) {
                    Highgui.imwrite(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cropimage_" + i + ".png", image_roi);
                    // Send the JSON command with the Devicename_Timestamp
                    flag = false;
                }
                else{
                    //Send the JSON command to the server with Device name NULL
                }
            }
        }

        return aInputFrame;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }

    /*public void callFacedetection(){
        Intent intent = new Intent(this, DetectActivity.class);
        startActivity(intent);
    }*/

    public class ConnectToServer extends AsyncTask<Void,Void,Void> {
        private String serverIP="10.202.124.204";
        private  int port=9000;
        String text = null;
        String devID = null;

    /*connectToServer (String ip, int port) {
        this.serverIp = ip;
        this.port = port;   //this.messsage = message;
    }*/

        public Void doInBackground(Void... params) {
            //Socket socket;
//        try {
//
//
////            connected = socket.isConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

            try {
                Socket socket = new Socket(serverIP, port);  //connect to serverd
                InputStreamReader readCommand = new InputStreamReader(socket.getInputStream());
                JsonReader reader = new JsonReader(readCommand);
                reader.beginObject();

                while(reader.hasNext()){
                    String name = reader.nextName();
                    if (name.equals("Command")) {
                        text = reader.nextString();
    //                    System.out.println(text);
                    }
                    else if(name.equals("DeviceID")){
                        devID = reader.nextString();
  //                      System.out.println(devID);
                    }
                    else {
                        reader.skipValue();
                    }
                    //   System.out.println(name);
                }
                reader.endObject();

                if(text.equals("take picture")){
                    flag = true;
//                    System.out.println(text + "\n" + devID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}