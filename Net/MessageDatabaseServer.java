package Net;

import java.net.*;
import Database.*;

public class MessageDatabaseServer extends GenericDatabaseServer {
  public MessageDatabaseServer(Socket client, GenericDatabase db) {
    super(client, db);
  }

  @Override
  public Packet handlePacket(Packet p) {
    return null;
  }
}
