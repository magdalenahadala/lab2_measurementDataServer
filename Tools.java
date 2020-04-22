package lab2;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Tools 
{
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
			out.writeObject(obj);
		  out.flush();
		  return bos.toByteArray();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
		  try {
		    bos.close();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	  }
	}
	
	public static Object deserialize(byte[] bytes) throws ClassNotFoundException {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bis);
			obj = in.readObject();
			in.close();
			System.out.println("Serialized data is retrieved from bytes array");
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return obj;
		}
	}
}
