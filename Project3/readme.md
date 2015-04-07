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

## Subsystem Breakdown
#### Server Backbone (SB)
The server backbone will house all backend functionality.  Basic functions are the storage and maintenance of user and location information.  The storage method of choice for this information is SQLite, and the backend programming language of choice is Java.

User information includes things like login credentials, user "friendships", and location tracking over time.  Location information includes things like location name, object identification information, and (possibly) GPS coordinates.

#### Location Identification (LI)
This subsystem will provide necessary functionality to allow the SB to identify and track users as they move between Predetermined Locations of Interest (PLI).  The means used to identify that a user is in the vicinity of a PLI **have not been finalized at this time**, but methods of "checking in" include:

- Using a smartphone camera and image processing to photograph a unique object at the PLI
- Sniffing wireless frames and detecting when a user's MAC address is present near a PLI 
- Using facial or voice recognition to physically identify a user near a PLI
- Using a QR code and reader as a robust method of checking in to a PLI

#### User Feedback (UF)
User feedback consists of everything that the end user may interact with.  **Currently,** this consists of an Android application and (possibly) a "user greeting" system that will provide an audio or visual cue (display an image, play a sound, send a text) that indicates the user has been recognized at a PLI.

## Functional Component Breakdown
#### Server Database Management
Server Backbone information will be stored in a series of SQLite tables as follows:

**USERS TABLE**

Column Name | SQL Name | Details 
--- | --- | ---
ID (Key) | userid | Auto-incrementing key for this user.  Used in Friends, Friend Requests, and Check-Ins tables
Username | username | User-selected username -- also what is displayed for other users on their friends list
Password | password | The password (or password hash) for this user

**FRIENDS TABLE**

Column Name | SQL Name | Details 
--- | --- | ---
ID1 | id1 | See text below
ID2 | id2 | See text below
The FRIENDS table will contain pairs of IDs.  These IDs will match up with the users in the USERS table.  Each pair of IDs constitutes a friendship between to users.  To improve searching efficiency, whenever a new friendship is established, a pair of entries is created for each relationship.

For example, if John (ID: 1001) and Jane (ID: 1337) become friends, the following entries will be added to the FRIENDS table:

id1 | id2 
--- | --- 
1001 | 1337
1337 | 1001

**REQUEST TABLE**

Column Name | SQL Name | Details 
--- | --- | ---
Originator | reqid1 | User who made the friend request
Recipient | reqid2 | User who will respond to the request
The REQUEST table is populated when a user makes a friend request.  If the recieving user responds to the friend request, the entry in this table is removed and a pair of friendship entries are created in the FRIENDS table (if accepted).

**LOCATIONS TABLE**

Column Name | SQL Name | Details 
--- | --- | ---
ID (Key) | locid | Auto-incrementing key for this location. Used in CHECK-INS table
Location Name | locname | Plain English label for this LOI
Picture Info | locimg | Image processing information for object identification (or reference to where the information can be found)
GPS Coords | locgps | Lat/Lon coordinates of this location

**CHECK-INS TABLE**

Column Name | SQL Name | Details 
--- | --- | ---
User ID (Key1) | chkuid | ID reference for the user who is being checked in
Timestamp (Key2) | chkts | Timestamp for this check-in (in absolute millis)
Location ID | chklid | ID Reference for the LOI being checked in to
Method | chkmeth | The means by which this user is being checked in (object identification, MAC sniffing, facial recognition, etc)
Details/Data | chkdata | Additional relevant information pertaining to the check-in -- MAC Addr for MAC sniffing, filename for facial recognition, etc
A dual key is used in this table as a way of passively ensuring that are no duplicate check-ins.

###### User Authentication/Credentials
###### Social media integration
#### Image processing/object identification
###### MAC Address tracking
###### Facial recognition
###### QR Code reader
###### Voice Recognition
#### User application
The user application will provide the primary interface between the user and the rest of the project.  The expected application lifecycle is as follows:

ID | Activity | Behavior | Exit Conditions (next activity) 
--- | --- | --- | ---
1 | Login | User registration/login screen | User logs in (2 first login, 3 otherwise)
2 | Friend Manager | List friends, allow user to add/remove new friends | User cancels/done (3)
3 | Friend Feed | Show list of friends' recent check-ins | User requests change to another activity (2, 4-7)
4 | Check-In | Provide manual check-in interface (take picture, scan QR code, etc) | User cancels (3) or Checks In (5)
5 | Current Location | Show statistics of current location (friends checked in, previous check-ins) | User cancels (3)
6 | Location History | Show location history for this user (on map?) | User cancels (3)
7 | Logout | User feedback while logging out/shutting down | Logged out -- end program

###### User greeting
