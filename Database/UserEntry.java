package Database;

import java.util.ArrayList;

public class UserEntry extends GenericEntry {
  private int ID;
  private ArrayList<Integer> friendList;
  private ArrayList<Integer> blockList;
  private String profilePicture;
  private String region;


  public UserEntry(String s) throws ParseExceptionXML {
    super(s);
    this.ID = 0;
    this.friendList = new ArrayList<>();
    this.blockList = new ArrayList<>();
    this.profilePicture = null;
    this.region = null;
  }

  @Override
  protected void handleXML(String content, String curTag, String parentTag) {
    if (curTag.equals("ID") && parentTag.equals("User")) {
      this.ID = Integer.parseInt(content);
    } else if (curTag.equals("ID") && parentTag.equals("FriendList")) {
      this.friendList.add(Integer.parseInt(content));
    } else if (curTag.equals("ID") && parentTag.equals("BlockList")) {
      this.blockList.add(Integer.parseInt(content));
    } else if (curTag.equals("ProfilePicture")) {
      this.profilePicture = content;
    } else if (curTag.equals("Region")) {
      this.region = content;
    }
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
}
