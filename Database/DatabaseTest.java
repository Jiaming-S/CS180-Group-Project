
package Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.ArrayList;
import java.io.*;

import Message.*;


/**
 * A set of JUnit tests testing everything surrounding Database
 * @author Haochen(Richard) Feng
 * @version 11/16/2024
 */

public class DatabaseTest {

    // Test for message content by itself
    @Test
    public void initialTestContentUser() {
        String userStr = """
            <User>
                <Username>joebiden</Username>
                <Password>password123</Password>
                <ID>77889900</ID>
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
                <Bio>BidenBlast</Bio>
                <PrivacyPreference>All</PrivacyPreference>
            </User>""".replaceAll(" ", "").replaceAll("\n", "");
        UserEntry ue;
        try {
            ue = new UserEntry(userStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("ToString should return the XML used to initiate UserEntry", userStr, ue.toString().replaceAll("\n", "").replace("\t", ""));
    }

    // Test for formatting
    @Test
    public void initialTestFormatUser() {
        String userStr = "<User>\n\t<Username>joebiden</Username>\n\t<Password>password123</Password>\n\t<ID>77889900</ID>\n\t<FriendList>\n\t\t<ID>11223344</ID>\n\t\t<ID>55667788</ID>\n\t\t<ID>12345678</ID>\n\t</FriendList>\n\t<BlockList>\n\t\t<ID>10101010</ID>\n\t\t<ID>44444444</ID>\n\t</BlockList>\n\t<ProfilePicture>/path/to/image.png</ProfilePicture>\n\t<Region>USA/Midwest</Region>\n\t<Bio>BidenBlast</Bio>\n\t<PrivacyPreference>All</PrivacyPreference>\n</User>\n";
        UserEntry ue;
        try {
            ue = new UserEntry(userStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("ToString should return the XML used to initiate UserEntry", userStr, ue.toString());
    }

    //test getter methods
    @Test
    public void testGettersUser() {
        String userStr = """
            <User>
                <Username>joebiden</Username>
                <Password>password123</Password>
                <ID>77889900</ID>
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
                <Bio>BidenBlast</Bio>
                <PrivacyPreference>All</PrivacyPreference>
            </User>""".replaceAll(" ", "").replaceAll("\n", "");
        String username = "joebiden";

        String password = "password123";

        int userID = 77889900;

        ArrayList<Integer> friendList = new ArrayList<>();
        friendList.add(11223344);
        friendList.add(55667788);
        friendList.add(12345678);

        ArrayList<Integer> blockList = new ArrayList<>();
        blockList.add(10101010);
        blockList.add(44444444);

        String profilePicture = "/path/to/image.png";

        String region = "USA/Midwest";

        String bio = "BidenBlast";

        String privacyPreference = "All";

        UserEntry ue;
        try {
            ue = new UserEntry(userStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("getUsername returns wrong string!", username, ue.getUsername());
        assertEquals("getPassword returns wrong string!", password, ue.getPassword());
        assertEquals("getID returns wrong value!", userID, ue.getID());
        assertEquals("getFriendList returns wrong values!", friendList, ue.getFriendList());
        assertEquals("getBlockList returns wrong values!", blockList, ue.getBlockList());
        assertEquals("getProfilePicture returns wrong string!", profilePicture, ue.getProfilePicture());
        assertEquals("getRegion returns wrong value!", region, ue.getRegion());
        assertEquals("getBio returns wrong value!", bio, ue.getBio());
        assertEquals("getPrivacyPreference returns wrong value!", privacyPreference, ue.getPrivacyPreference());
    }

    //test searchers;
    @Test
    public void testSearchersUser() {
        UserDatabase ud;
        try {
            ud = new UserDatabase("./Database/databaseTestFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occured while reading test file: " + e.getMessage());
            return;
        }
        assertEquals("searchByName returns wrong user!", 1, ud.searchByName("joebiden").getID());
        assertEquals("searchByID returns wrong user!", "name3", ud.searchByID(4).getUsername());
    }

    @Test
    public void testSettersGettersUser() {
        String tocopy = "./Database/copyDatabaseTestFile.txt";
        String tosave = "./Database/databaseTestFile.txt";

        try (FileWriter writer = new FileWriter(tosave)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(tocopy));
            PrintWriter pw = new PrintWriter(new FileWriter(tosave))) {
            String line = bfr.readLine();
            while (line != null) {
                pw.println(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tocopy = "./Database/copyMessageDatabaseTestFile.txt";
        tosave = "./Database/messageDatabaseTestFile.txt";

        try (FileWriter writer = new FileWriter(tosave)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(tocopy));
            PrintWriter pw = new PrintWriter(new FileWriter(tosave))) {
            String line = bfr.readLine();
            while (line != null) {
                pw.println(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDatabase ud;
        try {
            ud = new UserDatabase("./Database/databaseTestFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occured while reading test file: " + e.getMessage());
            return;
        }
        String userStr = """
            <User>
                <Username>name3</Username>
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
                <Bio>3name</Bio>
                <PrivacyPreference>All</PrivacyPreference>
            </User>""".replaceAll(" ", "").replaceAll("\n", "");
        UserEntry ue;
        try {
            ue = new UserEntry(userStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("getEntry for user database returns wrong value!", ue, ud.getEntry(3));

        String newUserStr = """
            <User>
                <Username>name4</Username>
                <Password>password123</Password>
                <ID>00000021</ID>
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
                <Bio>4name</Bio>
                <PrivacyPreference>All</PrivacyPreference>
            </User>""".replaceAll(" ", "").replaceAll("\n", "");
        UserEntry nue;
        try {
            nue = new UserEntry(newUserStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        ud.insertEntry(nue);
        assertEquals("insertEntry for user database doesn't work as expected!", nue, ud.getEntry(4));
    }

    // Test for message content by itself
    @Test
    public void initialTestContentMessages() {
        String messageStr = """
            <Message>
                <Timestamp>2023-12-07T18:59:59.216152800</Timestamp>
                <Sender>
                    <ID>12345678</ID>
                </Sender>
                <Recipient>
                    <ID>77889900</ID>
                </Recipient>
                <Content>TextMessage|12345678|77889900|2023-12-07T18:59:59.216152800|Lorem ipsum dolor sit amet, consectetur adipiscing elit.</Content>
            </Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry me;
        try {
            me = new MessageEntry(messageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("ToString should return the content used to initiate MessageEntry", messageStr, me.toString().replaceAll("\n", "").replace("\t", ""));
    }

    // Test for formatting
    @Test
    public void initialTestFormatMessages() {
        String messageStr = "<Message>\n\t<Timestamp>2023-12-07T18:59:59.216152800</Timestamp>\n\t<Sender>\n\t\t<ID>12345678</ID>\n\t</Sender>\n\t<Recipient>\n\t\t<ID>77889900</ID>\n\t</Recipient>\n\t<Content>TextMessage|12345678|77889900|2023-12-07T18:59:59.216152800|frfrsotrueong.</Content>\n</Message>\n";
        MessageEntry me;
        try {
            me = new MessageEntry(messageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }
        assertEquals("ToString should return the correct format used to initiate MessageEntry", messageStr, me.toString());
    }

    //test get methods for messages
    @Test
    public void testGettersMessages() {
        String messageStr = """
            <Message>
                <Timestamp>2023-12-07T18:59:59.216152800</Timestamp>
                <Sender>
                    <ID>12345678</ID>
                </Sender>
                <Recipient>
                    <ID>77889900</ID>
                </Recipient>
                <Content>TextMessage|12345678|77889900|2023-12-07T18:59:59.216152800|frfrsotrueong.</Content>
            </Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry me;
        try {
            me = new MessageEntry(messageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        String timestamp = "2023-12-07T18:59:59.216152800";

        int sender = 12345678;

        int recipient = 77889900;

        String content = "TextMessage|12345678|77889900|2023-12-07T18:59:59.216152800|frfrsotrueong.";

        assertEquals("getTimestamp returns the wrong string!", timestamp, me.getTimestamp());
        assertEquals("getSender returns the wrong ID!", sender, me.getSender());
        assertEquals("getRecipient returns the wrong ID!", recipient, me.getRecipient());
        assertEquals("getContent returns the wrong string!", content, ((TextMessage)(me.getContent())).toString());
    }

    //test setters and getters for messageDabatase
    @Test
    public void testSettersGettersMessages() {
        String tocopy = "./Database/copyDatabaseTestFile.txt";
        String tosave = "./Database/databaseTestFile.txt";

        try (FileWriter writer = new FileWriter(tosave)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(tocopy));
            PrintWriter pw = new PrintWriter(new FileWriter(tosave))) {
            String line = bfr.readLine();
            while (line != null) {
                pw.println(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tocopy = "./Database/copyMessageDatabaseTestFile.txt";
        tosave = "./Database/messageDatabaseTestFile.txt";

        try (FileWriter writer = new FileWriter(tosave)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(tocopy));
            PrintWriter pw = new PrintWriter(new FileWriter(tosave))) {
            String line = bfr.readLine();
            while (line != null) {
                pw.println(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageDatabase md;
        try {
            md = new MessageDatabase("./Database/messageDatabaseTestFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occured while reading test file: " + e.getMessage());
            return;
        }

        String messageStr = """
            <Message>
                <Timestamp>2023-12-07T18:59:59.216152800</Timestamp>
                <Sender>
                    <ID>88888888</ID>
                </Sender>
                <Recipient>
                    <ID>99999999</ID>
                </Recipient>
                <Content>TextMessage|88888888|99999999|2023-12-07T18:59:59.216152800|Content</Content>
            </Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry me;
        try {
            me = new MessageEntry(messageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        assertEquals("getEntry for message database returns the wrong value!", me.toString(), md.getEntry(3).toString());

        String newMessageStr = """
            <Message>
                <Timestamp>2023-12-07T18:59:59.216152800</Timestamp>
                <Sender>
                    <ID>99999999</ID>
                </Sender>
                <Recipient>
                    <ID>10101010</ID>
                </Recipient>
                <Content>TextMessage|99999999|10101010|2023-12-07T18:59:59.216152800|Content</Content>
            </Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry nme;
        try {
            nme = new MessageEntry(newMessageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        md.insertEntry(nme);

        assertEquals("insertEntry for message database doesn't work properly!", nme.toString(), md.getEntry(4).toString());
    }

    //test search methods for MessageDatabase
    @Test
    public void testSearchersMessages() {
        MessageDatabase md;
        try {
            md = new MessageDatabase("./Database/messageDatabaseTestFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occured while reading test file: " + e.getMessage());
            return;
        }

        String messageStr = """
            <Message>
                <Timestamp>2023-12-07T18:58:55.216152800</Timestamp>
                <Sender>
                    <ID>12345678</ID>
                </Sender>
                <Recipient>
                    <ID>77889900</ID>
                </Recipient>
                <Content>TextMessage|12345678|77889900|2023-12-07T18:58:55.216152800|Lorem ipsum dolor sit amet, consectetur adipiscing elit.</Content>
            </Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry me;
        try {
            me = new MessageEntry(messageStr);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        MessageEntry res = md.searchFirstByRecipientID(77889900);

        assertEquals("searchFirstByRecipientID returns the wrong value!", me.toString(), res.toString().replace(" ", ""));

        String messageStr2 = """
            <Message>
                <Timestamp>2023-12-07T18:59:55.216152800</Timestamp>
                <Sender>
                    <ID>12345678</ID>
                </Sender>
                <Recipient>
                    <ID>77889900</ID>
                </Recipient>
                <Content>TextMessage|12345678|77889900|2023-12-07T18:59:55.216152800|blocked</Content>
                </Message>
            <Message>""".replaceAll(" ", "").replaceAll("\n", "");
        MessageEntry me2;
        try {
            me2 = new MessageEntry(messageStr2);
        } catch (ParseExceptionXML e) {
            e.printStackTrace();
            fail("Exception occurred while parsing XML: " + e.getMessage());
            return;
        }

        ArrayList<MessageEntry> messageArr = new ArrayList<>();
        messageArr.add(me);
        messageArr.add(me2);

        ArrayList<MessageEntry> resArr = md.searchAllByRecipientID(77889900);
        assertEquals("searchAllByRecipientID returns the wrong number of entries!", messageArr.size(), resArr.size());
        assertEquals("searchAllByRecipientID returns the wrong contents!", messageArr.get(0).toString().replaceAll(" ", "") + "<compareBlock>" + messageArr.get(1).toString().replaceAll(" ", ""), resArr.get(0).toString().replaceAll(" ", "") + "<compareBlock>" + resArr.get(1).toString().replaceAll(" ", ""));

        res = md.searchFirstBySenderID(12345678);

        assertEquals("searchFirstBySenderID returns the wrong value!", me.toString().replaceAll(" ", ""), res.toString().replaceAll(" ", ""));

        resArr = md.searchAllBySenderID(12345678);
        assertEquals("searchAllBySenderID returns the wrong number of entries!", messageArr.size(), resArr.size());
        assertEquals("searchAllBySenderID returns the wrong contents!", messageArr.get(0).toString().replaceAll(" ", "") + "<compareBlock>" + messageArr.get(1).toString().replaceAll(" ", ""), resArr.get(0).toString().replaceAll(" ", "") + "<compareBlock>" + resArr.get(1).toString().replaceAll(" ", ""));
    }
}

