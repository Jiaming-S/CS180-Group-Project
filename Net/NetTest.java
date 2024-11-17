package Net;


import java.io.*;
import java.net.*;

import Database.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.ArrayList;

/**
 * A set of JUnit tests testing everything surrounding the network side e.g. servers for users and messages, packets, etc.
 * @author Haochen(Richard) Feng, with network code copied from NetowkrTest.java
 * @version 11/16/2024
 */
public class NetTest {

    // basic tests for UserDatabaseServer
    @Test
    public void networkTestUser() {
        try {
            UserDatabaseServer udbserv = new UserDatabaseServer(
                new ServerSocket(12345),
                new UserDatabase("./Database/databaseTestFile.txt")
            );

            Thread serverThread = new Thread(udbserv);
            serverThread.start();

            Socket client = new Socket("127.0.0.1", 12345);
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois  = new ObjectInputStream(client.getInputStream());

            String userStr = """
                <User>
                    <Username>joebiden</Username>
                    <Password>password123</Password>
                    <ID>00000001</ID>
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
                </User> """.replaceAll(" ", "").replaceAll("\n", "");

            UserEntry ue = new UserEntry(userStr);

            oos.writeObject(new Packet(
                "searchByName",
                "joebiden", 
                null
            ));
            Packet received = (Packet) ois.readObject();
            assertEquals("searchByName network query returns the wrong value!", received.content, ue);


            oos.writeObject(new Packet(
                "searchByID",
                1, 
                null
            ));
            received = (Packet) ois.readObject();
            assertEquals("searchByID network query returns the wrong value!", received.content, ue);


            oos.writeObject(new Packet(
                "getEntry",
                0, 
                null
            ));
            received = (Packet) ois.readObject();
            assertEquals("getEntry network query returns the wrong value!", received.content, ue);


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
            UserEntry nue = new UserEntry(newUserStr);


            oos.writeObject(new Packet(
                "insertEntry",
                nue, 
                null
            ));
            received = (Packet) ois.readObject();
            assertEquals("insertEntry network query returns false!", received.content, true);

            oos.writeObject(new Packet(
                null,
                null, 
                null
            ));

            serverThread.join();
            client.close();
        } catch (IOException e) {
            fail("Encountered IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("Encountered ClassNotFoundException: " + e.getMessage());
        } catch (InterruptedException e) { 
            fail("Encountered InterruptedException: " + e.getMessage());
        } catch (ParseExceptionXML e) {
            fail("Encountered ParseExceptionXML: " + e.getMessage());
        }
    }

    //testing network database for messages
    @Test
    public void networkTestMessages() {
        try {
            MessageDatabaseServer mdbserv = new MessageDatabaseServer(
                new ServerSocket(12345),
                new MessageDatabase("./Database/messageDatabaseTestFile.txt")
            );

            Thread serverThread = new Thread(mdbserv);
            serverThread.start();

            Socket client = new Socket("127.0.0.1", 12345);
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois  = new ObjectInputStream(client.getInputStream());

            String messageStr = """
                <Message>
                    <Timestamp>02/11/2024</Timestamp>
                    <Sender>
                        <ID>12345678</ID>
                    </Sender>
                    <Recipient>
                        <ID>77889900</ID>
                    </Recipient>
                    <Content>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</Content>
                </Message>""".replaceAll(" ", "").replaceAll("\n", "");
            MessageEntry me = new MessageEntry(messageStr);

            oos.writeObject(new Packet(
                "searchFirstByRecipientID",
                77889900, 
                null
            ));
            Packet received = (Packet) ois.readObject();
            assertEquals("searchFirstByRecipientID network query returns the wrong value!", me.toString(), received.content.toString());

            String messageStr2 = """
                <Message>
                    <Timestamp>02/12/2024</Timestamp>
                    <Sender>
                        <ID>12345678</ID>
                    </Sender>
                    <Recipient>
                        <ID>77889900</ID>
                    </Recipient>
                    <Content>blocked</Content>
                    </Message>
                <Message>""".replaceAll(" ", "").replaceAll("\n", "");
            MessageEntry me2 = new MessageEntry(messageStr2);

            ArrayList<MessageEntry> messageArr = new ArrayList<>();
            messageArr.add(me);
            messageArr.add(me2);

            oos.writeObject(new Packet(
                "searchAllByRecipientID",
                77889900, 
                null
            ));
            received = (Packet) ois.readObject();

            ArrayList<MessageEntry> resArr = (ArrayList<MessageEntry>) received.content;

            assertEquals("searchAllByRecipientID network query returns the wrong number of entries!", messageArr.size(), resArr.size());
            assertEquals("searchAllByRecipientID network query returns the wrong contents!", messageArr.get(0).toString() + "<compareBlock>" + messageArr.get(1).toString(), resArr.get(0).toString() + "<compareBlock>" + resArr.get(1).toString());

            oos.writeObject(new Packet(
                "searchFirstBySenderID",
                12345678, 
                null
            ));
            received = (Packet) ois.readObject();

            assertEquals("searchFirstBySenderID network query returns the wrong value!", me.toString(), received.content.toString());

            oos.writeObject(new Packet(
                "searchAllBySenderID",
                12345678, 
                null
            ));
            received = (Packet) ois.readObject();

            resArr = (ArrayList<MessageEntry>) received.content;

            assertEquals("searchAllBySenderID network query returns the wrong number of entries!", messageArr.size(), resArr.size());
            assertEquals("searchAllBySenderID network query returns the wrong contents!", messageArr.get(0).toString() + "<compareBlock>" + messageArr.get(1).toString(), resArr.get(0).toString() + "<compareBlock>" + resArr.get(1).toString());

            oos.writeObject(new Packet(
                null,
                null, 
                null
            ));

            serverThread.join();
            client.close();
        } catch (IOException e) {
            fail("Encountered IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("Encountered ClassNotFoundException: " + e.getMessage());
        } catch (InterruptedException e) { 
            fail("Encountered InterruptedException: " + e.getMessage());
        } catch (ParseExceptionXML e) {
            fail("Encountered ParseExceptionXML: " + e.getMessage());
        }
    }
}
