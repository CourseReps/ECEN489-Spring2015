The program in this directory uses the PCAP library to access the wireless card directly and pull MAC data from ethernet frames flying through the air.  It outputs the information we should need in CSV format and creates a new file every so many entries (default: every 10000 frames).  The intention is that a second Java program will come in behind it every so often and parse the files/populate an SQL database.

1: Install the pcap library  
`sudo apt-get install libpcap-dev`  

2: Download the source on our Git [right here](https://github.com/CourseReps/ECEN489-Spring2015/blob/master/Project1/Team2/PromiscuousBox/8.wireless_mac_sniffing.c)  

3: Build using the pcap library  
`gcc SRC.c -o EXE -lpcap`  

4: Run the program  
`sudo EXE -m -i wlan1`  

5: Use the -h or --help flags if you want to customize the program's behavior

6: Check out the created CSV files.  Format:  
`timestamp in S,    timestamp in uS,    source MAC,    destination MAC`

7: Profit!

Some things to keep in mind...
* This program was written on top of an old packet counter I wrote many moons ago -- there may be legacy code or bugs hiding somewhere  
* This program returns MAC addresses and timestamps ONLY  
* Viewing IP traffic and sniffing data would require us to be attached to a network  
* If you are attached to a network and do NOT choose monitor mode, it will show IP traffic (but won't log it to a file)  
* Timestamps are in seconds + microseconds, returned from the wireless device through PCAP  
* Because speed is key, this program ONLY logs and does not perform any other operations
* The program is designed to create multiple CSV files, so a second program can come in behind it and do the heavy lifting with minimal delay
