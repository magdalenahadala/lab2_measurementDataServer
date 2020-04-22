package lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import lab1.Packet;
import lab1.Spectrum;
import lab1.TimeHistory;

public class UDPClient {
	public static void main(String[] args) throws ClassNotFoundException {
		DatagramSocket aSocket = null;
		Scanner scanner = null;
		try {
			byte[] buffer = new byte[1024];
			InetAddress aHost = InetAddress.getByName(args[0]);
			int serverPort = 9876;
			aSocket = new DatagramSocket();
			scanner = new Scanner(System.in);
			String line = "";
			while (true) {
				System.out.println("Enter the data 1. TimeHistory, 2.Spectrum, 3.Request");
				if (scanner.hasNextLine()) {
					Packet packet = null;
					line = scanner.nextLine();
					switch(line) {
					case"1":
						packet = new TimeHistory<Integer>("device1", "description1", 2020, 1, "unit1", 15.6, 1, 10.20);
						break;
					case"2":
						packet = new Spectrum<Integer>("device2", "description2", 2020, 1, "unit2", 15.6, 2 , 1);
						break;
					case"3":
						packet = new Request("Device", "Description", 2020,"Type","Value");
						break;
				
					}
				String path = "packet.ser";
				if (serialize(packet,path) != false) {
					byte[] data = Files.readAllBytes(new File(path).toPath());
					DatagramPacket request = new DatagramPacket(data,data.length,aHost,serverPort);
					aSocket.send(request);
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					String retrieve = "retrieve.ser";
					Files.write(new File(retrieve).toPath(),reply.getData());
					Packet read = null;
					
					try {
					read = (Packet) deserialize(retrieve);
					System.out.println("Retrieved packet " + read);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						}
					}
				}
			}
			


		
		} catch (SocketException ex) {
			Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException ex) {
			Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			aSocket.close();
			scanner.close();
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
