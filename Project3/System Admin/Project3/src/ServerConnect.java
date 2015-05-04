import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;
/**
 * Created by RhoadsWylde on 5/4/2015.
 */
public class ServerConnect {
    public class SSHReadFile
    {
        public void main(String args[])
        {
            String user = "Blade";
            String password = "mypassword";
            String host = "192.168.100.23";
            int port=22;

            String remoteFile="/home/john/test.txt";

            try
            {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                System.out.println("Establishing Connection...");
                session.connect();
                System.out.println("Connection established.");
                System.out.println("Crating SFTP Channel.");
                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                System.out.println("SFTP Channel created.");


                InputStream out= null;
                out= sftpChannel.get(remoteFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(out));
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                br.close();
            }
            catch(Exception e){System.err.print(e);}
        }
    }
}
