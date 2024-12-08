
package User;

import Database.MessageEntry;
import Database.UserEntry;
import Message.*;
import Net.Packet;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;


/**
 * UserThread class with several methods related to User actions and will work with GUI
 * (Some methods may need to be altered during Phase 3 to work with GUI)
 * @author Nikita Sirandasu
 * @version 11/17/2024
 */
public class UserThread extends Thread implements UserThreadInt {
    private User currUser;
    private ObjectOutputStream userOut;
    private ObjectInputStream userIn;
    private ObjectOutputStream msgOut;
    private ObjectInputStream msgIn;

    private JFrame frame;
    JButton searchButton;
    JTextField searchField;
    private final Object lock = new Object();
    private Scanner scanner;

    private JButton friendButton;
    private JButton blockButton;
    private JButton messageButton;

    public UserThread(User currUser, ObjectOutputStream userOut, ObjectInputStream userIn, ObjectOutputStream msgOut, ObjectInputStream msgIn, JFrame Frame) throws IOException, ClassNotFoundException {
        this.currUser = currUser;
        this.userOut = userOut;
        this.userIn = userIn;
        this.msgOut = msgOut;
        this.msgIn = msgIn;
        this.scanner = new Scanner(System.in);
        this.frame = frame;
    }

    public void searchUser(String username) {
        synchronized (lock) {
            //sends packet to databaseserver to get matching userEntry of username
            Packet packet = new Packet("searchByName", username, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject(); //receive packet with UserEntry info from databaseserver
                if (response.query.equals("success")) { //query == "success" if user found, "failure" if anything else occurs.
                    UserEntry userToFind = (UserEntry) response.content;
                    User searchedUser = new User(userToFind);
                    JOptionPane.showMessageDialog(null, String.format("%s\n", "User found: " + userToFind.getUsername()), "Successful", JOptionPane.INFORMATION_MESSAGE);
                    int input = JOptionPane.showConfirmDialog(null, "Do you want to view this user's profile?", "Successful", JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.YES_OPTION) {
                        ProfilePage profilePage = new ProfilePage(this, userToFind);
                        profilePage.viewProfile();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User was not found", "Failure", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when searching user.");
                JOptionPane.showMessageDialog(null, "Error occurred during search", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editProfile() {
        synchronized (lock) {
            String newBio = JOptionPane.showInputDialog(null, "Enter your new bio", currUser.getBio());
            String newRegion = JOptionPane.showInputDialog(null, "Enter your new location", currUser.getRegion());
            currUser.setBio(newBio);
            currUser.setRegion(newRegion);
            Packet packet = new Packet("updateEntry", new UserEntry(currUser), null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    JOptionPane.showMessageDialog(null, "Bio edited", "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Edit failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when blocking user.");
                JOptionPane.showMessageDialog(null, "Error when editing", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void blockUser(String blockedUsername) {
        synchronized (lock) {
            Packet getUserPacket = new Packet("searchByName", blockedUsername, null);
            try {
                userOut.writeObject(getUserPacket);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry blocked = (UserEntry) response.content;
                    currUser.addBlockedUser(blocked.getID()); //add to the userentry's arraylist of blocked users
                    //sends packet with the new user's info to server to be written to the database.
                    Packet blockPacket = new Packet("updateEntry", new UserEntry(currUser), null);
                    try {
                        userOut.writeObject(blockPacket);
                        Packet resp = (Packet) userIn.readObject();
                        boolean success = resp.getQuery().equals("success"); //checks if the user was successfully written to database. throws exception if not.
                        if (!success) {
                            System.out.println("Error writing to database.");
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Blocked user: " + blocked.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error when blocking user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void unblockUser(String unBUsername) {
        synchronized (lock) {
            Packet getUserPacket = new Packet("searchByName", unBUsername, null);
            try {
                userOut.writeObject(getUserPacket);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry unB = (UserEntry) response.content;
                    currUser.removeBlockedUser(unB.getID()); //add to the userentry's arraylist of blocked users
                    //sends packet with the new user's info to server to be written to the database.
                    Packet blockPacket = new Packet("updateEntry", new UserEntry(currUser), null);
                    try {
                        userOut.writeObject(blockPacket);
                        Packet resp = (Packet) userIn.readObject();
                        boolean success = resp.getQuery().equals("success"); //checks if the user was successfully written to database. throws exception if not.
                        if (!success) {
                            System.out.println("Error writing to database.");
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Unblocked user: " + unB.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error when unblocking user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void friendUser(String friendUsername) {
        synchronized (lock) {
            Packet getUserPacket = new Packet("searchByName", friendUsername, null);
            try {
                userOut.writeObject(getUserPacket);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry friend = (UserEntry) response.content;
                    currUser.addFriend(friend.getID()); //add to the userentry's arraylist of blocked users
                    //sends packet with the new user's info to server to be written to the database.
                    Packet friendPacket = new Packet("updateEntry", new UserEntry(currUser), null);
                    try {
                        userOut.writeObject(friendPacket);
                        Packet resp = (Packet) userIn.readObject();
                        boolean success = resp.getQuery().equals("success"); //checks if the user was successfully written to database. throws exception if not.
                        if (!success) {
                            System.out.println("Error writing to database.");
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Friended user: " + friend.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error when friending user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void unfriendUser(String unFUsername) {
        synchronized (lock) {
            Packet getUserPacket = new Packet("searchByName", unFUsername, null);
            try {
                userOut.writeObject(getUserPacket);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry unF = (UserEntry) response.content;
                    currUser.removeFriend(unF.getID()); //add to the userentry's arraylist of blocked users
                    //sends packet with the new user's info to server to be written to the database.
                    Packet friendPacket = new Packet("updateEntry", new UserEntry(currUser), null);
                    try {
                        userOut.writeObject(friendPacket);
                        Packet resp = (Packet) userIn.readObject();
                        boolean success = resp.getQuery().equals("success"); //checks if the user was successfully written to database. throws exception if not.
                        if (!success) {
                            System.out.println("Error writing to database.");
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Unfriended user: " + unF.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error when unfriending user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void newConvo() {
        synchronized (lock) {
            String username = JOptionPane.showInputDialog(null, "Enter username to start a conversation with");
            Packet packet = new Packet("searchByName", username, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipientEntry = (UserEntry) response.content;
                    System.out.println("Starting a conversation with " + recipientEntry.getUsername());
                    JOptionPane.showMessageDialog(null, "Starting a conversation with: " + recipientEntry.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("User not found.");
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error occurred when starting conversation", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void viewMsg() {
        //must be implemented after GUI is created
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MessageEntry> getAllMessagesFromUser(int ID) {
        ArrayList<MessageEntry> result = null;
        Packet packet = new Packet("searchAllBySenderID", ID, null);
        try {
            msgOut.writeObject(packet);
            Packet response = (Packet) msgIn.readObject();
            if (response.query.equals("success")) {
                result = (ArrayList<MessageEntry>) response.content;
                return result;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to find any messages from " + ID, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred when searching messages from " + ID, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MessageEntry> getAllMessagesToUser(int ID) {
        ArrayList<MessageEntry> result = null;
        Packet packet = new Packet("searchAllByRecipientID", ID, null);
        try {
            msgOut.writeObject(packet);
            Packet response = (Packet) msgIn.readObject();
            if (response.query.equals("success")) {
                result = (ArrayList<MessageEntry>) response.content;
                return result;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to find any messages from id: " + ID, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred when searching messages from id: " + ID, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public void deleteMessageEntry(MessageEntry me) {
        Packet packet = new Packet("deleteEntry", me, null);
        try {
            msgOut.writeObject(packet);
            Packet response = (Packet) msgIn.readObject();
            if (!response.query.equals("success")) {
                JOptionPane.showMessageDialog(null, "Failed to delete message.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred when deleting message.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UserEntry userFromID(int ID) {
        UserEntry result = null;
        Packet packet = new Packet("searchByID", ID, null);
        try {
            userOut.writeObject(packet);
            Packet response = (Packet) userIn.readObject();
            if (response.query.equals("success")) {
                result = (UserEntry) response.content;
                return result;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to find any users with id: " + ID, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred searching for user with id: " + ID, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public void updateUserPrivacy(String newP) {
        currUser.setPrivacyPreference(newP);
        Packet packet = new Packet("updateEntry", new UserEntry(currUser), null);
        try {
            userOut.writeObject(packet);
            Packet response = (Packet) userIn.readObject();
            if (response.query.equals("success")) {
                JOptionPane.showMessageDialog(null, "Privacy preference updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error writing to database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating preference!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UserEntry userFromUsername(String username) {
        UserEntry result = null;
        Packet packet = new Packet("searchByName", username, null);
        try {
            userOut.writeObject(packet);
            Packet response = (Packet) userIn.readObject();
            if (response.query.equals("success")) {
                result = (UserEntry) response.content;
                return result;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to find any users with username: " + username, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred searching for user with username: " + username, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public void sendDMTextMessage(String msg, UserEntry recipientUser) {
        if (recipientUser.getPrivacyPreference().equals("Friends") && !recipientUser.getFriendList().contains(currUser.getID())) {
            JOptionPane.showMessageDialog(null, "" + recipientUser.getUsername() + " does not have you on their friend list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        if (recipientUser.getBlockList().contains(currUser.getID())) {
            JOptionPane.showMessageDialog(null, "" + recipientUser.getUsername() + " has you on their block list.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Packet dmMessage = new Packet(
                "insertEntry",
                new MessageEntry(
                        LocalTime.now().toString(),
                        currUser.getID(),
                        recipientUser.getID(),
                        new TextMessage(
                                msg,
                                currUser.getID(),
                                recipientUser.getID()
                        )
                ),
                null
        );
        try {
            msgOut.writeObject(dmMessage);
            Packet response = (Packet) msgIn.readObject();
            if (response.content == null || !response.query.equals("success")) throw new IllegalArgumentException();
        } catch (Exception e) {
            System.out.println("Error when sending text message.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occured sending text message to " + recipientUser.getUsername(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendPhotoMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            Packet packet = new Packet("searchByName", recipientUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipient = (UserEntry) response.content;
                    System.out.print("Enter the path of the photo: ");
                    String photoPath = scanner.nextLine();

                    Packet photoMsgPacket = new Packet(
                            "insertEntry",
                            new MessageEntry(
                                    LocalTime.now().toString(),
                                    currUser.getID(),
                                    recipient.getID(),
                                    new PhotoMessage (
                                            photoPath,
                                            currUser.getID(),
                                            recipient.getID()
                                    )
                            ),
                            null
                    );
                    msgOut.writeObject(photoMsgPacket); // Send the photo message packet
                    Packet photoResponse = (Packet) msgIn.readObject();
                    if (photoResponse == null || photoResponse.query.isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                    System.out.println("Photo message sent to " + recipientUsername);
                    JOptionPane.showMessageDialog(null, "Photo message sent to: " + recipientUsername, "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("User was not found.");
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when sending photo message.");
                JOptionPane.showMessageDialog(null, "Error occured when sending photo message", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        BufferedImage bimage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        icon = new ImageIcon(bimage.getScaledInstance(100, 50, Image.SCALE_DEFAULT));
        bGr.drawImage(icon.getImage(), 0, 0, null);
        bGr.dispose();
        return icon;
    }

    public User getCurrUser() {
        return currUser;
    }
}
