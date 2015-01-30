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

Linux distro we will use: [Debian] (https://www.debian.org/distrib/)


Project Architecture
--------------------

