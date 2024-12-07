import Database.*;
import Net.*;
import User.*;


import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private static JFrame frame; // the canvas

    private JButton registerButton;
    private JButton loginButton;
    private JTextField usernameTF;
    private JTextField passwordTF;

    private static boolean loggedIn;

    private static ObjectOutputStream uoos, moos;
    private static ObjectInputStream uois, mois;
    private static User currentUser = null;

    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portUDBS = 12345;
        int portMDBS = 12346;
        Socket userSocket = null;
        Socket messageSocket = null;
        UserThread userThread = null;

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

            SwingUtilities.invokeLater(new Runner());

            try {
                userThread = new UserThread(currentUser, uoos, uois, moos, mois, frame);
                //create new userthread, sending streams so userthread can access the same databaseservers.
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }

            while (true) {
                if (loggedIn) {
                    if (userThread != null) {
                        userThread.start(); //begin userthread. full app functionality through run() method in userthread class.
                    }
                    try {
                        if (userThread != null) userThread.join();
                        if (uois != null) uois.close();
                        if (uoos != null) uoos.close();
                        if (mois != null) mois.close();
                        if (moos != null) moos.close();
                        if (userSocket != null) userSocket.close();
                        if (messageSocket != null) messageSocket.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (uois != null) uois.close();
            if (uoos != null) uoos.close();
            if (mois != null) mois.close();
            if (moos != null) moos.close();
            if (userSocket != null) userSocket.close();
            if (messageSocket != null) messageSocket.close();
        }
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
        frame = new JFrame("AOL Squared");

        Container content = new Container();
        content.setLayout(new BorderLayout());

        Panel topPanel = new Panel();
        topPanel.setLayout(new FlowLayout());

        registerButton = new JButton("Register");
        registerButton.addActionListener(actionListener);
        topPanel.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        topPanel.add(loginButton);

        content.add(topPanel, BorderLayout.CENTER);

        Image image = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("aol.jpg"));
            image = bufferedImage.getScaledInstance(100, 50, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new FlowLayout());

        ImageIcon aol = new ImageIcon(image);
        JFrame frame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setIcon(aol);

        bottomPanel.add(jLabel);

        content.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(content);

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    loggedIn = true;

                }
            }
        }
    };
}
