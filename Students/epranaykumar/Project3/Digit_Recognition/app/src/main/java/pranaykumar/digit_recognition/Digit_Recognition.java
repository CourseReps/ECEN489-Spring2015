package pranaykumar.digit_recognition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
//import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

public class Digit_Recognition extends Activity implements CvCameraViewListener2, OnTouchListener {
    private static final String TAG = "OCVSample::Activity";

    private DigRec_CameraView mOpenCvCameraView;
    private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;

    private int count,digitnum;
    private int dgimgnum ;
    private Mat Gray_in_im;
    private Mat adpthr_image;
    int inimgwidth;
    int inimgheight;
    int numofdigits;

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


}

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        count =0;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        count =0;

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
        digitnum=digitnum+1;

        if (count == 0) {
            Log.i(TAG,"entered if loop");

            // Image Preprocessing

            // Step-1 : Get Binary Image

            //adaptiveThreshold

            int maxValue = 255;
            int blockSize = 11;
            int meanOffset = 3;
            Imgproc.adaptiveThreshold(Gray_in_im, adpthr_image, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                    Imgproc.THRESH_BINARY_INV, blockSize, meanOffset);



            //simple Threshold
            //   Imgproc.threshold(Gray_in_im, adpthr_image, 85, 255, Imgproc.THRESH_BINARY_INV);  //76 red Green 150 blue 29

            // Inrange
            //Core.inRange(Gray_in_im, new Scalar(50), new Scalar(86), adpthr_image);//76 red

            // Step-2 : Remove small objects

            // Perform Morphology close
            org.opencv.core.Size kernelsize =new org.opencv.core.Size(7,7); // 7,7 is optimum (if less thin digits) (if more than 7 more dots)
            // these values doesn't depend on color
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, kernelsize);

            Mat Mor_Close_im = new Mat();
            Imgproc.morphologyEx(adpthr_image, Mor_Close_im, Imgproc.MORPH_CLOSE, kernel);

            // Perform Morphology open
            org.opencv.core.Size dil_kernelsize =new org.opencv.core.Size(3,3);  //5,5 removes complete digit so 3,3 is optimum for opening
            Mat dil_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, dil_kernelsize);
            Mat dilate_im = new Mat();  //Mat dilate_im = Mor_Close_im;

            Imgproc.morphologyEx( Mor_Close_im, dilate_im, Imgproc.MORPH_OPEN, dil_kernel);

            // Perform Dilate
            // Imgproc.dilate( Mor_Close_im,dilate_im, dil_kernel);

            // Step-2 : Find Contours of digits

            // find contours
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(dilate_im, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Approximate contours to polygons + get bounding rects

            numofdigits=0;

            MatOfPoint2f approxCurve = new MatOfPoint2f();
            for( int i = 0; i < contours.size(); i++ )
            {
                // if its area is greater than 150
                double area = Imgproc.contourArea(contours.get(i));
                if ((area > 700) ) {


                    numofdigits=+1;

                    //Convert contours(i) from MatOfPoint to MatOfPoint2f
                    MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );

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

                    // resize the image to 50,50
                    org.opencv.core.Size dsize = new org.opencv.core.Size(50,50);
                    Imgproc.resize(ROI, ROI, dsize);

                    // convert to float
                    ROI.convertTo(ROI, CvType.CV_32FC1);

                    // write cropped image
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                    String currentDateandTime2 = sdf2.format(new Date());

                    String cropimgName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                            "/trainingnumber_" + currentDateandTime2 +"_"+ dgimgnum + ".jpg";

                    dgimgnum = dgimgnum+1;
                    Highgui.imwrite(cropimgName, ROI);

                    // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                    Core.rectangle(Mor_Close_im, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 3);
                    // Core.rectangle(Mor_Close_im, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar( 0, 0, 255),2);
                }


            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateandTime = sdf.format(new Date());


            // String fileName = Environment.getExternalStorageDirectory().getPath() +
            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                    "/digit_training_" + currentDateandTime + ".jpg";

            Log.i(TAG,"calling take picture");
            mOpenCvCameraView.takePicture(fileName);

            Highgui.imwrite(fileName,Mor_Close_im);


            Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();

            // inserted code
            count = 1;
            mOpenCvCameraView.disableView();
        }
        //
        return false;
    }
}

