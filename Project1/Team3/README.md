#Project 1: Team 3
=================

* Kevin Christopher Wilkens, [kevinwilkens22](https://github.com/kevinwilkens22) | The Notorious S.E.R.V.E.R.
* Hong Pan, [hongpan0507](https://github.com/hongpan0507) | PromiscuousBox 
* Yajie Zeng, [yjzeng8833](https://github.com/yjzeng8833) | R2Data
* Evelyn Susanah Cifuentes, [cifuentesevelyn](https://github.com/cifuentesevelyn) | elCloudo

#Critical Path

## Sensor Box (Promiscuous Box)

**Module Lead: Hong**
###Essentials:  

**PRIORITY.  Need to get promiscuous mode working and reading mac addresses of all nearby devices.**

Next, we need to solve problems with connecting devices and sharing content via bluetooth, and in the meantime explore options with wifi connection and data transfer to the mule.

*Monday 2/23/2014 Deliverables:* Present MAC address data read by the network card in monitor mode.  Also filter some MAC addresses.

## Android Mule (R2Data)

**Module Lead: Yajie**
###Essentials:  



Collaborate with PB team lead to find a suitable interface for collecting data from the PB.  Have an Android app that is able to receive data from the PB and store it into a local SQLite database.

*Monday 2/23/2014 Deliverables:* Get interface between PB and R2Data. 

## Server (The Notorious S.V.R.)

**Module Lead: Kevin**
###Essentials:  

We currently have programs that read data from an Android device and can store said data into a SQLite database.  Current goal is to solve issues regarding writing data to Fusion Tables. This program will read the SQLite database, pull data from the read database and write it into a database local to the server computer, and will run a check to ensure that no duplicate data is written into the server database file.

*Monday 2/23/2014 Deliverables:* Present a server program that will read a SQLite database file from an Android device into a SQLite database into the Linux Server.  

## Fusion Tables (El Cloudo)

**Module Lead: Evelyn**
###Essentials:
Write data to Fusion tables using Java.
[Wiki page](https://github.com/CourseReps/ECEN489-Spring2015/wiki/Project1Team3-Wiki)

