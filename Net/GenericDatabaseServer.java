package Net;

import java.io.*;
import java.net.*;
import Database.*;

/**
 * This abstract class represents a generic database server and implements the main server logic.
 * Any extending class MUST implement `handlePacket(Packet p)`.
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/16/2024
 */
public abstract class GenericDatabaseServer implements DatabaseServer, Runnable {
  protected ServerSocket server;
  protected GenericDatabase db;

  public GenericDatabaseServer(ServerSocket server, GenericDatabase db) {
    this.server = server;
    this.db = db;
  }

  @Override
  public void run() {
    ObjectOutputStream oos;
		ObjectInputStream  ois;

    try {
      Socket client = server.accept();
      oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			oos.flush();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    try {
      Packet p = (Packet) ois.readObject();

      System.out.println("Received Packet:\n" + p);
      
      Packet response = handlePacket(p);
      if (response != null && response.query != null && !response.query.isEmpty()) oos.writeObject(response);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception occurred while handling query.");
    }

    try {
      oos.close();
      ois.close();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }
}
