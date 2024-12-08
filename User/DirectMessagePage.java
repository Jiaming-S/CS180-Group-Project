package User;

import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.*;
import Message.TextMessage;

public class DirectMessagePage {
  private JFrame frame;
  private UserThread userThread;
  private User currUser;
  private int otherUserID;

  private JPanel messageHistory;
  private JTextField sendMessageField;
  private JButton sendMessageButton;

  public DirectMessagePage(UserThread userThread, int otherUserID) {
    this.userThread = userThread;
    this.currUser = userThread.getCurrUser();
    this.otherUserID = otherUserID;

    frame = new JFrame("AOL TWO");
    frame.setSize(600, 400);
    frame.setLocationRelativeTo(null);
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
    if (me.getContent() == null) tileContent = new JLabel("null Message");
    else tileContent = new JLabel((String) (me.getContent().getMessage()));
    
    tileContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    wrapper.add(tileContent);

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

      new DirectMessagePage(userThread, otherUserID).viewDMPage();
    });

    JScrollPane messageScroller = new JScrollPane(messageHistory);
    content.add(messageScroller);

    sendMessageFooter.add(sendMessageField);
    sendMessageFooter.add(sendMessageButton);
    content.add(sendMessageFooter, BorderLayout.SOUTH);

    frame.add(content);
    frame.setVisible(true);
  }
}
