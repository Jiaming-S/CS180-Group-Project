package Net;

public class Packet {
  public String query;
  public Object content; // must always be non-null unless this is a "end connection" packet
  public Object recipient; // might always be null depending on use case
  
  public Packet(String query, Object content, Object recipient) {
    this.query = query;
    this.content = content;
    this.recipient = recipient;
  }

  @Override
  public String toString() {
    return String.format("Query: %s\nContent: %s\nRecipient: %s\n",this.query, this.content, this.recipient);
  }
}
