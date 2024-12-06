import Database.*;
import Net.*;
import User.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Runner {

    /**
     * This class is the main method run by the client. Handles user interaction, network
     * startup and interaction, thread creation, and in the future will handle GUI.
     * @author Jane Bazzell
     * @version 11/17/2024
     */

    JPanel frame = new JPanel();

    public static void main(String[] args) throws IOException {
        UserThread userThread = null;
        User currentUser = null;

        String hostName = "localhost";
        int portUDBS = 12345;
        int portMDBS = 12346;
        Socket userSocket, messageSocket;
        ObjectOutputStream uoos, moos;
        ObjectInputStream uois, mois;

        try {
            //connect client to database of user information, begin streams
            userSocket = new Socket(hostName, portUDBS);
            uoos = new ObjectOutputStream(userSocket.getOutputStream());
            uoos.flush();
            uois = new ObjectInputStream(userSocket.getInputStream());
            //connect client to database of messaging conversations, begin streams
            messageSocket = new Socket(hostName, portMDBS);
            moos = new ObjectOutputStream(messageSocket.getOutputStream());
            moos.flush();
            mois = new ObjectInputStream(messageSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        int selection = 0; //just used for tracking user input

        System.out.println("Welcome");

        while (!loggedIn) { //prompts user to create account or log in until they successfully log in.
            System.out.println("1 - Create New User\n2 - Log In");
            try {
                selection = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter 1 or 2.");
                break;
            }
            switch (selection) {
                case 1:
                    addUser(scanner, uoos, uois);
                    System.out.println("User created");
                    break; //reloop to the selection menu
                case 2:
                    currentUser = attemptLogin(scanner, uoos, uois);
                    if (currentUser == null) {
                        break; //reloop to the selection menu
                    }
                    try {
                        userThread = new UserThread(currentUser, uoos, uois, moos, mois);
                        //create new userthread, sending streams so userthread can access the same databaseservers.
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
                    break;
            }
        } if (userThread != null) {
            userThread.start(); //begin userthread. full app functionality through run() method in userthread class.
        } try {
            if (userThread != null) userThread.join();
            if (uois != null) uois.close();
            if (uoos != null) uoos.close();
            if (mois != null) mois.close();
            if (moos != null) moos.close();
            if (userSocket != null) userSocket.close(); //close all streams
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(Scanner scanner, ObjectOutputStream oos, ObjectInputStream ois) {
        System.out.println("Please enter your new username");
        String username = scanner.next();
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            addUser(scanner, oos, ois); //recursively prompts user to create valid user inputs until they actually do it
            return;
        }
        System.out.println("Please enter your new password");
        String password = scanner.next();
        if (password.length() < 3) {
            System.out.println("Please increase password length");
            addUser(scanner, oos, ois); //recursively prompts user to create valid user inputs until they actually do it
            return;
        }
        UserEntry userEntry = new UserEntry(username, password, 0, new ArrayList<>(), new ArrayList<>(), "./", "USA");
        Packet packet = new Packet("insertEntry", userEntry, null);
        //sends packet with the new user's info to server to be written to the database.
        try {
            oos.writeObject(packet);
            Packet response = (Packet) ois.readObject();
            boolean success = response.getQuery().equals("success"); //checks if the user was successfully written to database. throws exception if not.
            if (!success) {
                System.out.println("Error writing to database.");
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User attemptLogin(Scanner scanner, ObjectOutputStream oos, ObjectInputStream ois) {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String pw = scanner.next();
        UserEntry ue = logIn(username, oos, ois); //login method handles server packet fetching.
        if (ue == null) return null;
        if (ue.getPassword().equals(pw)) {
            return new User(ue);
        } else {
            System.out.println("Incorrect!");
            attemptLogin(scanner, oos, ois);
        }
        return null;
    }

    public static UserEntry logIn(String username, ObjectOutputStream oos, ObjectInputStream ois) {
        Packet packet = new Packet("searchByName", username, null); //packet requesting the matching userentry to the username.
        UserEntry userE = null;
        try {
            oos.writeObject(packet);
            Packet response = (Packet) ois.readObject();
            if (response.getContent() == null) {
                return null;
            }
            return new UserEntry(response.getContent().toString()); //returns matching userentry if found.
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public void openGUI() {

    }
}
