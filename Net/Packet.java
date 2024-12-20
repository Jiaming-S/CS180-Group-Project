package Net;

import java.io.Serializable;

/**
 * This class represents a Packet which is used to communicate with `DatabaseServer`s.
 * @author Jiaming Situ
 * @version 11/16/2024
 */
public class Packet implements Serializable {
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
    String q = this.query;
    String c = "null";
    String r = "null";

    try {
      c = content.toString();
    } catch (Exception e) {
      // TODO
    }

    try {
      r = recipient.toString();
    } catch (Exception e) {
      // TODO
    }

    return String.format(
      "Query: %s\nContent: %s\nRecipient: %s\n", q, c, r);
  }

  public Object getContent() {
    return content;
  }

  public String getQuery() {
    return query;
  }
}
