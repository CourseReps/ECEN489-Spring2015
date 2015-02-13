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



Linux distro we will use: Debian

[Link to the latest stable Debian install DVD (USB bootable -- you only need the first DVD)] (http://cdimage.debian.org/debian-cd/7.8.0/amd64/iso-dvd/debian-7.8.0-amd64-DVD-1.iso)


Debian Install Instructions
--------------------
There is a problem with the NUC firmware where it fails to recognize a boot device after Linux is installed.  Here is the workaround:

.1. Install debian

.2. At the INSTALL COMPLETE screen, select GO BACK instead of continue

.3. Press **ctrl-alt-F2** to move to a second console.  Press enter to activate the console

.4. Enter the following five commands:


`mount /dev/sda1 /mnt`

`mkdir /mnt/EFI/BOOT`

`cp /mnt/EFI/ubuntu/* /mnt/EFI/BOOT`

`mv /mnt/EFI/BOOT/grubx64.efi /mnt/EFI/BOOT/bootx64.efi`

`exit`


.5. Press **ctrl-alt-F1** to move back to the install screen

.6. Select FINISH INSTALL, remove the USB drive and restart

.7. Enjoy Debian!

Project Architecture
--------------------


### Fusing Table

### Cloud Server

The Cloud 

### Android DataMule

The data mule is an Android app that can connects to the Sensor Box and acquired the gathered observations.
This data set should be stored locally in an SQLite database.
Once this information is stored on the device, the data mule must seek an Internet Wi-Fi connection and transfer the data to the Cloud server.


### Sensor Box (GNU/Linux Debian)

