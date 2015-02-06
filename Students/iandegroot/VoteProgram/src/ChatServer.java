// Chat Server

import java.net.*;
import java.util.*;
import java.io.*;

class ChatServer
{
    static Vector ClientSockets;
    static Vector LoginNames;

    ChatServer() throws Exception
    {
        ServerSocket soc=new ServerSocket(5217);
        ClientSockets=new Vector();
        LoginNames=new Vector();

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
                    StringTokenizer st=new StringTokenizer(msgFromClient);
                    String Sendto=st.nextToken();
                    String MsgType=st.nextToken();
                    int iCount=0;

                    if(MsgType.equals("LOGOUT"))
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
                    else
                    {
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
                    }
                    if(MsgType.equals("LOGOUT"))
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