package Net;

import java.net.*;
import Database.*;

/**
 * This class represents a UserDatabaseServer acting as a web API for a `UserDatabase` in a thread-safe manner.
 * @author Jiaming Situ
 * @version 11/16/2024
 */
public class UserDatabaseServer extends GenericDatabaseServer {
  public UserDatabaseServer(Socket client, UserDatabase db) {
    super(client, db);
  }

  @Override
  public Packet handlePacket(Packet p) {
    UserDatabase udb = (UserDatabase) this.db;
    Packet response = null;

    try {
      switch (p.query) {
        case "searchByName":
          String name = (String) p.content;
          response = new Packet("success", udb.searchByName(name), null);
          break;
        case "searchByID":
          int userId = (Integer) p.content;
          response = new Packet("success", udb.searchByID(userId), null);
          break;
        case "getEntry":
          int getRowNum = (Integer) p.content;
          response = new Packet("success", udb.getEntry(getRowNum), null);
          break;
        case "insertEntry":
          udb.insertEntry((UserEntry) p.content);
          response = new Packet("success", true, null);
          break;
        default:
          response = new Packet("forbidden", null, null);
          break;
      }
    } catch (Exception e) {
      return new Packet("error", null, null);
    }

    return response;
  }
}
