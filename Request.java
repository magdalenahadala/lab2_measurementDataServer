package lab2;

import java.io.Serializable;
import java.util.Arrays;

import lab1.Packet;

public class Request extends Packet implements Serializable {
	protected String type;
	protected String value;
	
	public Request(String device, String description, long date, String type, String value) {
		super(device, description, date);
		this.type = type;
		this.value = value;
	}

	@Override
	public int getDescription() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDevice() {
		// TODO Auto-generated method stub
		return null;
	}
}