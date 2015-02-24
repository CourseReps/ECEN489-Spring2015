Project 1: Passive Device Monitoring
====================================

This project focuses on identifying active [Wi-Fi](http://www.wi-fi.org/) devices using a network of wireless sensing nodes.
The sensors will collect network traffic meta data in monitor mode, also called [promiscuous mode](http://en.wikipedia.org/wiki/Promiscuous_mode).
Monitor mode enables a Wi-Fi network interface card to capture certain aspects of Wi-Fi packets in an area, regardless of their source or intended destination.
Leveraging this information, it is possible to estimate useful parameters such as current population density, data traffic trends over a period of time, or most frequent visitors.

With this type of infrastructure, it is also possible to study the effect of antenna design on overall performance.
More specifically, the antenna radiation patterns associated with the acquisition devices will dictate which locations are surveyed by the sensor nodes.
By tuning the illuminated areas, it is possible to match the gathered data to the inference tasked at hand.


Monitoring Hardware
-------------------

The selection for physical components for the monitoring nodes is guided by usability and ease of implementation.
This project utilizes the [Debian](https://www.debian.org/) [GNU](https://www.gnu.org/)/Linux operating system to power the sensing devices.
This choice is motivated by its [free](http://www.fsf.org/) distribution, its programmability, and the availability of drivers.

### Intel Next Unit of Computing

The computers employed for data collection are based on the Next Unit of Computing (NUC) by [Intel](http://www.intel.com/).
The NUCs have sufficient power to perform the necessary operations, and they possess a small form-factor.

### Network Interface Cards

Careful selection of a wireless adapter is essential because only a select few adapters can operate in monitor mode.
Indeed, monitor mode permits several types of traffic analyses, including illegitimate usage such as spying or hacking.
For this reason, many manufacturers do not support monitor mode with their adapters.
Fortunately, [Atheros](http://www.qca.qualcomm.com/) allows some of its NIC chipsets to operate in monitor mode.
This project employs the [TP-LINK](http://www.tp-link.us/) TL-WN722N Wireless N150 High Gain USB Adapter, as it features one such Atheros chipset.

Two possible models of wireless dongle:

[TP-LINK TL-WN722N](http://www.amazon.com/TP-LINK-TL-WN722N-Wireless-Adapter-External/dp/B002SZEOLG/ref=sr_1_1?ie=UTF8&qid=1422659883&sr=8-1&keywords=TP-Link%27s+TL-WN722N&pebp=1422659900492&peasin=B002SZEOLG)

[Alfa AWUS036NH] (http://www.amazon.com/Alfa-AWUS036NH-802-11g-Wireless-Long-Range/dp/B003YIFHJY/ref=sr_1_fkmr0_2?s=electronics&ie=UTF8&qid=1422660889&sr=1-2-fkmr0&keywords=atheros+9280)


Network Card Update
#1
TP-Link:
Model Brand:					TP-LINK
Model:							TL-WN722N
Specs
Standards:					IEEE 802.11b/g/n
Wireless Data Rates:		Up to 150Mbps
Security:						64/128 bit WEP, WPA-PSK/WPA2-PSK
Interface:						USB 2.0
Frequency Band:			2.4GHz - 2.4835GHz
Modulation:					DBPSK, DQPSK, CCK, OFDM, 16-QAM, 64-QAM
Antenna:						4dBi Detachable Omni-directional Antenna (RP-SMA)
System Requirements:	OS: Windows 7(32/64bits), Windows Vista(32/64bits), Windows XP(32/64bits), Windows 2000
Dimensions:					3.7" x 1.0" x 0.4"
Weight:							0.35
Temperature:					0°C - 40°C (32°F - 104°F)
Humidity:						10% - 90% (Non-Condensing)
Features:						Wireless Modes: Ad-Hoc / Infrastructure mode
									Advanced Functions: WMM, PSP X-LINK(For Windows XP), Roaming
									Wireless N speed up to 150Mbps makes it ideal for video streaming, online gaming and VoIP
									High gain external antenna brings better performance than conventional internal antenna
									Easy wireless security encryption at a push of the QSS button
Can be purchased from Amazon.com for $14.99 or Newegg.com for $17.99 and other retailers


#2
TP-Link:
Model Brand:					Alfa
Model:							AWUS036NH
Specs:
Wireless: 						IEEE 802.11b/g/n
Interface:						USB 2.0 standard to USB 2.0 mini USB
Data Rate:					802.11b: UP to 11Mbps
									802.11g: UP to54Mbps
									802.11n: UP to 150Mbps
OS support: 					Windows 2000, XP, Vista, Windows 7, Linux 2.6, Mac 10.4, 10.5, 10.6
Antenna type: 				1 x 2.4Ghz RP-SMA connector
Antenna: 						5dBi 2.4GHz Antenna
Frequency Range: 		2.412~2.483 GHz
Chipset: 						RT3070
One LED: 						Power/Status, Wireless Active
Channel:						1~11 channels ( North America)
Output power:				802.11b : 33dBm±1
									802.11g : 32dBm±1
									802.11n (HT20) : 32dBm±1
									802.11n (HT40) : 33dBm±1
Sensitivity:					11b: -92dBm
									11g: -76dBm
									11n: -73dBm@HT20
									-70dBm@HT40
Data Modulation Type:	BPSK,QPSK, CCK and OFDM
Power: 							Voltage: 5V+5%
Security:						WEP 64/128 802.1X support
									Wi-Fi Protected Access (WPA)
									WPA-PSK
									WPA II
									Cisco CCX support
									WAPI-PSK
									WAPI-CERT Environment
Operating Temp:			0°C ~ +50°C
Storage: 						-10°C ~ +65°C
Humidity: 						5%~98% non-condensing
Can be purchased from Amazon.com for $39.99 or Newegg.com for $33.99 and other retailers

Linux distro we will use: Ubuntu

Project Architecture
--------------------


### Fusing Table


### Cloud Server

The Cloud 

###The Notorious SVR

2/23/2015 update:  The Notorious SVR will be receiving .db files from R2Data.  The databases will be named with a timestamp based on when R2Data pulled the data from the PB, and the SVR will parse the databases received from R2Data into a single organized database.  


### Android DataMule aka (R2Data)

The data mule is an Android app that can connects to the Sensor Box and acquires the gathered observations.
This data set should be stored locally in an SQLite database.
Once this information is stored on the device, the data mule must seek an Internet Wi-Fi connection and transfer the data to the Cloud server.

2/23/2015 update:

######PB-R2Data Interface Update
-The interface from the PB to the R2Data will be implemented using Bluetooth communications. A Java server program will be written to take apart the local SQL database file and send the components over as a serilizable object to the Android app. The Android app will take the entries and compose a new local database on the R2Data. The format of the database is currently TBD.~gmnealusn



### [Sensor Box (GNU/Linux Debian)](https://github.com/CourseReps/ECEN489-Spring2015/tree/master/Project1/Team2/PromiscuousBox)

2/23/2015 update:
Responsibilities - 


How to Use WireShark and Enable Monitor Mode
--------------------

1. Install wireshark (sudo apt-get install wireshark)
2. Turn off your wireless dongle, put it in montior mode, then turn it back on (INTERFACE should be wlan1):  
    sudo ifconfig (INTERFACE) down  
    sudo iwconfig (INTERFACE) mode monitor  
    sudo ifconfig (INTERFACE) up  
3. Start wireshark in root mode (ignore the warnings)  
    sudo wireshark  
4. Start monitoring on the desired interface (it should be WLAN1)
5. Profit!
6. To return the card to normal operation, perform step 2 again, but use the command MODE MANAGED instead of monitor


Managing Updating of the Wireshark in Monitor Mode
--------------

1. Due to the speed of people, we will only monitor in seconds instead of milliseconds
2. Collections will work better than arrays due to not allowing duplicates

~Bladeroybal Updated 6:04pm 2/23/2015
