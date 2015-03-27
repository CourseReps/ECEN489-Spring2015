//
//  main.cpp
//  Opencv
//
//  Created by Tungala Naga Raghavendra Anudeep on 3/8/15.
//  Copyright (c) 2015 Tungala Naga Raghavendra Anudeep. All rights reserved.
//

#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv/cvaux.h>

using namespace cv;
using namespace std;

string face_cascade_name = "/Users/tungalanagaraghavendraanudeep/Documents/opencv-2.4.10/data/haarcascades/haarcascade_frontalface_alt.xml";
CascadeClassifier face_cascade;

bool facedetect(Mat frame,String ts) {
    bool flag = false;
    Mat grayFrame;
    Mat crop;
    Mat res;
    Mat gray;
    vector<Rect> faces;
    Rect box;
    int filenumber = 1; // Number of file to be saved
    string filename;
    
    cvtColor(frame, grayFrame, COLOR_BGR2GRAY);
    equalizeHist(grayFrame, grayFrame);
    
    face_cascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));
    
    size_t itr = 0;
    
    for (itr=0; itr<faces.size(); itr++) {
        Point pt1(faces[itr].x, faces[itr].y); // Display detected faces on main window - live stream from camera
        Point pt2((faces[itr].x + faces[itr].height), (faces[itr].y + faces[itr].width));
        rectangle(frame, pt1, pt2, Scalar(0, 255, 0), 2, 8, 0);
        crop = frame(faces[itr]);
        resize(crop, res, Size(128, 128), 0, 0, INTER_LINEAR); // This will be needed later while saving images
        //cvtColor(crop, gray, CV_BGR2GRAY);
        
        // Form a filename
        filename = "/Users/tungalanagaraghavendraanudeep/Documents/faceRecClient/FaceRec3_";
        stringstream ssfn;
        ssfn <<filenumber<<"_"<<ts<< ".png";
        filename = filename+ssfn.str();
        filenumber++;
        
        imwrite(filename, res);
    }
    
    
    imshow("Image", frame);
    
    return flag;
    
}

int main(int argc, char *argv[])
{
    

    VideoCapture capture(0);
    if (!capture.isOpened()) {
        printf("--(!)Error with camera\n");
        return (-1);

    }
    

    
    if (!face_cascade.load(face_cascade_name)) {
        printf("--(!)Error loading\n");
        return (-1);
    }
    
    Mat frame;
    for(int i=0;i<=20;i++){
        
        
        
        capture.read(frame);
        //imshow("Original", frame);
        
        
        if (!frame.empty())
        {
            facedetect(frame,argv[1]);
        }
        else
        {
            printf(" --(!) No captured frame -- Break!");
            break;
        }
        
        
        
        
        
        if(waitKey(30) >= 0) break;
    }
    return 0;
}