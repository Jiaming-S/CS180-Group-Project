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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
    }

    public void showPage() {
        frame.dispose();
        frame.setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 1, 10, 10));


        JButton searchButton = new JButton("Search User");
        JTextField searchField = new JTextField(20);
        gridPanel.add(new JLabel("Search User:"));
        gridPanel.add(searchField);
//        JButton profileButton = new JButton("View Profile");
//        JButton editProfileButton = new JButton("Edit Profile");
//        JButton blockButton = new JButton("Block User");
//        JButton convoButton = new JButton("Start New Conversation");
//        JButton msgButton = new JButton("View Message");
//        JButton sendTextButton = new JButton("Send Text Message");
//        JButton sendPhotoButton = new JButton("Send Photo Message");
//        JButton logOutButton = new JButton("Log Out");

        gridPanel.add(searchButton);
        gridPanel.add(searchField);
//        panel.add(profileButton);
//        panel.add(blockButton);
//        panel.add(convoButton);
//        panel.add(msgButton);
//        panel.add(sendTextButton);
//        panel.add(sendPhotoButton);
//        panel.add(logOutButton);

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
//        logOutButton.addActionListener(e -> System.out.println("Log Out clicked"));
        frame.add(gridPanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
