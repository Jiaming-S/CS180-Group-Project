package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPage extends JComponent {
    private JFrame frame;
    UserThread userThread;

    public MainPage(UserThread userThread) {
        this.userThread = userThread;
        frame = new JFrame("AOL TWO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
    }

    public void showPage() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JButton searchButton = new JButton("Search User");
        JTextField searchField = new JTextField(15);
//        JButton profileButton = new JButton("View Profile");
//        JButton editProfileButton = new JButton("Edit Profile");
//        JButton blockButton = new JButton("Block User");
//        JButton convoButton = new JButton("Start New Conversation");
//        JButton msgButton = new JButton("View Message");
//        JButton sendTextButton = new JButton("Send Text Message");
//        JButton sendPhotoButton = new JButton("Send Photo Message");
        JButton logOutButton = new JButton("Log Out");

        panel.add(searchButton);
//        panel.add(profileButton);
//        panel.add(blockButton);
//        panel.add(convoButton);
//        panel.add(msgButton);
//        panel.add(sendTextButton);
//        panel.add(sendPhotoButton);
        panel.add(logOutButton);

        // Action Listeners for buttons
        searchButton.addActionListener(e -> {
            String searchUser = searchField.getText();
            userThread.searchUser(searchUser);


        });
//        profileButton.addActionListener(e -> System.out.println("View Profile clicked"));
//        blockButton.addActionListener(e -> System.out.println("Block User clicked"));
//        convoButton.addActionListener(e -> System.out.println("Start New Conversation clicked"));
//        msgButton.addActionListener(e -> System.out.println("View Message clicked"));
//        sendTextButton.addActionListener(e -> System.out.println("Send Text Message clicked"));
//        sendPhotoButton.addActionListener(e -> System.out.println("Send Photo Message clicked"));
        logOutButton.addActionListener(e -> System.out.println("Log Out clicked"));

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
