// Chat Server

import java.net.*;
import java.util.*;
import java.io.*;

class ChatServer
{
    static Vector ClientSockets;
    static Vector LoginNames;
    public static int numYes = 0;
    public static int numNo = 0;
    public final int numVoters = 3;

    ChatServer() throws Exception
    {
        ServerSocket soc = new ServerSocket(9000);
        ClientSockets = new Vector();
        LoginNames = new Vector();

        while(true)
        {
            Socket CSoc=soc.accept();
            AcceptClient obClient=new AcceptClient(CSoc);
        }
    }
    public static void main(String args[]) throws Exception
    {
        ChatServer ob = new ChatServer();
    }

    public boolean checkNumVotes()
    {
        int sum = numYes + numNo;
        if (sum == numVoters)
            return true;
        else
            return false;
    }

    class AcceptClient extends Thread
    {
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
        AcceptClient (Socket CSoc) throws Exception
        {
            ClientSocket=CSoc;

            din=new DataInputStream(ClientSocket.getInputStream());
            dout=new DataOutputStream(ClientSocket.getOutputStream());

            String LoginName=din.readUTF();

            System.out.println("User Logged In :" + LoginName);
            LoginNames.add(LoginName);
            ClientSockets.add(ClientSocket);
            start();
        }

        public void run()
        {
            while(true)
            {

                try
                {
                    String msgFromClient = new String();
                    msgFromClient=din.readUTF();
                    System.out.println(msgFromClient);
                    StringTokenizer st=new StringTokenizer(msgFromClient);
                    String Sendto=st.nextToken();
                    //String MsgType=st.nextToken();
                    int iCount=0;

                    if(msgFromClient.equals("LOGOUT"))
                    {
                        for(iCount=0;iCount<LoginNames.size();iCount++)
                        {
                            if(LoginNames.elementAt(iCount).equals(Sendto))
                            {
                                LoginNames.removeElementAt(iCount);
                                ClientSockets.removeElementAt(iCount);
                                System.out.println("User " + Sendto +" Logged Out ...");
                                break;
                            }
                        }

                    }
                    else if(msgFromClient.equals("YES"))
                    {
                        numYes++;
                        boolean doneVoting = checkNumVotes();
                        System.out.println(numYes);
                        System.out.println(doneVoting);
                        if(doneVoting)
                        {
                            for(iCount=0;iCount<LoginNames.size();iCount++)
                            {
                                System.out.println("Login names");
                                System.out.println("Send votes");
                                Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
                                DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                                tdout.writeUTF("Number of Yes votes: " + numYes + "Number of No votes: " + numNo);
                            }
                        }
                    }
                    else if(msgFromClient.equals("NO"))
                    {
                        numNo++;
                        boolean doneVoting = checkNumVotes();

                        if(doneVoting)
                        {
                            for(iCount=0;iCount<LoginNames.size();iCount++)
                            {
                                Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
                                DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                                tdout.writeUTF("Number of Yes votes: " + numYes + "Number of No votes: " + numNo);
                            }
                        }
                    }
                    else
                    {
                        /*
                        String msg="";
                        while(st.hasMoreTokens())
                        {
                            msg=msg+" " +st.nextToken();
                        }
                        for(iCount=0;iCount<LoginNames.size();iCount++)
                        {
                            if(LoginNames.elementAt(iCount).equals(Sendto))
                            {
                                Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
                                DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                                tdout.writeUTF(msg);
                                break;
                            }
                        }
                        if(iCount==LoginNames.size())
                        {
                            dout.writeUTF("I am offline");
                        }
                        else
                        {

                        }
                        */
                    }
                    if(msgFromClient.equals("LOGOUT"))
                    {
                        break;
                    }

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }



            }
        }
    }
}