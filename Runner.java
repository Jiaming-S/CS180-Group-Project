import Database.*;
import Net.*;
import User.*;


import java.awt.event.*;
import java.awt.geom.Dimension2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

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

    private static boolean loggedIn;

    private static ObjectOutputStream uoos, moos;
    private static ObjectInputStream uois, mois;
    private static User currentUser = null;
    private static UserThread userThread;

    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portUDBS = 12345;
        int portMDBS = 12346;
        Socket userSocket = null;
        Socket messageSocket = null;
        userThread = null;

        try {
            //TODO: implement photo messaging using a pop-up? consider drop-down
            //TODO: look for and consider converting byte array to images
            //TODO: Delete messages
            //TODO: Condense edit profile and toggle privacy into a single user settings page?
            //TODO: Do some bug-securing
            //TODO: Handle if user closes the frame
            //TODO: Report
            //TODO: Slides
            //TODO: Demo and presentation
            //connect client to database of user information, begin streams
            try {
                userSocket = new Socket(hostName, portUDBS);
                uoos = new ObjectOutputStream(userSocket.getOutputStream());
                uoos.flush();
                uois = new ObjectInputStream(userSocket.getInputStream());
                //connect client to database of messaging conversations, begin streams
                messageSocket = new Socket(hostName, portMDBS);
                moos = new ObjectOutputStream(messageSocket.getOutputStream());
                moos.flush();
                mois = new ObjectInputStream(messageSocket.getInputStream());
            } catch (ConnectException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to server! Closing application.");
                return;
            }

            SwingUtilities.invokeLater(new Runner());

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
        UserEntry userEntry = new UserEntry(username, password, 0, new ArrayList<>(), new ArrayList<>(), "", "USA", "", "All");
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

//        String username = JOptionPane.showInputDialog(null, "Enter your username",
//                "Login", JOptionPane.QUESTION_MESSAGE);
//        String pw;
//
//
//        if (username == null) {
//            SwingUtilities.invokeLater(new Runner());
//        } else {
//            pw = JOptionPane.showInputDialog(null, "Enter your password",
//                    "Login", JOptionPane.QUESTION_MESSAGE);
//            UserEntry ue = fetchUser(username, oos, ois); //login method handles server packet fetching.
//            if (ue == null) return null;
//            if (ue.getPassword().equals(pw)) {
//                return new User(ue);
//            } else {
//                System.out.println("Incorrect!");
//                attemptLogin(oos, ois);
//            }
//            return null;
//        }
//        return null;
    }

    public static UserEntry fetchUser(String username, ObjectOutputStream oos, ObjectInputStream ois) {
        Packet packet = new Packet("searchByName", username, null); //packet requesting the matching userentry to the username.
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
        frame.setSize(640, 360);

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(3, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Image image = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("aol.jpg"));
            image = bufferedImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon aol = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(aol);
        JPanel wrapper = new JPanel();
        wrapper.setSize(new Dimension(100, 200));
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(jLabel);
        content.add(wrapper);

        registerButton = new JButton("Register");
        registerButton.addActionListener(actionListener);
        content.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        content.add(loginButton);

        frame.add(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
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

                try {
                    userThread = new UserThread(currentUser, uoos, uois, moos, mois, frame);
                    //create new userthread, sending streams so userthread can access the same databaseservers.
                } catch (ClassNotFoundException | IOException ex) {
                    ex.printStackTrace();
                }

                if (currentUser != null) {
                    loggedIn = true;
                    frame.setVisible(false);
                    MainPage mainPage = new MainPage(userThread);
                    mainPage.showPage();

                }
            }
        }
    };
}
