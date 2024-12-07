/*
package User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import Net.*;
import Database.*;
import java.net.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

*/
/**
 * A set of JUnit tests testing everything surrounding UserThreadTest
 * @author Haochen(Richard) Feng
 * @version 11/17/2024
 *//*

public class UserThreadTest {
    // Comprehensive test for the methods in UserThread
    @Test
    public void methodsTest() throws Exception {
        UserDatabase userDatabase;
        MessageDatabase messageDatabase;

        UserThread userThread = null;
        User currentUser = null;


        String hostName = "localhost";
        int portUDBS = 5555;
        int portMDBS = 6666;


        Socket socket, messageSocket;
        ObjectOutputStream oos, moos;
        ObjectInputStream ois, mois;

        try {
            userDatabase = new UserDatabase("user_db.txt");
            UserDatabaseServer userDatabaseServer = new UserDatabaseServer(
                new ServerSocket(portUDBS), 
                userDatabase
            );

            messageDatabase = new MessageDatabase("message_db.txt");
            MessageDatabaseServer messageDatabaseServer = new MessageDatabaseServer(
                new ServerSocket(portMDBS),
                messageDatabase
            );

            Thread udbs = new Thread(userDatabaseServer);
            udbs.start();

            Thread mdbs = new Thread(messageDatabaseServer);
            mdbs.start(); // note: probably don't need to do anything with message db in runner, good to just start it tho

            socket = new Socket(hostName, portUDBS);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            messageSocket = new Socket(hostName, portMDBS);
            moos = new ObjectOutputStream(messageSocket.getOutputStream());
            mois = new ObjectInputStream(messageSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

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

        User tempUser = new User("name4", "password123");

        System.out.println("Here");
        
        try {
            userThread = new UserThread(tempUser, oos, ois, moos, mois);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
                    
        if (userThread != null) {
            userThread.start();
        } try {
            if (userThread != null) userThread.join();
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();

            if (mois != null) mois.close();
            if (moos != null) moos.close();
            if (messageSocket != null) messageSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
