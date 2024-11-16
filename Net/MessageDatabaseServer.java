package Net;

import java.net.*;
import Database.*;

public class MessageDatabaseServer extends GenericDatabaseServer {
  public MessageDatabaseServer(Socket client, MessageDatabase db) {
    super(client, db);
  }

  @Override
  public Packet handlePacket(Packet p) {
    MessageDatabase mdb = (MessageDatabase) this.db;
    Packet response = null;

    try {
      switch (p.query) {
        case "searchFirstBySenderID":
          int sendId = (Integer) p.content;
          response = new Packet("success", mdb.searchFirstBySenderID(sendId), null);
          break;
        case "searchFirstByRecipientID":
          int receiveId = (Integer) p.content;
          response = new Packet("success", mdb.searchFirstByRecipientID(receiveId), null);
          break;
        case "searchAllBySenderID":
          int sendIdAll = (Integer) p.content;
          response = new Packet("success", mdb.searchAllBySenderID(sendIdAll), null);
          break;
        case "searchAllByRecipientID":
          int receiveIdAll = (Integer) p.content;
          response = new Packet("success", mdb.searchAllByRecipientID(receiveIdAll), null);
          break;
        case "getEntry":
          int getRowNum = (Integer) p.content;
          response = new Packet("success", mdb.getEntry(getRowNum), null);
          break;
        case "insertEntry":
          mdb.insertEntry((MessageEntry) p.content);
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
