#include <pcap.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/if_ether.h>
#include <net/ethernet.h>
#include <netinet/ether.h>

#include <signal.h> // for kernel CTRL-C capture
#include <string.h> // for memet
#include <unistd.h> // for checking if a file exists

// FUNCTION PROTOTYPES
FILE * create_new_file();
void help(char *filename);
void handle_radiotap_frame(u_char *args, const struct pcap_pkthdr* pkthdr, const u_char* packet);

void handle_ethernet_frame(u_char *args, const struct pcap_pkthdr* pkthdr, const u_char* packet);


FILE *thisFile;
int fileIncrement = 0;
int maxRecsPerFile = 10000;
int recsThisFile = 0;

int totalCount = 0;
int dataLinkType = 0;

int silenceOutput = 0;

// STRUCTS

// Frames captured in monitor mode
struct sniffed_frame {
    u_int8_t    radiotap_header[40];
    u_int8_t    mac_dest[6];
    u_int8_t    mac_src[6];
} __attribute__ ((__packed__));

// PCAP "packet header" -- part of library, shown here for reference only
// struct pcap_pkthdr {
//         struct timeval ts;   time stamp 
//         bpf_u_int32 caplen;  length of portion present 
//         bpf_u_int32;         lebgth this packet (off wire) 
// }

// Raw ethernet header -- taken from ethernet.h, shown here for reference only
// struct ether_header {
//   u_int8_t  ether_dhost[ETH_ALEN];	/* mac_dest eth addr	*/
//   u_int8_t  ether_shost[ETH_ALEN];	/* mac_src ether addr	*/
//   u_int16_t ether_type;		        /* packet type ID field	*/
// } __attribute__ ((__packed__));


// Kill command capture function (ctrl-c)
void signal_handler(int sig)
{
	if (sig == SIGINT)
	{
		fclose(thisFile);
		printf("\n\nMonitoring complete.\n");
		printf("%d packets captured\n\n", totalCount);
		exit (1);
	}
}

// PCAP Callback function
void my_callback (u_char *args, const struct pcap_pkthdr* pkthdr,
					const u_char* packet)
{
	if (dataLinkType == 127) {
		handle_radiotap_frame(args, pkthdr, packet);
	
	} else {

		handle_ethernet_frame(args, pkthdr, packet);
	}

	recsThisFile++;
	totalCount++;

	if (recsThisFile >= maxRecsPerFile) {

		fclose(thisFile);
		thisFile = create_new_file();
		recsThisFile = 0;
	}
}


// Breaks down radiotap frames into something we can use
void handle_radiotap_frame (u_char *args, const struct pcap_pkthdr* pkthdr, const u_char* packet)
{
    struct sniffed_frame *eptr;
	eptr = (struct sniffed_frame *) packet;

    struct timeval *timeStamp;
	timeStamp = (struct timeval *) &pkthdr->ts;

    char* source = ether_ntoa((const struct ether_addr *)&eptr->mac_src);
    char* dest = ether_ntoa((const struct ether_addr *)&eptr->mac_dest);

	if (silenceOutput == 0) {
	    // long int secs = (long int) timeStamp->tv_sec;
	    // long int usecs = (long int) timeStamp->tv_usec;

		printf ("TS: %li.%li | "
	            , (long int) timeStamp->tv_sec, (long int) timeStamp->tv_usec);
		printf ("SRC: %s \t | "
	            , ether_ntoa((const struct ether_addr *)&eptr->mac_src));
		printf ("DST: %s "
	            , ether_ntoa((const struct ether_addr *)&eptr->mac_dest));
	    printf("\n");
		fflush(stdout);	
	}

	fprintf(thisFile, "%li,", timeStamp->tv_sec);
	fprintf(thisFile, "%li,", timeStamp->tv_usec);
	fprintf(thisFile, "%s,", ether_ntoa((const struct ether_addr *)&eptr->mac_src));
	fprintf(thisFile, "%s\n", ether_ntoa((const struct ether_addr *)&eptr->mac_dest));

	// ,%li,%s,%s\n", timeStamp->tv_sec, timeStamp->tv_usec,
	// 	source, dest);
	// // fprintf(thisFile, "%li,%li,%s,%s\n", secs, usecs, source, dest);
	fflush(thisFile);
    return;
}

// Breaks down ehternet frames into something we can use
void handle_ethernet_frame (u_char *args, const struct pcap_pkthdr* pkthdr, const u_char* packet)
{
    struct ether_header *eptr;  /* net/ethernet.h */
	u_short ether_type;
	
	eptr = (struct ether_header *) packet;
    ether_type = ntohs(eptr->ether_type);

	printf ("Source: %s   \t"
            , ether_ntoa((const struct ether_addr *)&eptr->ether_shost));
    printf (" || Dest: %s   \t"
            , ether_ntoa((const struct ether_addr *)&eptr->ether_dhost));
	
	/* check to see if we have an ip packet */
    if (ether_type == ETHERTYPE_IP)
    {
        printf ("(IP)");
    }
	else  if (ether_type == ETHERTYPE_ARP)
    {
        printf ("(ARP)");
    }
	else {
        printf ("(?)");
    }
	
    printf("\n");
	fflush(stdout);
    return;
}


// Main function
int main (int argc, char **argv)
{
	int i;
	int argCount = 0;
	int filterLoc = 0;
	int devAssigned = 0;
	int promiscuous = 0;
	int maxPackets = -1;
	int monitorMode = 0;
	char *dev;
	char errbuf[PCAP_ERRBUF_SIZE];
	pcap_t* descr;
	const u_char *packet;
	struct pcap_pkthdr hdr;
	struct ether_header *eptr;
	struct bpf_program fp;
	bpf_u_int32 maskp;
	bpf_u_int32 netp;
	u_char* args = NULL;
	
	// Intercept CTRL-C
	signal(SIGINT, signal_handler);
	
	// HANDLE COMMAND LINE ARGUMENTS
    for (i = 1; i < argc; i++)  /* Skip argv[0] (program name). */
    {

		// Packet limit		
        if (strcmp(argv[i], "-q") == 0)
        {
            maxPackets = atoi(argv[i+1]);
			argCount = argCount + 2;
		}

		// Interface Selection
        else if (strcmp(argv[i], "-i") == 0)
        {
            dev = argv[i+1];            
			argCount = argCount + 2;
			devAssigned = 1;
        }

        // Set monitor mode on
        else if (strcmp(argv[i], "-m") == 0)
        {
        	monitorMode = 1;       
			argCount = argCount + 1;
        }

        // Set promiscuous mode on
        else if (strcmp(argv[i], "-p") == 0)
        {
			promiscuous = 1;
			argCount = argCount + 1;
        }

		// Set promiscuous mode on
        else if (strcmp(argv[i], "-s") == 0)
        {
			silenceOutput = 1;
			argCount = argCount + 1;
        }

        // Set maximum number of data points per file
        else if (strcmp(argv[i], "-f") == 0)
        {
        	maxRecsPerFile = atoi(argv[i+1]);
			argCount = argCount + 2;
        }

        // Ask for help
		else if ((strcmp(argv[i], "-h") == 0) ||
					(strcmp(argv[i], "--help") == 0))
		{
			help(argv[0]);
		}

    }
	
	// if there are not enough arguments, display help and abort
	if (argCount != (argc - 1))
	{	help(argv[0]); }
	
	// If network interface not explicitly chosen, find one
	if (devAssigned == 0)
	{	dev = pcap_lookupdev(errbuf); }

	// If the interface can't be found, abort
	if (dev == NULL)
	{
		printf("%s\n", errbuf);
		exit(1);
	}

	// Activate PCAP on the selected interface
	descr = pcap_create(dev,errbuf);
	if (descr == NULL)
	{
		printf("pcap_create() error\n");
		exit(1);
	}

	// Check to make sure it's possible, then enable monitor mode
	if (monitorMode == 1) {
		int monitor = pcap_can_set_rfmon(descr);
		if (monitor != 1)
		{
			printf("pcap_can_set_rfmon(): %d\n", monitor);
			exit(1);
		}

		monitor = pcap_set_rfmon(descr, 1);
		if (monitor != 0)
		{
			printf("pcap_set_rfmon() error\n");
			exit(1);
		}

		printf("Monitor mode enabled!\n");
	}

	// Enable promiscuous mode
	pcap_set_promisc(descr, promiscuous); // Turn promiscuous mode on

	if (promiscuous == 1) {
		printf("Promiscuous mode enabled!\n");
	}

	// Set the frame snapshot length and timeout
	pcap_set_snaplen(descr, 2048);  // Set the snapshot length to 2048
	pcap_set_timeout(descr, 512); // Set the timeout to 512 milliseconds
	
	// Turn on PCAP and ensure that we're successful -- abort otherwise
	int status = pcap_activate(descr);
	if (status != 0) {
		help(argv[0]);		
	}

	// We tell the user what's happening
	printf ("\nCapturing packets on interface: %s\n", dev);

	// Check the datalink type so we can break down our captured framed properly
	dataLinkType = pcap_datalink(descr);
	printf("Datalink type: %d\n", dataLinkType);

	// Create our logfile
	thisFile = create_new_file();

	// Begin capture
	pcap_loop (descr, maxPackets, my_callback, args);
	
	// Once the capture cycle is broken, display the following messages
	printf("\n\nMonitoring complete.\n");
	printf("%d packets captured\n\n", totalCount);
	return 1;
}

// I'm sure you can figure out what this does
void help(char *filename)
{
	printf ("\n");
	printf ("Proper usage: sudo %s -[argument] [value]", filename);
	printf ("\n\n");
	printf ("-p              Enable promiscuous mode (default: OFF)\n");
	printf ("-m              Enable monitor mode (default: OFF)\n");	
	printf ("-s              Silence screen output (less overhead busy networks)\n");	
	printf ("-f [#]          Number of frames recorded per file (default: 10000)\n");
	printf ("-q [#]          Stop after # total frames (default: -1\n");
	printf ("-i [interface]: Force network interface selection\n");
	printf ("-h, --help:   this help menu\n");
	printf ("\n");
	printf ("Ensure you are running this program with elevated permissions (sudo %s)\n\n",filename);
	exit (0);			
}

// This function creates a new file (and checks to see if others already exist before doing so)
FILE * create_new_file() {

	FILE *writeFile;
	char fname[30];

	do {
		
		sprintf(fname, "snifflog_%d.csv", fileIncrement);
		fileIncrement++;
	} while (access( fname, F_OK ) != -1);

	writeFile = fopen(fname, "w");

	if (writeFile == NULL) {
	  	printf("There was a problem creating file: %s\n", fname);
  		exit(0);
	}

	printf("Created new logfile: %s\n", fname);
	return writeFile;
}
