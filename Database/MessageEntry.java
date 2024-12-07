package Database;

import Message.Message;

/**
 * This class represents a MessageEntry for use in the MessageDatabase.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class MessageEntry extends GenericEntry {
  private String timestamp;
  private int sender;
  private int recipient;
  private Message content;

  // Create entry given fields
  public MessageEntry(String timestamp,  int sender,  int recipient,  Message content) {
    this.timestamp = timestamp;
    this.sender = sender;
    this.recipient = recipient;
    this.content = content;
  }

  // Create entry given XML
  public MessageEntry(String xml) throws ParseExceptionXML {
    super(xml);
  }

  @Override
  protected void handleXML(String content, String curTag, String parentTag) {
    if (curTag.equals("Timestamp")) {
      this.timestamp = content;
    } else if (curTag.equals("ID") && parentTag.equals("Sender")) {
      this.sender = Integer.parseInt(content);
    } else if (curTag.equals("ID") && parentTag.equals("Recipient")) {
      this.recipient = Integer.parseInt(content);
    } else if (curTag.equals("Content")) {
      this.content = Message.convertToMessage(content);
    }
  }

  @Override
  public String toString() {
    String result = "";
    result += "<Message>\n";

    result += String.format("\t<Timestamp>%s</Timestamp>\n", this.timestamp);

    result += String.format("\t<Sender>\n\t\t<ID>%d</ID>\n\t</Sender>\n", this.sender);

    result += String.format("\t<Recipient>\n\t\t<ID>%d</ID>\n\t</Recipient>\n", this.recipient);

    result += String.format("\t<Content>%s</Content>\n", this.content);

    result += "</Message>\n";

    return result;
  }

  public String getTimestamp() {
    return this.timestamp;
  }
  public int getSender() {
    return this.sender;
  }
  public int getRecipient() {
    return this.recipient;
  }
  public Message getContent() {
    return this.content;
  }

  public static void main(String[] args) throws ParseExceptionXML {
    System.out.println(new MessageEntry("<Message><Timestamp>02/11/2024</Timestamp><Sender><ID>12345678</ID></Sender><Recipient><ID>77889900</ID></Recipient><Content>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</Content></Message>"));
  }
}
