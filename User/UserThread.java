package User;

import Database.UserEntry;
import Message.*;
import Net.Packet;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



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
    private final Object lock = new Object();
    private Scanner scanner;

    public UserThread(User currUser, ObjectOutputStream userOut, ObjectInputStream userIn, ObjectOutputStream msgOut, ObjectInputStream msgIn, JFrame Frame) throws IOException, ClassNotFoundException {
        this.currUser = currUser;
        this.userOut = userOut;
        this.userIn = userIn;
        this.msgOut = msgOut;
        this.msgIn = msgIn;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        boolean running = true;
        try {
            while (running) {
                System.out.println("Welcome " + currUser.getUsername());
                System.out.println("1 - Search User\n2 - View Profile\n3 - Block User\n4 - Start New Conversation\n5 - View Message\n6 - Send TextMessage\n7 - Send PhotoMessage\n8 - Log Out");
                String input = scanner.nextLine().trim();
                try {
                    int answer = Integer.parseInt(input);
                    switch (answer) {
                        case 1:
                            searchUser();
                            break;
                        case 2:
                            viewProfile();
                            break;
                        case 3:
                            blockUser();
                            break;
                        case 4:
                            newConvo();
                            break;
                        case 5:
                            viewMsg();
                            break;
                        case 6:
                            sendTextMsg();
                            break;
                        case 7:
                            sendPhotoMsg();
                            break;
                        case 8:
                            running = false;
                            System.out.println("Logging out");
                            break;
                        default:
                            System.out.println("Invalid answer, please enter a valid answer.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
        } catch(Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            try { //close all streams
                if (userOut != null)
                    userOut.close();
                if (userIn != null)
                    userIn.close();
                if (msgOut != null)
                    msgOut.close();
                if (msgIn != null)
                    msgIn.close();
                if (scanner != null)
                    scanner.close();
            } catch (IOException e) {
                System.err.println("Streams cannot be closed: " + e.getMessage());
            }
        }
    }

    public void searchUser() {
        synchronized (lock) {
            System.out.print("Enter username you want to search: ");
            String username = scanner.nextLine();
            //sends packet to databaseserver to get matching userEntry of username
            Packet packet = new Packet("searchByName", username, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject(); //receive packet with UserEntry info from databaseserver
                if (response.query.equals("success")) { //query == "success" if user found, "failure" if anything else occurs.
                    UserEntry userToFind = (UserEntry) response.content;
                    System.out.println("User found: " + userToFind.getUsername());
                    System.out.println("ID: " + userToFind.getID());
                    System.out.println("Region: " + userToFind.getRegion());
                    JOptionPane.showMessageDialog(null, String.format("%s\n%s\n%s", "User found: " + userToFind.getUsername(), "ID: " + userToFind.getID(), "Region: " + userToFind.getRegion()), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("User was not found.");
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when searching user.");
                JOptionPane.showMessageDialog(null, "Error occured during search", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void viewProfile() {
        synchronized (lock) {
            System.out.println(currUser.getUsername() + "'s Profile:");
            System.out.println("Username: " + currUser.getUsername());
            System.out.println("ID: " + currUser.getID());
            System.out.println("Region: " + currUser.getRegion());
        }
    }

    public void blockUser() {
        synchronized (lock) {
            System.out.print("Enter username to block: ");
            String blockedUsername = scanner.nextLine();
            Packet packet = new Packet("searchByName", blockedUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry blocked = (UserEntry) response.content;
                    currUser.getBlockList().add(blocked.getID()); //add to the userentry's arraylist of blocked users
                    System.out.println("Blocked user is: " + blocked.getUsername());
                    JOptionPane.showMessageDialog(null, "Blocked user: " + blocked.getUsername(), "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("User not found.");
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when blocking user.");
                JOptionPane.showMessageDialog(null, "Error when blocking user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void newConvo() {
        synchronized (lock) {
            System.out.print("Enter username to start a conversation with: ");
            String username = scanner.nextLine();
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
                System.out.println("Error when starting conversation.");
                JOptionPane.showMessageDialog(null, "Error occured when starting conversation", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void viewMsg() {
        //must be implemented after GUI is created
    }

    public void sendTextMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            Packet packet = new Packet("searchByName", recipientUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipient = (UserEntry) response.content;
                    System.out.print("Enter your message: ");
                    String messageContent = scanner.nextLine();
                    Packet textMsgPacket = new Packet(
                        "sendTextMessage", 
                        new TextMessage(messageContent, currUser.getID(), recipient.getID()),
                        null
                    );
                    msgOut.writeObject(textMsgPacket); // Send the text message packet
                    Packet textResponse = (Packet) msgIn.readObject();
                    // if anything besides success of textmessage writing occurs throw exception
                    if (textResponse == null || textResponse.query.isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                    System.out.println("Text message sent to " + recipientUsername);
                    JOptionPane.showMessageDialog(null, "Text message sent to: " + recipientUsername, "Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("User was not found.");
                    JOptionPane.showMessageDialog(null, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Error when sending text message.");
                JOptionPane.showMessageDialog(null, "Error occured when sending text message", "Error", JOptionPane.ERROR_MESSAGE);
            }
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

                    Packet photoMsgPacket = new Packet("sendPhotoMessage", new PhotoMessage(photoPath, currUser.getID(), recipient.getID()), null);
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

}