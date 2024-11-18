package User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import Net.*;
import Database.*;
import java.net.*;

import java.util.ArrayList;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A set of JUnit tests testing everything surrounding UserThreadTest
 * @author Haochen(Richard) Feng
 * @version 11/17/2024
 */
public class UserThreadTest {
    // Comprehensive test for the methods in UserThread
    @Test
    public void methodsTest() throws Exception {
        int port1 = 4242;
        int port2 = 2424;
        UserDatabaseServer udbserv = new UserDatabaseServer(
            new ServerSocket(port1),
            new UserDatabase("./Database/databaseTestFile.txt")
        );

        MessageDatabaseServer mdbserv = new MessageDatabaseServer(
            new ServerSocket(port2),
            new MessageDatabase("./Database/messageDatabaseTestFile.txt")
        );

        Thread userServerThread = new Thread(udbserv);
        userServerThread.start();

        Thread messageServerThread = new Thread(mdbserv);
        messageServerThread.start();

        Socket us = new Socket("127.0.0.1",port1);
        Socket ms = new Socket("127.0.0.1", port2);
        ObjectOutputStream uoos = new ObjectOutputStream(us.getOutputStream());
        ObjectOutputStream moos = new ObjectOutputStream(ms.getOutputStream());

        String newUserStr = """
            <User>
                <Username>name4</Username>
                <Password>password123</Password>
                <ID>00000004</ID>
                <FriendList>
                    <ID>11223344</ID>
                    <ID>55667788</ID>
                    <ID>12345678</ID>
                </FriendList>
                <BlockList>
                    <ID>10101010</ID>
                    <ID>44444444</ID>
                </BlockList>
                <ProfilePicture>/path/to/image.png</ProfilePicture>
                <Region>USA/Midwest</Region>
            </User>""".replaceAll(" ", "").replaceAll("\n", "");

        User tempUser = new User(new UserEntry(newUserStr));

        UserThread ut = new UserThread(tempUser, us, ms);

        uoos.writeObject(new Packet(
            null,
            null, 
            null
        ));

        moos.writeObject(new Packet(
            null,
            null, 
            null
        ));

        messageServerThread.join();
        userServerThread.join();
        us.close();
        ms.close();
    }
}
