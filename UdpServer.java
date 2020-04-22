package lab2;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import lab1.Packet;
import lab1.Spectrum;
import lab1.TimeHistory;

public class UdpServer {
    public static void main(String[] args) {
    	DatagramSocket aSocket = null;
      try {
        aSocket = new DatagramSocket(9876);
        byte[] buffer = new byte[1024];
        while(true) {
          DatagramPacket request = new DatagramPacket(buffer, buffer.length);
          System.out.println("Waiting for request...");
          aSocket.receive(request);
          
          try {
        	  Packet read = (Packet) deserialize("packet.ser");
        	  
        	  String filename = "";
        	  
        	  if (read instanceof TimeHistory) {
          	  filename += "TimeHistory-";
            } else if (read instanceof Spectrum) {
          	  filename += "Spectrum-";
            } else if (read instanceof Packet) {
          	  filename += "Packet-";
            } else if (read instanceof Request) {
          	  filename += "Request-";
            }
        	  
        	  filename += read.getDevice() + "-" + read.getDescription();System.out.print("Creating .ser file for : "+ filename +" class\n");
        	  
        	  Files.write(new File(filename).toPath(), buffer);

          } catch (ClassNotFoundException ex) {
        	 ex.printStackTrace();
          };
          
                    
          DatagramPacket reply = new DatagramPacket(request.getData(), 
	        request.getLength(), request.getAddress(), request.getPort());
          aSocket.send(reply); 
        }
      } catch (SocketException ex) {
        Logger.getLogger(UdpServer.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(UdpServer.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
    		aSocket.close();
    	}
      
    }

    
    private static boolean serialize(Object obj, String path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			out.close();
			System.out.println("Serialized data is saved in " + path);
			return true;
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
	}

	private static Object deserialize(String path) throws ClassNotFoundException {
		Object obj = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			obj = in.readObject();
			in.close();
			System.out.println("Serialized data is retrieved from " + path);
			return obj;
		} catch (IOException i) {
			i.printStackTrace();
			return obj;
		}
	}
}
