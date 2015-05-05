package com.ecen489.googlesignin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecen489.slidermenu.DigRec_CameraView;
import com.ecen489.slidermenu.MainActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.ml.CvKNearest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

//import org.opencv.core.Size;

public class Digit_Recognition extends Activity implements CvCameraViewListener2, OnTouchListener {
    private static final String TAG = "Dig_Rec";

    private DigRec_CameraView mOpenCvCameraView;
    private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;

    private int count;
    private  int dgimgnum ;
    int inimgwidth;
    int inimgheight;

    private Mat Gray_in_im;
    private Mat adpthr_image;
    Mat Mor_Close_im;
    Mat dilate_im;

    private static CvKNearest knn;
    static StringBuilder s = new StringBuilder(100);
    static String loc;
    static int train_count;
    static int cent_count;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(Digit_Recognition.this);

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Digit_Recognition() {
        //Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "called onCreate");
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    setContentView(R.layout.activity_dig_rec);

    mOpenCvCameraView = (DigRec_CameraView) findViewById(R.id.digRecActivity_java_surface_view);

    mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

    mOpenCvCameraView.setCvCameraViewListener(this);

    train_count=0;

}

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        count =0;
        s.setLength(0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        count =0;
        s.setLength(0);

    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

    }

    public void onCameraViewStarted(int width, int height) {
        Gray_in_im = new Mat(height, width, CvType.CV_8UC4);
        adpthr_image = new Mat(height, width, CvType.CV_8UC4);
        inimgwidth=width;
        inimgheight=height;
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        Gray_in_im = inputFrame.gray();
        return Gray_in_im;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<String> effects = mOpenCvCameraView.getEffectList();

        if (effects == null) {
            Log.e(TAG, "Color effects are not supported by device!");
            return true;
        }

        mColorEffectsMenu = menu.addSubMenu("Color Effect");
        mEffectMenuItems = new MenuItem[effects.size()];

        int idx = 0;
        ListIterator<String> effectItr = effects.listIterator();
        while(effectItr.hasNext()) {
            String element = effectItr.next();
            mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, element);
            idx++;
        }

        mResolutionMenu = menu.addSubMenu("Resolution");
        mResolutionList = mOpenCvCameraView.getResolutionList();
        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        idx = 0;
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
            idx++;
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item.getGroupId() == 1)
        {
            mOpenCvCameraView.setEffect((String) item.getTitle());
            Toast.makeText(this, mOpenCvCameraView.getEffect(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2)
        {
            int id = item.getItemId();
            Size resolution = mResolutionList.get(id);
            mOpenCvCameraView.setResolution(resolution);
            resolution = mOpenCvCameraView.getResolution();
            String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
            Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.i(TAG,"onTouch event");

        // inserted code


        if (count == 0) {
            Log.i(TAG,"entered if loop");

            // Image Preprocessing

                // Step-1 : Get Binary Image

                        //getBinaryImage(Gray_in_im);

                        //adaptiveThreshold
                        int maxValue = 255;
                        int blockSize = 11;
                        int meanOffset = 3;
                        Imgproc.adaptiveThreshold(Gray_in_im, adpthr_image, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                Imgproc.THRESH_BINARY_INV, blockSize, meanOffset);

                        //simple Threshold
                        //   Imgproc.threshold(input_img, bin_image, 85, 255, Imgproc.THRESH_BINARY_INV);  //76 red Green 150 blue 29

                        // Inrange
                        //Core.inRange(input_img, new Scalar(50), new Scalar(86), bin_image);//76 red

                // Step-2: train knn
/*
                        //knntraining();
                        if (train_count==0) {
                            train_count+=1;

                            Mat trainData = new Mat();
                            Mat response_array = new Mat();
                            Mat img;
                            Mat imgResized;

                            for (int i = 0; i < 10; i++) {
                                for (int j = 1; j < 3; j++) {
                                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                                            "/num_" + i + j + ".jpg";


                                    img = Highgui.imread(path);
                                    img.convertTo(img, CvType.CV_32FC1);

                                    if (img.empty()) //check whether the image is loaded or not
                                    {
                                        Log.i(TAG, "img is empty ");
                                    } else {
                                        Log.i(TAG, "img is not empty ");
                                        org.opencv.core.Size dsize = new org.opencv.core.Size(50, 50);
                                        Imgproc.resize(img, img, dsize);

                                        img.convertTo(img, CvType.CV_32FC1);

                                        imgResized = img.reshape(1, 1);
                                        trainData.push_back(imgResized);

                                        response_array.push_back(new Mat(1, 1, CvType.CV_32FC1, new Scalar(i))); // Store label to a mat
                                    }
                                }
                            }

                            Mat response = new Mat();
                            Mat tmp;
                            tmp = response_array.reshape(1, 1); //make continuous
                            tmp.convertTo(response, CvType.CV_32FC1); // Convert  to float

                            knn = new CvKNearest();
                            knn.train(trainData, response);
                        }
*/
                // Step-2 : Remove small objects

                        //Mat processed_image = new Mat();
                        //processed_image = applyMorphologyOperation(adpthr_image);
                        //applyMorphologyOperation();


                        // Perform Morphology close

                        //get kernel
                        org.opencv.core.Size kernelsize =new org.opencv.core.Size(7,7); // 7,7 is optimum (if less thin digits) (if more than 7 more dots)
                        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, kernelsize);// these values doesn't depend on color

                        // apply Morphology close
                        Mor_Close_im = new Mat();
                        Imgproc.morphologyEx(adpthr_image, Mor_Close_im, Imgproc.MORPH_CLOSE, kernel);

                        // Perform Morphology open

                        //get kernel
                        org.opencv.core.Size dil_kernelsize =new org.opencv.core.Size(3,3);  //5,5 removes complete digit so 3,3 is optimum for opening
                        Mat dil_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, dil_kernelsize);

                        dilate_im = new Mat();
                        //dilate_im = Mor_Close_im;
                        Imgproc.morphologyEx( Mor_Close_im, dilate_im, Imgproc.MORPH_OPEN, dil_kernel);

                        // Perform Dilate
                        //Imgproc.dilate( Mor_Close_im,dilate_im, dil_kernel);

                // Step-3 : Find Contours of digits

                       // List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                       // Mat hierarchy = new Mat();
                       // contours = getContours();

                        // find contours
                        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                        Mat hierarchy = new Mat();
                        // Imgproc.findContours(dilate_im, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                        Imgproc.findContours(dilate_im, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


            // Step-4 : filter contours by their position

                        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
                        List<Moments> mu = new ArrayList<Moments>(contours.size());
                        int[][] centroidx = new int[contours.size()][2];
                         cent_count =0;
                        for (int i = 0; i < contours.size(); i++) {
                            mu.add(i, Imgproc.moments(contours.get(i), false));
                            Moments p = mu.get(i);
                            int x = (int) (p.get_m10() / p.get_m00());
                            int y = (int) (p.get_m01() / p.get_m00());
                             /*  if((x>inimgwidth/4)&&(x<(3*inimgwidth)/4)) {
                                   mContours.add(contours.get(i));
                               }
                             */
                            if((y>inimgheight/4)&&(y<(3*inimgheight)/4)) {
                                mContours.add(contours.get(i));
                                centroidx[cent_count][1]=x;
                                centroidx[cent_count][0]=cent_count;
                                cent_count = cent_count+1;
                            }
                        }

                        Arrays.sort(centroidx,
                                             new java.util.Comparator<int[]>(){
                                                 @Override
                                                 public int compare(int[]a,int[]b){
                                                     Integer numOfKeys1 = a[1];
                                                     Integer numOfKeys2 = b[1];
                                                     return numOfKeys1.compareTo(numOfKeys2);
                                                     // return a[1]-b[1];
                                                     //return Integer.compare(a[1], b[1]);
                                                             }
                                            });


            // Step-5 : get bounding Rectangle and crop each digit

                        // Approximate contours to polygons + get bounding rects
                        MatOfPoint2f approxCurve = new MatOfPoint2f();

                            for( int i = 0; i < mContours.size(); i++ )
                            {
                                // if its area is greater than 150
                                double area = Imgproc.contourArea(mContours.get(i));
                                if ((area > 500) ) {

                                    //Log.e(TAG, "sorted array :: " + centroidx[i][1]);
                                    //Convert contours(i) from MatOfPoint to MatOfPoint2f
                                    MatOfPoint2f contour2f = new MatOfPoint2f( mContours.get(i).toArray() );

                                    //Processing on mMOP2f1 which is in type MatOfPoint2f
                                    double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
                                    Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

                                    //Convert back to MatOfPoint
                                    MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

                                    // Get bounding rect of contour
                                    Rect rect = Imgproc.boundingRect(points);

                                        /*
                                           Log.i(TAG,"assigning submat ");
                                           Mat ROI2 = Mor_Close_im.submat(rect.x,rect.x+rect.width,rect.y,rect.y+rect.height);

                                            Log.i(TAG,"created zero Mat of source type");
                                            Mat  zr = new Mat( Mat.zeros(inimgwidth, inimgheight,  Mor_Close_im.type()),rect);

                                            Log.i(TAG,"assigning zero");
                                            //ROI2.setTo(zr);
                                            zr.copyTo(ROI2);

                                            Log.i(TAG,"assigned zero");
                                        */

                                    //Crop the image
                                    Mat ROI = new Mat(Mor_Close_im,rect);
                                    org.opencv.core.Size dsize = new org.opencv.core.Size(50,50);
                                    Imgproc.resize(ROI, ROI, dsize);

                                    // convert to float
                                    ROI.convertTo(ROI, CvType.CV_32FC1);

                                    // save cropped image
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                                    String currentDateandTime2 = sdf2.format(new Date());
/*
                                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                                            "/digit_recognition";
                                    File file = new File(path);
                                    file.mkdirs();

                                    String cropimgName = path +"/trainingnumber_" + currentDateandTime2 +"_"+ dgimgnum + ".jpg";
                                    Highgui.imwrite(cropimgName, ROI);

                                    // knn testing

                                    //float numdetected =  knntesting(ROI);
                                    Mat img2 = Highgui.imread(cropimgName);
                                    //Mat img2 = new Mat();
                                    //img2 = ROI;
                                    Mat test = new Mat();

                                    Imgproc.resize(img2,test, new org.opencv.core.Size(50,50) );
                                    test.convertTo(test, CvType.CV_32FC1);
                                    Mat results = new Mat();
                                    Mat responses = new Mat();
                                    Mat dists = new Mat();

                                    float numdetected = knn.find_nearest(test.reshape(1,1), 1,results,responses,dists);
                                    s.append((int)numdetected);

*/

                                     dgimgnum = dgimgnum+1;

                                    // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                                    Core.rectangle(Mor_Close_im, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 3);
                                    // Core.rectangle(Mor_Close_im, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar( 0, 0, 255),2);
                                }


                             }

                            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                                    "/digit_recognition");
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(dir, children[i]).delete();
                                }
                            }

                // Step 5 : Save the processed image

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                        String currentDateandTime = sdf.format(new Date());

                        // String fileName = Environment.getExternalStorageDirectory().getPath() +
                        String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                                "/digit_training_" + currentDateandTime + ".jpg";

                        Log.i(TAG,"calling take picture");
                        mOpenCvCameraView.takePicture(fileName);

                        Highgui.imwrite(fileName,Mor_Close_im);


                        //Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(this, "Checked In at location :: "+ s , Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Checked In at location :: "+ dgimgnum , Toast.LENGTH_SHORT).show();

                        // disable cameraview
                        count = 1;
                        mOpenCvCameraView.disableView();

                // Step 6: display the processed image

                        displayimageonscreen(Mor_Close_im);

                // step 7: display alert message to take another picture;

                        displayalertdialog();


        }
        //
        return false;
    }

    // functions that used for each step

    public void getBinaryImage(Mat input_img) {

        // Step-1 : Get Binary Image

                //adaptiveThreshold

                int maxValue = 255;
                int blockSize = 11;
                int meanOffset = 3;
                Mat bin_image = new Mat();

                Imgproc.adaptiveThreshold(input_img, adpthr_image, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                        Imgproc.THRESH_BINARY_INV, blockSize, meanOffset);

                //simple Threshold
                //   Imgproc.threshold(input_img, bin_image, 85, 255, Imgproc.THRESH_BINARY_INV);  //76 red Green 150 blue 29

                // Inrange
                //Core.inRange(input_img, new Scalar(50), new Scalar(86), bin_image);//76 red

     //   return bin_image;

    }

    public void knntraining () {

        Mat trainData = new Mat();
        Mat response_array = new Mat();
        Mat img ;
        Mat imgResized;

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 2; j++) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                        "/num_" + i + j+".jpg";

                img = Highgui.imread(path);
                img.convertTo(img, CvType.CV_32FC1);

                if (img.empty()) //check whether the image is loaded or not
                {
                    Log.i(TAG,"img is empty ");
                }
                else {
                    Log.i(TAG,"img is not empty ");
                    org.opencv.core.Size dsize = new org.opencv.core.Size(50, 50);
                    Imgproc.resize(img, img, dsize);

                    img.convertTo(img, CvType.CV_32FC1);

                    imgResized = img.reshape(1, 1);
                    trainData.push_back(imgResized);

                    response_array.push_back(new Mat (1,1,CvType.CV_32FC1,new Scalar(i))); // Store label to a mat
                }
            }
        }

        Mat response=new Mat();
        Mat tmp;
        tmp=response_array.reshape(1,1); //make continuous
        tmp.convertTo(response,CvType.CV_32FC1); // Convert  to float

        knn =  new CvKNearest();
        knn.train(trainData, response);
    }

    public float knntesting (Mat im) {

        // resize the image to 50,50
        org.opencv.core.Size dsize = new org.opencv.core.Size(50,50);
        Mat im2 = new Mat();
        Imgproc.resize(im, im2, dsize);

        // convert to float

        im.convertTo(im2, CvType.CV_32FC1);

        Mat results = new Mat();
        Mat responses = new Mat();
        Mat dists = new Mat();

        float numberDetected = knn.find_nearest(im2.reshape(1,1), 1,results,responses,dists);

        return numberDetected;
    }

    public void applyMorphologyOperation() {

        // Step-2 : Remove small objects

            // Perform Morphology close

                    //get kernel
                    org.opencv.core.Size kernelsize =new org.opencv.core.Size(7,7); // 7,7 is optimum (if less thin digits) (if more than 7 more dots)
                    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, kernelsize);// these values doesn't depend on color

                    // apply Morphology close
                     Mor_Close_im = new Mat();
                    Imgproc.morphologyEx(adpthr_image, Mor_Close_im, Imgproc.MORPH_CLOSE, kernel);

            // Perform Morphology open

                    //get kernel
                    org.opencv.core.Size dil_kernelsize =new org.opencv.core.Size(3,3);  //5,5 removes complete digit so 3,3 is optimum for opening
                    Mat dil_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, dil_kernelsize);

                     dilate_im = new Mat();
                    //dilate_im = Mor_Close_im;
                    Imgproc.morphologyEx( Mor_Close_im, dilate_im, Imgproc.MORPH_OPEN, dil_kernel);

            // Perform Dilate
                     //Imgproc.dilate( Mor_Close_im,dilate_im, dil_kernel);

        //return dilate_im;
        //return Mor_Close_im;
    }
    public List<MatOfPoint> getContours() {

        // Step-3 : Find Contours of digits

            // find contours
                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                Mat hierarchy = new Mat();
                // Imgproc.findContours(dilate_im, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                Imgproc.findContours(Mor_Close_im, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;

    }

    public void displayimageonscreen (Mat im) {

        // Step 6: display the processed image


        Bitmap bm = Bitmap.createBitmap(im.cols(), im.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(im, bm);

        setContentView(R.layout.activity_imageview);
        // find the imageview and draw it!
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageBitmap(bm);
    }

    public void displayalertdialog() {

        // Step-6
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Do you want to retake the picture");
        alertDialog.setMessage("detected number :: "+ dgimgnum);

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                            setContentView(R.layout.activity_dig_rec);
                            mOpenCvCameraView = (DigRec_CameraView) findViewById(R.id.digRecActivity_java_surface_view);
                            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
                            mOpenCvCameraView.setCvCameraViewListener(Digit_Recognition.this);

                            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, Digit_Recognition.this, mLoaderCallback);

                            count = 0;
                            s.setLength(0);
                            }
              });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    count = 0;
                    s.append(dgimgnum);

                   /* if (s.equals("0")){loc = "EIC";}
                    else if (s.equals("1")){loc = "EIC";}
                    else if (s.equals("2")){loc = "EIC";}
                    else if (s.equals("3")){loc = "EIC";}
                    else if (s.equals("4")){loc = "EIC";}
                    else if (s.equals("5")){loc = "EIC";}
                    else if (s.equals("6")){loc = "EIC";}
                    else if (s.equals("7")){loc = "EIC";}
                    else if (s.equals("8")){loc = "EIC";}
                    else if (s.equals("9")){loc = "EIC";}
                    else {loc = "home";}
*/
                    if (dgimgnum==0){loc = "EIC0";}
                    else if (dgimgnum==1){loc = "EIC1";}
                    else if (dgimgnum==2){loc = "EIC2";}
                    else if (dgimgnum==3){loc = "EIC3";}
                    else if (dgimgnum==4){loc = "EIC4";}
                    else if (dgimgnum==5){loc = "EIC5";}

                    else {loc = "home";}


                    s.setLength(0);
                    dgimgnum=0;
                    Intent i = new Intent(Digit_Recognition.this, MainActivity.class);
                    i.putExtra("setCheckInButton", true);
                    i.putExtra("loc", loc);
                    startActivity(i);

                }
            });

        alertDialog.show();

    }
}

