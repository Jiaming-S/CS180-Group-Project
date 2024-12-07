import Database.*;
import Net.*;
import User.*;


import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner extends JComponent implements Runnable {

    /**
     * This class is the main method run by the client. Handles user interaction, network
     * startup and interaction, thread creation, and in the future will handle GUI.
     * @author Jane Bazzell
     * @version 11/17/2024
     */
    private JFrame frame; // the canvas

    private JButton registerButton;
    private JButton loginButton;
    private JTextField usernameTF;
    private JTextField passwordTF;

    private static boolean loggedIn;

    private static ObjectOutputStream uoos, moos;
    private static ObjectInputStream uois, mois;
    private UserThread userThread = null;
    private User currentUser = null;

    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portUDBS = 12345;
        int portMDBS = 12346;
        Socket userSocket, messageSocket;

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
        loggedIn = false;
        SwingUtilities.invokeLater(new Runner());

    }

    public void addUser(ObjectOutputStream oos, ObjectInputStream ois) {
        String username = JOptionPane.showInputDialog(null, "Enter new username",
                "New account creation", JOptionPane.QUESTION_MESSAGE);
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            addUser(oos, ois); //recursively prompts user to create valid user inputs until they actually do it
            return;
        }
        String password = JOptionPane.showInputDialog(null, "Enter new password",
                "New account creation", JOptionPane.QUESTION_MESSAGE);
        if (password.length() < 3) {
            addUser(oos, ois); //recursively prompts user to create valid user inputs until they actually do it
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

    public static User attemptLogin(ObjectOutputStream oos, ObjectInputStream ois) {
        String username = JOptionPane.showInputDialog(null, "Enter your username",
                "Login", JOptionPane.QUESTION_MESSAGE);
        String pw = JOptionPane.showInputDialog(null, "Enter your password",
                "Login", JOptionPane.QUESTION_MESSAGE);
        UserEntry ue = fetchUser(username, oos, ois); //login method handles server packet fetching.
        if (ue == null) return null;
        if (ue.getPassword().equals(pw)) {
            return new User(ue);
        } else {
            System.out.println("Incorrect!");
            attemptLogin(oos, ois);
        }
        return null;
    }

    public static UserEntry fetchUser(String username, ObjectOutputStream oos, ObjectInputStream ois) {
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

    public void run() {
        frame = new JFrame("AOL Two");

        Panel panel = new Panel();
        panel.setLayout(new FlowLayout());

        registerButton = new JButton("Register");
        registerButton.addActionListener(actionListener);
        panel.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        panel.add(loginButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == registerButton) {
                addUser(uoos, uois);

            }
            if (e.getSource() == loginButton) {
                currentUser = attemptLogin(uoos, uois);
                if (currentUser != null) {
                    try {
                        userThread = new UserThread(currentUser, uoos, uois, moos, mois);
                        //create new userthread, sending streams so userthread can access the same databaseservers.
                    } catch (ClassNotFoundException | IOException ex) {
                        ex.printStackTrace();
                    }
                    loggedIn = true;
                    if (userThread != null) {
                        userThread.start(); //begin userthread. full app functionality through run() method in userthread class.
                    }
                    try {
                        if (userThread != null) userThread.join();
                        System.out.println("CLOSING!!");
                        if (uois != null) uois.close();
                        if (uoos != null) uoos.close();
                        if (mois != null) mois.close();
                        if (moos != null) moos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    };
}
