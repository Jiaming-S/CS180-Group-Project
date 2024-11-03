package Database;

import java.util.ArrayList;

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

  // Create entry given fields
  public UserEntry(String username, String password, int ID, ArrayList<Integer> friendList, ArrayList<Integer> blockList, String profilePicture, String region) {
    super();
    this.username = username;
    this.password = password;
    this.ID = ID;
    this.friendList = friendList;
    this.blockList = blockList;
    this.profilePicture = profilePicture;
    this.region = region;
  }

  // Create entry given XML
  public UserEntry(String xml) throws ParseExceptionXML {
    super(xml);
  }

  @Override
  protected void handleXML(String content, String curTag, String parentTag) {
    if (curTag.equals("ID") && parentTag.equals("Message")) {
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

  public boolean equals(Object o) {
    if (!(o instanceof UserEntry)) return false;
    UserEntry compare = (UserEntry) o;
    return (ID == compare.getID());
  }

  public static void main(String[] args) throws ParseExceptionXML {
    System.out.println(new UserEntry("<User>\t<Username>joebiden</Username>\n\t<Password>password123</Password><ID>77889900</ID><FriendList><ID>11223344</ID><ID>55667788</ID><ID>12345678</ID></FriendList><BlockList><ID>10101010</ID><ID>44444444</ID></BlockList><ProfilePicture>/path/to/image.png</ProfilePicture><Region>USA/Midwest</Region></User>"));
  }
}
