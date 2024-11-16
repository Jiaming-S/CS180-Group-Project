package Net;

import java.io.*;
import java.net.*;
import Database.*;

public abstract class GenericDatabaseServer implements DatabaseServer, Runnable {
  protected Socket client;
  protected GenericDatabase db;

  public GenericDatabaseServer(Socket client, GenericDatabase db) {
    this.client = client;
    this.db = db;
  }

  @Override
  @SuppressWarnings("unused")
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

    try {
      while (true) {
        Packet p = (Packet) ois.readObject();
        System.out.println(p);
        Packet response = handlePacket(p);
        if (response != null && response.content != null && !response.query.isEmpty()) oos.writeObject(response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception occurred while handling query. Exiting main loop.");
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
