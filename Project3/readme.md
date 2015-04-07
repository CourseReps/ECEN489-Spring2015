Project 3: Social Location Sharing
====================================

![IMAGE HERE](http://www.redcylindersoftware.com/489/Prpject3img.jpg)

This project will allow users to be tracked in space and time.  Users will have the ability to "check in" to a series of predetermined locations and track the check-in activities of their friends.


This project is broken down into three major subsystems (TENTATIVE)  
1. Server backbone **(SB)**  
2. Location identification **(LI)**  
3. User feedback **(UF)**  


The subsystems are further broken down into **X** functional components (TENTATIVE)  

Component Name | Language or API | Subsystem
--- | --- | ---
Server Database Management | Java/SQLite | SB
User Authentication/Credentials | ??? API | SB
Social media integration | Facebook | SB  
Image processing/object identification | OpenCV | LI  
MAC Address tracking | PCAP | LI
Facial recognition | OpenCV | LI  
QR Code reader | Android | LI  
Voice Recognition | ??? API | LI  
User application | Android | UF  
User greeting | Java or microcontroller? | UF  
to be continued... | |  

### Subsystem Breakdown
##### Server Backbone (SB)
The server backbone will house all backend functionality.  Basic functions are the storage and maintenance of user and location information.  The storage method of choice for this information is SQLite, and the backend programming language of choice is Java.

User information includes things like login credentials, user "friendships", and location tracking over time.  Location information includes things like location name, object identification information, and (possibly) GPS coordinates.

##### Location Identification (LI)
This subsystem will provide necessary functionality to allow the SB to identify and track users as they move between Predetermined Locations of Interest (PLI).  The means used to identify that a user is in the vicinity of a PLI **have not been finalized at this time**, but methods of "checking in" include:

- Using a smartphone camera and image processing to photograph a unique object at the PLI
- Sniffing wireless frames and detecting when a user's MAC address is present near a PLI 
- Using facial or voice recognition to physically identify a user near a PLI
- Using a QR code and reader as a robust method of checking in to a PLI

##### User Feedback (UF)
User feedback consists of everything that the end user may interact with.  **Currently,** this consists of an Android application and (possibly) a "user greeting" system that will provide an audio or visual cue (display an image, play a sound, send a text) that indicates the user has been recognized at a PLI.

### Functional Component Breakdown
#### Server Database Management
###### User Authentication/Credentials
###### Social media integration
#### Image processing/object identification
###### MAC Address tracking
###### Facial recognition
###### QR Code reader
###### Voice Recognition
#### User application
###### User greeting
