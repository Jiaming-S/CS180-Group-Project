package Net;

public class Packet {
  public String query; // ALWAYS non-null; if this is null then something has gone horribly wrong
  public Object content; // usually non-null; except when this is an error packet or "end of connection" packet
  public Object recipient; // usually null; only used in some niche api calls
  
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
