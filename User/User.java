package User;

import Database.UserEntry;

import java.util.ArrayList;

public class User {

    /**
     * User class with several constructors
     * @author Jane Bazzell
     * @version 11/02/2024
     */

    private String username;
    private String password;
    private int ID;
    private ArrayList<Integer> friendList;
    private ArrayList<Integer> blockList;
    private String profilePicture;
    private String region;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String username, String password, int ID) {
        this.username = username;
        this.password = password;
        this.ID = ID;
    }
    public User(String username, String password, int ID, ArrayList<Integer> friendList, ArrayList<Integer> blockList, String profilePicture, String region) {
        this.username = username;
        this.password = password;
        this.ID = ID;
        this.friendList = friendList;
        this.blockList = blockList;
        this.profilePicture = profilePicture;
        this.region = region;
    }
    public User (UserEntry entry) {
        this.username = entry.getUsername();
        this.password = entry.getPassword();
        this.ID = entry.getID();
        this.friendList = entry.getFriendList();
        this.blockList = entry.getBlockList();
        this.profilePicture = entry.getProfilePicture();
        this.region = entry.getRegion();

    }

    public User() {
        this.username = "test";
        this.password = "test";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Integer> getFriendList() {
        return friendList;
    }

    public ArrayList<Integer> getBlockList() {
        return blockList;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getRegion() {
        return region;
    }
}