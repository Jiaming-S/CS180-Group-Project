package User;

import Database.UserEntry;

import javax.swing.*;
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
    private ImageIcon profilePicture;
    private String region;
    private String bio;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.ID = 0;
    }
    public User(String username, String password, int ID) {
        this.username = username;
        this.password = password;
        this.ID = ID;
    }
    public User(String username, String password, int ID, ArrayList<Integer> friendList, ArrayList<Integer> blockList, ImageIcon profilePicture, String region, String bio) {
        this.username = username;
        this.password = password;
        this.ID = ID;
        this.friendList = friendList;
        this.blockList = blockList;
        this.profilePicture = profilePicture;
        this.region = region;
        this.bio = bio;
    }
    public User (UserEntry entry) {
        this.username = entry.getUsername();
        this.password = entry.getPassword();
        this.ID = entry.getID();
        this.friendList = entry.getFriendList();
        this.blockList = entry.getBlockList();
        this.profilePicture = entry.getProfilePicture();
        this.region = entry.getRegion();
        this.bio = bio;
    }

    public User() {
        this.username = "test";
        this.password = "test";
    }

    public UserEntry userToEntry() {
        return new UserEntry(username, password, ID, friendList, blockList, profilePicture, region, bio);
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

    public ImageIcon getProfilePicture() {
        return profilePicture;
    }

    public String getRegion() {
        return region;
    }

    public String getBio() {
        return bio;
    }
}