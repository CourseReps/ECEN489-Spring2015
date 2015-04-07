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

Column Name | Details 
--- | --- 
ID (Key) | Auto-incrementing key for this user.  Used in Friends, Friend Requests, and Check-Ins tables
Username | User-selected username -- also what is displayed for other users on their friends list
Password | The password (or password hash) for this user

**FRIENDS TABLE**

Column Name | Details 
--- | --- 
ID1 | See text below
ID2 | See text below
The FRIENDS table will contain pairs of IDs.  These IDs will match up with the users in the USERS table.  Each pair of IDs constitutes a friendship between to users.  To improve searching efficiency, whenever a new friendship is established, a pair of entries is created for each relationship.

For example, if John (ID: 1001) and Jane (ID: 1337) become friends, the following entries will be added to the FRIENDS table:

ID1 | ID2 
--- | --- 
1001 | 1337
1337 | 1001

**REQUEST TABLE**

Column Name | Details 
--- | --- 
Originator | User who made the friend request
Recipient | User who will respond to the request
The REQUEST table is populated when a user makes a friend request.  If the recieving user responds to the friend request, the entry in this table is removed and a pair of friendship entries are created in the FRIENDS table (if accepted).

**LOCATIONS TABLE**

Column Name | Details 
--- | --- 
ID | User who made the friend request
Location Name | Plain English label for this LOI
Picture Info | Image processing information for object identification (or reference to where the information can be found)
GPS Coords | Lat/Lon coordinates of this location

**CHECK-INS TABLE**

Column Name | Details 
--- | --- 
User ID (Key1) | ID reference for the user who is being checked in
Timestamp (Key2) | Timestamp for this check-in (in absolute millis)
Location ID | ID Reference for the LOI being checked in to
Method | The means by which this user is being checked in (object identification, MAC sniffing, facial recognition, etc)
Details/Data | Additional relevant information pertaining to the check-in -- MAC Addr for MAC sniffing, filename for facial recognition, etc
A dual key is used in this table to ensure that are no duplicate check-ins for users.

###### User Authentication/Credentials
###### Social media integration
#### Image processing/object identification
###### MAC Address tracking
###### Facial recognition
###### QR Code reader
###### Voice Recognition
#### User application
###### User greeting
