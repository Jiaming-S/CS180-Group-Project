import java.io.*;
import java.net.*;

import Database.*;
import Net.*;

public class ServerRunner {
	public static void main(String[] args) throws IOException {
        int portUDBS = 12345;
        int portMDBS = 12346;

        try {
            //create client access server
            UserDatabase userDatabase = new UserDatabase("user_db.txt");
            UserDatabaseServer userDatabaseServer = new UserDatabaseServer(
                new ServerSocket(portUDBS), 
                userDatabase
            );

            //create message access server
            MessageDatabase messageDatabase = new MessageDatabase("message_db.txt");
            MessageDatabaseServer messageDatabaseServer = new MessageDatabaseServer(
                new ServerSocket(portMDBS),
                messageDatabase
            );

            //begin servers
            Thread udbs = new Thread(userDatabaseServer);
            udbs.start();
            Thread mdbs = new Thread(messageDatabaseServer);
            mdbs.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
	}
}
