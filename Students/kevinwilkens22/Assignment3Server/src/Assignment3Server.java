
import java.io.*;


/**
 * Created by kwilk_000 on 2/4/2015.
 */
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;



public class Assignment3Server {


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(60025, 1);
        int stop = 0;
        try {
            while (stop == 0) {
                Socket socket = server.accept();

                try {

                    BufferedReader input =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String answer = input.readLine() + "\n" + input.readLine()
                            + "\n" + input.readLine() + "\n" + input.readLine()
                            + "\n" + input.readLine() + "\n" + input.readLine()
                            + "\n" + input.readLine() + "\n" + input.readLine();

                   System.out.printf(answer);

                }
                catch ( IOException ioException )
                {
                    System.out.println("\nError writing object");
                }
                finally {
                    socket.close();
                    stop++;
                }
            }
        }

        finally {
            server.close();
        }
    }
}