package User;

import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.*;
import Message.TextMessage;

public class DirectMessagePage extends JFrame {
  private UserThread userThread;
  private User currUser;
  private int otherUserID;

  private JPanel messageHistory;
  private JTextField sendMessageField;
  private JButton sendMessageButton;

  public DirectMessagePage(UserThread userThread, int otherUserID) {
    super("AOL TWO: DM With User ID" + otherUserID);
    this.userThread = userThread;
    this.currUser = userThread.getCurrUser();
    this.otherUserID = otherUserID;
    
    setSize(600, 400);
    setLocationRelativeTo(null);
  }

  private JPanel makeMessageTileNoPad(MessageEntry me) {
    JPanel tile = new JPanel();
    tile.setLayout(new FlowLayout(FlowLayout.LEFT));
    tile.setBorder(new EmptyBorder(1, 1, 1, 1));
    
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
    wrapper.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(Color.black),
      BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    JLabel tileHeader = new JLabel(
      String.format(
        "Message from %s at %s.",
        userThread.userFromID(me.getSender()).getUsername(),
        me.getTimestamp().toString()
      )
    );
    tileHeader.setLayout(new FlowLayout(FlowLayout.LEFT));
    wrapper.add(tileHeader);

    JLabel tileContent;
    if (me.getContent() == null) tileContent = new JLabel("[null MessageEntry]");
    else tileContent = new JLabel((String) (me.getContent().getMessage()));
    
    tileContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    wrapper.add(tileContent);

    if (me.getSender() == currUser.getID()) {
      JButton deleteMessageButton = new JButton("Delete Message");
      deleteMessageButton.addActionListener(_ -> {
        userThread.deleteMessageEntry(me);
        new DirectMessagePage(userThread, otherUserID).viewDMPage();
      });
      wrapper.add(deleteMessageButton);
    }

    tile.add(wrapper);
    return tile;
  }

  public void viewDMPage() {
    Container content = new Container();
    content.setLayout(new BorderLayout());

    messageHistory = new JPanel();
    messageHistory.setLayout(new BoxLayout(messageHistory, BoxLayout.Y_AXIS));
    messageHistory.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    ArrayList<MessageEntry> messagesToUser = userThread.getAllMessagesToUser(currUser.getID());
    ArrayList<MessageEntry> messagesFromUser = userThread.getAllMessagesFromUser(currUser.getID());
    ArrayList<MessageEntry> combinedInvolvingUser = new ArrayList<>();
    if (messagesToUser != null) combinedInvolvingUser.addAll(messagesToUser);
    if (messagesFromUser != null) combinedInvolvingUser.addAll(messagesFromUser);

    Collections.sort(
      combinedInvolvingUser, 
      new Comparator<MessageEntry>() {
        public int compare(MessageEntry o1, MessageEntry o2){
          return o1.getTimestamp().compareTo(o2.getTimestamp()) <= 0 ? -1 : 1;
        }
      }
    );

    for (MessageEntry me : combinedInvolvingUser) {
      if (me.getSender() == otherUserID || me.getRecipient() == otherUserID) {
        messageHistory.add(makeMessageTileNoPad(me));
      }
    }

    JPanel sendMessageFooter = new JPanel();
    sendMessageFooter.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    sendMessageField = new JTextField(20);
    sendMessageButton = new JButton("Send");
    
    sendMessageButton.addActionListener(_ -> {
      String sentText = sendMessageField.getText();
      sendMessageField.setText("");
      userThread.sendDMTextMessage(sentText, userThread.userFromID(otherUserID));

      messageHistory.add(makeMessageTileNoPad(
        new MessageEntry(
          LocalTime.now().toString(),
          currUser.getID(),
          otherUserID,
          new TextMessage( 
            sentText,
            currUser.getID(),
            otherUserID
          )
        )
      ));

      DirectMessagePage newPage = new DirectMessagePage(userThread, otherUserID);
      newPage.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
          DirectMessagePage.this.dispose();
        }
      });
  
      newPage.viewDMPage();
    });

    JScrollPane messageScroller = new JScrollPane(messageHistory);
    content.add(messageScroller);

    sendMessageFooter.add(sendMessageField);
    sendMessageFooter.add(sendMessageButton);
    content.add(sendMessageFooter, BorderLayout.SOUTH);

    this.add(content);
    this.setVisible(true);
  }
}
