package Net;

import Database.*;
import java.io.*;
import java.net.*;

/**
 * This abstract class represents a generic database server and implements the main server logic.
 * Any extending class MUST implement `handlePacket(Packet p)`.
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/16/2024
 */
public abstract class GenericDatabaseServer implements DatabaseServer, Runnable {
  protected Socket client;
  protected GenericDatabase db;

  public GenericDatabaseServer(Socket client, GenericDatabase db) {
    this.client = client;
    this.db = db;
  }

  @Override
  public void run() {
    ObjectOutputStream oos;
		ObjectInputStream  ois;

    try {
      oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			oos.flush();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    int exceptionLimit = 10;
    while(true) {
      try {
        Packet p = (Packet) ois.readObject();

        if (p == null || (p.content == null && p.query == null)) break;

        System.out.println("[Server] *-- Received Packet: --*\n[Server] " + p.toString().replaceAll("\n", "\n[Server] "));
      
        Packet response = handlePacket(p);

        System.out.println("[Server] *-- Responded With Packet: --*\n[Server] " + response.toString().replaceAll("\n", "\n[Server] "));

        if (response != null && response.query != null && !response.query.isEmpty()) oos.writeObject(response);
      } catch (EOFException e) {
        System.out.println("[Server] Received EOF. Exiting server.");
        break;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("[Server] Exception occurred while handling query. Remaining exception limit: " + --exceptionLimit);
        if (exceptionLimit <= 0) break;
      }
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
