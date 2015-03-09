Project 2: Big Brother NSA 2.0
====================================

This project will be build on top of Project 1 using statistics/ analytics of MACs. Four sensing stations will be set up
to focus on a central area.  When a MAC address is sensed in this overlapped area, a picture will be taken using
a camera next to tv.  This system will try to link a person to the mac address and possibly the image. 

Roles:
RF Antena
Sensing box
Server
Data enhencement through open cv
Facial recognition

Project 1 Roles
-RF
-Sensing Box/Server
-Facial Recognition
  -Capture face with 2 cameras
  -use the photos with open CV to isolate the face (2 people using Android and 2 using native)
-Microcontroller (2 people for ping sensor and 2 people for gain)
  -control antenna gain, changing the voltage will change the gain (2 people)
      -distance that the signal can go
      -direction of the signal
    -communicate with the NUC via usb serial
  -"ping" sensor (2 people)
