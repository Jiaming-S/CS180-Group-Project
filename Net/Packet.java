package Net;

public class Packet {
  public String query;
  public Object content;
  public Object recipient;
  
  public Packet(String query, Object content, Object recipient) {
    this.query = query;
    this.content = content;
    this.recipient = recipient;
  }
}
