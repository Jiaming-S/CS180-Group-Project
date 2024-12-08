package User;

import Database.UserEntry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProfilePage extends JComponent {

    private JFrame frame;
    private UserThread userThread;
    private UserEntry profiledUser;
    private UserEntry currUser;
    private final Object lock = new Object();
    private JButton blockButton;
    private JButton friendButton;

    public ProfilePage(UserThread userThread, UserEntry user) {
        this.profiledUser = user;
        this.userThread = userThread;
        this.currUser = new UserEntry(userThread.getCurrUser());
        frame = new JFrame("AOL TWO");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        blockButton = new JButton();
        friendButton = new JButton();
    }

    public void viewProfile() {
        Container content = new Container();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // if (profiledUser.getProfilePicture() != null && !profiledUser.getProfilePicture().equals("")) {
        //     ImageIcon image = scaleImageIcon(new ImageIcon(profiledUser.getProfilePicture()), 100, 100);
        //     JLabel jLabel = new JLabel();
        //     jLabel.setIcon(image);
        //     leftPanel.add(jLabel);
        // } else {
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
            wrapper.add(jLabel);
            wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            leftPanel.add(wrapper);
        // }

        checkStatuses();
        blockButton.addActionListener(blockListener);
        friendButton.addActionListener(friendListener);

        content.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(new JLabel(
            String.format(
                "<html><h1>%s</h1><h4>ID: %d</h4><p>Region: %s</p><p>Bio: %s</p><p>Privacy Preference: %s</p></html>",
                profiledUser.getUsername(),
                profiledUser.getID(),
                profiledUser.getRegion(),
                profiledUser.getBio(),
                profiledUser.getPrivacyPreference()
            )
        ));

        System.out.println(currUser.getID());
        System.out.println(profiledUser.getID());
        if (currUser.getID() != profiledUser.getID()) {
            rightPanel.add(friendButton);
            rightPanel.add(blockButton);
        }

        content.add(rightPanel);

        frame.add(content);

        frame.setVisible(true);
    }

    public void checkStatuses() {
        if (currUser.getBlockList().contains(profiledUser.getID())) {
            blockButton.setText("Unblock user");
        } else {
            blockButton.setText("Block User");
        }
        if (currUser.getFriendList().contains(profiledUser.getID())) {
            friendButton.setText("Unfriend User");
        } else {
            friendButton.setText("Friend user");
        }
    }

    ActionListener blockListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currUser.getBlockList().contains(profiledUser.getID())) {
                userThread.unblockUser(profiledUser.getUsername());
            } else {
                if (currUser.getFriendList().contains(profiledUser.getID())) {
                    userThread.unfriendUser(profiledUser.getUsername());
                }
                userThread.blockUser(profiledUser.getUsername());
            }
            checkStatuses();
        }
    };

    ActionListener friendListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currUser.getBlockList().contains(profiledUser.getID())) {
                JOptionPane.showMessageDialog(null, "Cannot friend a blocked user!");
            } else {
                if (currUser.getFriendList().contains(profiledUser.getID())) {
                    userThread.unfriendUser(profiledUser.getUsername());
                } else {
                    userThread.friendUser(profiledUser.getUsername());
                }
            }
            checkStatuses();
        }
    };

    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        BufferedImage bimage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        icon = new ImageIcon(bimage.getScaledInstance(100, 50, Image.SCALE_DEFAULT));
        bGr.drawImage(icon.getImage(), 0, 0, null);
        bGr.dispose();
        return icon;
    }
}
