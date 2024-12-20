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
    private String bio;
    private String privacyPreference;

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
    public User(String username, String password, int ID, ArrayList<Integer> friendList, ArrayList<Integer> blockList, String profilePicture, String region, String bio, String privacyPreference) {
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
    public User (UserEntry entry) {
        this.username = entry.getUsername();
        this.password = entry.getPassword();
        this.ID = entry.getID();
        this.friendList = entry.getFriendList();
        this.blockList = entry.getBlockList();
        this.profilePicture = entry.getProfilePicture();
        this.region = entry.getRegion();
        this.bio = entry.getBio();
        this.privacyPreference = entry.getPrivacyPreference();
    }

    public User() {
        this.username = "test";
        this.password = "test";
    }

    public UserEntry userToEntry() {
        return new UserEntry(username, password, ID, friendList, blockList, profilePicture, region, bio, privacyPreference);
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

    public void addBlockedUser(int user) {
        blockList.add(user);
    }

    public void removeBlockedUser(int user) {
        blockList.remove(Integer.valueOf(user));
    }

    public void addFriend(int user) {
        friendList.add(user);
    }

    public void removeFriend(int user) {
        friendList.remove(Integer.valueOf(user));
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getRegion() {
        return region;
    }

    public String getBio() {
        return bio;
    }
    
    public String getPrivacyPreference() {
        return privacyPreference;
    }

    public void setPrivacyPreference(String newP) {
        privacyPreference = newP;
    }

    public void setBio(String newBio) {
        bio = newBio;
    }

    public void setRegion(String newR) {
        region = newR;
    }
}