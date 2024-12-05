import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Database.*;
import Net.*;

public class ServerRunner {
	public static void main(String[] args) throws IOException {
        final int portUDBS = 12345;
        final int portMDBS = 12346;

        UserDatabase userDatabase = new UserDatabase("user_db.txt");
        MessageDatabase messageDatabase = new MessageDatabase("message_db.txt");

        ArrayList<Thread> threadPool = new ArrayList<>();

        ServerSocket userDBServerListener = new ServerSocket(portUDBS);
        ServerSocket messageDBServerListener = new ServerSocket(portMDBS);

        int errorThreshold = 10;
        while (true) {
            try {
                Socket clientUDBS = userDBServerListener.accept();
                Thread threadUDBS = new Thread(new UserDatabaseServer(clientUDBS, userDatabase));
                threadUDBS.start();
                threadPool.add(threadUDBS);

                Socket clientMDBS = messageDBServerListener.accept();
                Thread threadMDBS = new Thread(new MessageDatabaseServer(clientMDBS, messageDatabase));
                threadMDBS.start();
                threadPool.add(threadMDBS);
            } catch (Exception e) {
                e.printStackTrace();
                if (--errorThreshold == 0) break;
            }
        }

        userDBServerListener.close();
        messageDBServerListener.close();
	}
}
