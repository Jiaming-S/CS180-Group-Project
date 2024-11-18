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
        UserDatabaseServer udbserv = new UserDatabaseServer(
            new ServerSocket(12345),
            new UserDatabase("./Database/databaseTestFile.txt")
        );

        MessageDatabaseServer mdbserv = new MessageDatabaseServer(
            new ServerSocket(12346),
            new MessageDatabase("./Database/messageDatabaseTestFile.txt")
        );

        Socket us = new Socket("127.0.0.1",12345);
        Socket ms = new Socket("127.0.0.1", 12346);
        ObjectOutputStream uoos = new ObjectOutputStream(us.getOutputStream());
        ObjectOutputStream moos = new ObjectOutputStream(ms.getOutputStream());

        Thread userServerThread = new Thread(udbserv);
        userServerThread.start();

        Thread messageServerThread = new Thread(mdbserv);
        messageServerThread.start();

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
