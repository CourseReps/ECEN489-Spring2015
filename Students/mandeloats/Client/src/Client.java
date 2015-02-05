import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client
{
    public static void main(String args[]) throws Exception
    {
        String ipaddr;
        int serverPortNo;

        if (args.length == 2) {
            ipaddr = args[0].toString();
            serverPortNo = Integer.valueOf(args[1]).intValue();
        }
        else{
            ipaddr = "Mandels-MacBook.local";
            serverPortNo = 2222;
        }

        Scanner inFromUser = new Scanner(System.in);
        Socket clientSocket = new Socket(ipaddr, serverPortNo);
        PrintStream outToServer = new PrintStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String input = "";
        System.out.println("Type \\disconnect to quit\n");
        while(!input.equals("\\disconnect")){
            System.out.println("ClientResponse:");
            input = inFromUser.nextLine();
            outToServer.println(input);
            System.out.println("Server Response:");
            System.out.println(inFromServer.readLine());
        }
        inFromUser.close();
        clientSocket.close();
    }
}