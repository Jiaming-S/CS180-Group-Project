package Database;

import java.util.ArrayList;
import User.User;

/**
 * This class represents a UserEntry for use in the UserDatabase.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class UserEntry extends GenericEntry {
  private String username;
  private String password;
  private int ID;
  private ArrayList<Integer> friendList;
  private ArrayList<Integer> blockList;
  private String profilePicture;
  private String region;
  private String bio;
  private String privacyPreference;

  public static volatile int HIGHEST_ID = 0;
  public static synchronized int incrementAndGetID() {
    UserEntry.HIGHEST_ID++;
    return UserEntry.HIGHEST_ID;
  }

  // Create entry given fields
  public UserEntry(String username, String password, int ID, ArrayList<Integer> friendList, ArrayList<Integer> blockList, String profilePicture, String region, String bio, String privacyPreference) {
    super();
    this.username = username;
    this.password = password;
    this.ID = ID;
    this.friendList = friendList;
    this.blockList = blockList;
    this.profilePicture = profilePicture;
    this.region = region;
    this.bio = bio;
    this.privacyPreference = privacyPreference;
  }

  public UserEntry(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.ID = user.getID();
    this.friendList = user.getFriendList();
    this.blockList = user.getBlockList();
    this.profilePicture = user.getProfilePicture();
    this.region = user.getRegion();
    this.bio = user.getBio();
    this.privacyPreference = user.getPrivacyPreference();
  }

  // Create entry given XML
  public UserEntry(String xml) throws ParseExceptionXML {
    super(xml);
    if (this.friendList == null) this.friendList = new ArrayList<>();
    if (this.blockList == null)  this.blockList = new ArrayList<>();
  }

  @Override
  protected void handleXML(String content, String curTag, String parentTag) {
    if (curTag.equals("ID") && parentTag.equals("User")) {
      this.ID = Integer.parseInt(content);
    } else if (curTag.equals("Username")) {
      this.username = content;
    } else if (curTag.equals("Password")) {
      this.password = content;
    } else if (curTag.equals("ID") && parentTag.equals("FriendList")) {
      if (this.friendList == null) this.friendList = new ArrayList<>();
      this.friendList.add(Integer.parseInt(content));
    } else if (curTag.equals("ID") && parentTag.equals("BlockList")) {
      if (this.blockList == null) this.blockList = new ArrayList<>();
      this.blockList.add(Integer.parseInt(content));
    } else if (curTag.equals("ProfilePicture")) {
      this.profilePicture = content;
    } else if (curTag.equals("Region")) {
      this.region = content;
    } else if (curTag.equals("Bio")) {
      this.bio = content;
    } else if (curTag.equals("PrivacyPreference")) {
      this.privacyPreference = content;
    }
  }

  @Override
  public String toString() {
    String result = "";
    result += "<User>\n";

    result += String.format("\t<Username>%s</Username>\n", this.username);

    result += String.format("\t<Password>%s</Password>\n", this.password);

    result += String.format("\t<ID>%d</ID>\n", this.ID);

    result += "\t<FriendList>\n";
    for (int ID : this.friendList) result += String.format("\t\t<ID>%d</ID>\n", ID);
    result += "\t</FriendList>\n";
    
    result += "\t<BlockList>\n";
    for (int ID : this.blockList) result += String.format("\t\t<ID>%d</ID>\n", ID);
    result += "\t</BlockList>\n";

    result += String.format("\t<ProfilePicture>%s</ProfilePicture>\n", this.profilePicture);

    result += String.format("\t<Region>%s</Region>\n", this.region);

    result += String.format("\t<Bio>%s</Bio>\n", this.bio);

    result += String.format("\t<PrivacyPreference>%s</PrivacyPreference>\n", this.privacyPreference);

    result += "</User>\n";

    return result;
  }

  public String getUsername() {
    return this.username;
  }
  public String getPassword() {
    return this.password;
  }
  public int getID() {
    return this.ID;
  }
  public ArrayList<Integer> getFriendList() {
    return this.friendList;
  }
  public ArrayList<Integer> getBlockList() {
    return this.blockList;
  }
  public String getProfilePicture() {
    return this.profilePicture;
  }
  public String getRegion() {
    return this.region;
  }
  public String getBio() {
    return this.bio;
  }
  public String getPrivacyPreference() {
    return this.privacyPreference;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public boolean equals(Object o) {
    if (!(o instanceof UserEntry)) return false;
    UserEntry compare = (UserEntry) o;
    return (ID == compare.getID());
  }
}
