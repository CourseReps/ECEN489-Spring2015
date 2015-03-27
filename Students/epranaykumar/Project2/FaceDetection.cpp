#include "StdAfx.h"
#include<stdio.h>
#include<math.h>
#include<opencv\cv.h>
#include<opencv\highgui.h>
#include<opencv2\objdetect\objdetect.hpp>
#include<opencv2\highgui\highgui.hpp>
#include<opencv2\imgproc\imgproc.hpp>
#include<vector>
#include <string>

using namespace cv;
using namespace std;

int main()
{
	CascadeClassifier face_cascade, eye_cascade;
	if(!face_cascade.load("D:\\OpenCV2.4.9\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml")) {
		printf("Error loading cascade file for face");
		return 1;
	}


	/*if(!eye_cascade.load("D:\\OpenCV2.4.9\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml")) {
		printf("Error loading cascade file for eye");
		return 1;
	}*/


	VideoCapture capture(0); //-1, 0, 1 device id
	if(!capture.isOpened())
	{
		printf("error to initialize camera");
		return 1;
	}
	Mat cap_img,gray_img;
	vector<Rect> faces, eyes;
	int NumFaces =0;
	int maximage=0;
	Mat croppedFaces;
	while(NumFaces < 1 ) 
	//for (unsigned int index = 0; index < 10; index++)
	{
		capture >> cap_img;
		waitKey(10);
		cvtColor(cap_img, gray_img, CV_BGR2GRAY);
		cv::equalizeHist(gray_img,gray_img);
		face_cascade.detectMultiScale(gray_img, faces, 1.1, 10, CV_HAAR_SCALE_IMAGE | CV_HAAR_DO_CANNY_PRUNING, cvSize(0,0), cvSize(300,300));
		for(unsigned int i=0; i < faces.size();i++)
		{
			Point pt1(faces[i].x+faces[i].width, faces[i].y+faces[i].height);
			Point pt2(faces[i].x,faces[i].y);
			Mat faceROI = gray_img(faces[i]);
			//eye_cascade.detectMultiScale(faceROI, eyes, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30,30));
			//for(size_t j=0; j< eyes.size(); j++)
			//{
			//	//Point center(faces[i].x+eyes[j].x+eyes[j].width*0.5, faces[i].y+eyes[j].y+eyes[j].height*0.5);
			//	Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
			//	int radius = cvRound((eyes[j].width+eyes[j].height)*0.25);
			//	circle(cap_img, center, radius, Scalar(255,0,0), 2, 8, 0);
			//}
			
			croppedFaces= cap_img(faces[i]);

			String path="P:\\Masters Courses\\Mobile Sensing\\course_repository\\Project2\\Project2_FaceDetection\\FaceDetection\\croppedimages\\";
		    imwrite( path + "th"+ std::to_string(static_cast<long long>(i)) + ".jpg", croppedFaces);
		   // imwrite( path + "th.jpg", croppedFaces );
			
				
			rectangle(cap_img, pt1, pt2, cvScalar(0,255,0), 2, 8, 0);
		}

		int height = 85;
		NumFaces = faces.size();
				
		putText(cap_img, "Number of People = " + std::to_string(static_cast<long long>(NumFaces)) , cvPoint(400, height), FONT_HERSHEY_SIMPLEX, 0.3, cvScalar(0, 255, 0), 1, CV_AA);
		
		imshow("Result", cap_img);
		waitKey(3);

		
		char c = waitKey(3);
		if(maximage ==20)
			break;
		
		if(c == 27)
			break;
	}
	//
	exit(0);
	//return 0;
}