package pranaykumar.trainingdatacreation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvKNearest;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class CreateTrainData extends ActionBarActivity {

    private static final String TAG = "In Digit_Recognition Activity ::";
    private EditText reply;
   private Button transmitButton;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_train_data);


        reply = (EditText) findViewById(R.id.editText);


// add status update for server connection
         transmitButton = (Button) findViewById(R.id.Transmit);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Mat trainData = new Mat();
                Mat response_array = new Mat();
                Mat img;
                Mat imgResized;

                for (int i = 1; i < 10; i++) {
                    for (int j = 1; j < 9; j++) {


                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                              //  "/train/img" +String.format( "%03d", i) +"-"+String.format( "%03d", j)+".png";
                                "/num_" + i + j+".jpg";
                        Log.i(TAG, path);
                        img = Highgui.imread(path);
                        img.convertTo(img, CvType.CV_32FC1);

                        if (img.empty()) //check whether the image is loaded or not
                        {
                            Log.i(TAG,"img is empty ");
                        }
                        else {
                            Log.i(TAG,"img is not empty ");
                            Size dsize = new Size(50, 50);
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

                CvKNearest knn = new CvKNearest();
                knn.train(trainData, response);

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() ;

                Log.i(TAG,"org train width "+trainData.cols()+"org train Height"+trainData.rows());
                Log.i(TAG,"org response width "+response.cols()+"org response Height"+response.rows());

                Bitmap bmpOut1 = Bitmap.createBitmap(trainData.cols(), trainData.rows(), Bitmap.Config.ARGB_8888);
                Bitmap bmpOut2 = Bitmap.createBitmap(response.cols(), response.rows(), Bitmap.Config.ARGB_8888);

                response.convertTo(response,CvType.CV_8UC1);
                trainData.convertTo(trainData,CvType.CV_8UC1);

                Utils.matToBitmap(trainData, bmpOut1);
                Utils.matToBitmap(response, bmpOut2);
                //File file = new File(path);
               // file.mkdirs();
                File file = new File(path, "train.jpg");
                File file2 = new File(path, "response.jpg");

                OutputStream fout = null;
                OutputStream fout2 = null;

                try {
                    fout = new FileOutputStream(file);
                    fout2 = new FileOutputStream(file2);

                    BufferedOutputStream bos = new BufferedOutputStream(fout);
                    BufferedOutputStream bos2 = new BufferedOutputStream(fout2);

                    bmpOut1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    bmpOut1.recycle();

                    bmpOut2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
                    bos2.flush();
                    bos2.close();
                    bmpOut2.recycle();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }




                //Resources res = getResources();
                // Drawable drawable = res.getDrawable(R.drawable.train);
                //Bitmap blankBitmap=((BitmapDrawable)drawable).getBitmap();

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap blankBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.train,o);
                int trainWidth = blankBitmap.getWidth();
                int trainHeight = blankBitmap.getHeight();
                //Target dimensions
                // int iW = 300;
                //int iH = 200;
                // mOriginalBitmap = Bitmap.createScaledBitmap(mOriginalBitmap, iW, iH, true);

                Log.i(TAG,"train width "+trainWidth +" train Height"+trainHeight);

                Mat trainData2 = new Mat();
                Utils.bitmapToMat(blankBitmap,trainData2);
                Log.i(TAG,"trainData2 width "+ trainData2.cols() +" trainData2 Height"+ trainData2.rows());

                //Drawable drawable2 = res.getDrawable(R.drawable.response);
                //Bitmap blankBitmap2=((BitmapDrawable)drawable2).getBitmap();
                Bitmap blankBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.response,o);
                int resWidth = blankBitmap2.getWidth();
                int resHeight = blankBitmap2.getHeight();

                Log.i(TAG,"res width "+ resWidth +" res Height"+ resHeight);
                Mat response2 = new Mat();
                Utils.bitmapToMat(blankBitmap2,response2);

                Log.i(TAG,"response2 width "+ response2.cols() +" res Height"+ response2.rows());

                Mat response3 = new Mat();
                Mat trainData3 = new Mat();

                Imgproc.cvtColor(response2,response2,Imgproc.COLOR_BGRA2GRAY);       // 1. change the number of channels
                Imgproc.cvtColor(trainData2,trainData2,Imgproc.COLOR_BGRA2GRAY);
                response2.convertTo(response3,CvType.CV_32FC1);
                trainData2.convertTo(trainData3,CvType.CV_32FC1);

                Log.i(TAG,"trainData2 width "+ trainData2.cols() +" trainData2 Height"+ trainData2.rows());
                Log.i(TAG,"response2 width "+ response2.cols() +" res Height"+ response2.rows());

               // response2 = response2.reshape(1, 1);
               // trainData2 = trainData2.reshape(1, 1);
                Log.i(TAG,"trainData3 type "+ trainData3.type() +" trainData2 Height"+ trainData3.rows());
                Log.i(TAG,"response2 width "+ response3.cols() +" res Height"+ response3.rows());

                CvKNearest knn2 = new CvKNearest();
                knn2.train(trainData3, response3);

                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                        "/num_22.jpg";

                img = Highgui.imread(path);
                Mat test = new Mat();
                Imgproc.resize(img,test, new Size(50,50) );
                test.convertTo(test, CvType.CV_32FC1);

                Mat results = new Mat();
                Mat responses = new Mat();
                Mat dists = new Mat();

                float numberDetected = knn.find_nearest(test.reshape(1,1), 1,results,responses,dists);
                reply.setText("number dtected is "+ numberDetected );

            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_train_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


/*
    public void knntrainingfromtrainingimages () {

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
*/