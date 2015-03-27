package com.anudeep;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {


        startFTPServer();


        //creates a listener socket on port 9000 which can have 5 clients on wait
        ServerSocket listener = new ServerSocket(9000,5);
        System.out.println("Server IP:" + Inet4Address.getLocalHost().getHostAddress());
        System.out.println("Server is running on Port 9000....");
        try {
            //infinite loop to continuously listen for new client connections
            while (true) {
                //Threading enables to connect with many clients simultaneously
                new ServerThread(listener.accept()).start();
            }
        } finally {
            //shutting down the server by closing the socket
            listener.close();
        }

    }

    public static void startFTPServer() {

        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(1234);// set the port of the listener (choose your desired port, not 1234)
        serverFactory.addListener("default", factory.createListener());
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        try {
            OutputStream output = new FileOutputStream("C:/Users/tungala/Desktop/ftpStorage/myusers.properties");
            output.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        userManagerFactory.setFile(new File("C:/Users/tungala/Desktop/ftpStorage/myusers.properties"));//choose any. We're telling the FTP-server where to read it's user list
        userManagerFactory.setPasswordEncryptor(new PasswordEncryptor()
        {//We store clear-text passwords in this example

            @Override
            public String encrypt(String password) {
                return password;
            }

            @Override
            public boolean matches(String passwordToCheck, String storedPassword) {
                return passwordToCheck.equals(storedPassword);
            }
        });
        //Let's add a user, since our myusers.properties files is empty on our first test run
        BaseUser user = new BaseUser();
        user.setName("test");
        user.setPassword("test");
        user.setHomeDirectory("C:/Users/tungala/Desktop/ftpStorage/files");
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        UserManager um = userManagerFactory.createUserManager();
        try
        {
            um.save(user);//Save the user to the user list on the filesystem
        }
        catch (FtpException e1)
        {
            //Deal with exception as you need
        }
        serverFactory.setUserManager(um);
        Map<String, Ftplet> m = new HashMap<String, Ftplet>();
        m.put("miaFtplet", new Ftplet()
        {

            @Override
            public void init(FtpletContext ftpletContext) throws FtpException {
                //System.out.println("init");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }

            @Override
            public void destroy() {
                //System.out.println("destroy");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }

            @Override
            public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException
            {
                //System.out.println("beforeCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException
            {
                //System.out.println("afterCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine() + " | " + reply.getMessage() + " : " + reply.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult onConnect(FtpSession session) throws FtpException, IOException
            {
                //System.out.println("onConnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException
            {
                //System.out.println("onDisconnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }
        });
        serverFactory.setFtplets(m);
        //Map<String, Ftplet> mappa = serverFactory.getFtplets();
        //System.out.println(mappa.size());
        //System.out.println("Thread #" + Thread.currentThread().getId());
        //System.out.println(mappa.toString());
        FtpServer server = serverFactory.createServer();
        try
        {
            server.start();//Your FTP server starts listening for incoming FTP-connections, using the configuration options previously set
        }
        catch (FtpException ex)
        {
            //Deal with exception as you need
        }
    }


    private static class ServerThread extends Thread {
        private Socket socket;

        /*
        Constructor for the ServerThread class
         */
        public ServerThread(Socket socket) {
            this.socket = socket;

            System.out.println("New connection with  at " + socket);
        }
        /*
        Logic to be implemented while thread is started from the main function
         */
        public void run() {
            try {



                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String line = in.readLine();

                JSONObject obj = (JSONObject) JSONValue.parse(line);

                String deviceName = obj.get("connect").toString();

                System.out.println("Client Name: "+deviceName);

                JSONObject sendObj = new JSONObject();
                sendObj.put("takepicture","12345");

                out.println(sendObj.toString());
                out.flush();

                line = in.readLine();
                obj.clear();
                obj = (JSONObject) JSONValue.parse(line);

                String fileName = obj.get("filename").toString();

                System.out.println("File : "+fileName +" is Transmitted");





            } catch (Exception e) {
                System.out.println("Error handling client: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error while closing the Socket.... Socket can not be closed");
                }
                System.out.println("Connection with client closed");
            }
        }


    }
}
