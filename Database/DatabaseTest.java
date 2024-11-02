package Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.util.ArrayList;

/**
 * A set of JUnit tests testing everything surrounding Database
 * @author Haochen(Richard) Feng
 * @version 11/02/2024
 */
public class DatabaseTest {

    // Test for message content by itself
    @Test
    public void initialTestContent() {
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
    public void initialTestFormat() {
        String userStr = "<User>\n\t<Username>joebiden</Username>\n\t<Password>password123</Password>\n\t<ID>77889900</ID>\n\t<FriendList>\n\t\t<ID>11223344</ID>\n\t\t<ID>55667788</ID>\n\t\t<ID>12345678</ID>\n\t</FriendList>\n\t<BlockList>\n\t\t<ID>10101010</ID>\n\t\t<ID>44444444</ID>\n\t</BlockList>\n\t<ProfilePicture>/path/to/image.png</ProfilePicture>\n\t<Region>USA/Midwest</Region>\n</User>\n";
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

    @Test
    public void testGetters() {
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
    }
}
