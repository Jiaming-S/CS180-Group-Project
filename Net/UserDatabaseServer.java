package Net;

import java.net.*;
import Database.*;

public class UserDatabaseServer extends GenericDatabaseServer {
  public UserDatabaseServer(Socket client, GenericDatabase db) {
    super(client, db);
  }

  @Override
  public Packet handlePacket(Packet p) {
    return null;
  }
}
