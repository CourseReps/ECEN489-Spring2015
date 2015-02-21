The program in this directory uses the PCAP library to access the wireless card directly and pull MAC data from ethernet frames flying through the air.  It outputs the information we should need in CSV format and creates a new file every so many entries (default: every 10000 frames).  The intention is that a second Java program will come in behind it every so often and parse the files/populate an SQL database.

1: Install the pcap library  
`sudo apt-get install libpcap-dev`  

2: Download the pcap-based program found [in this directory](https://github.com/CourseReps/ECEN489-Spring2015/blob/master/Project1/Team2/PromiscuousBox/8.wireless_mac_sniffing.c)  

3: Build using the pcap library  
`gcc SRC.c -o EXE -lpcap`  

4: Run the program  
`sudo EXE -m -i wlan1`  

5: Use the -h or --help flags if you want to customize the program's behavior

6: Check out the created CSV files.  Format:  
`timestamp in S,    timestamp in uS,    source MAC,    destination MAC`

7: Profit!
