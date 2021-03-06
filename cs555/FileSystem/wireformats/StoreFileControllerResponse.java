package cs555.FileSystem.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class StoreFileControllerResponse implements Event{
	private int type;
	private long timestamp;
	private String m_info;

	public void setMessageType(int messageType){ type = messageType; }
	public int getMessageType(){ return type; }
	public String getIPAddress(){ return null; }
	public int getListenPortNumber(){ return -1;}
	public byte getStatusCode(){ return 0; }
	public String getInfo(){ return m_info; }
	public int getForwardFlag(){ return -1; }
	public int getFingerTableEntry(){ return -1; }
	
	public byte[] storeFileControllerResponse(String info) throws IOException
	{
		type = Protocol.STORE_FILE_CONTROLLER_RESPONSE;
		Date dte=new Date();
	    timestamp = dte.getTime();
	    m_info = info;
		byte[] marshalledBytes = getBytes();
		return marshalledBytes;
	}
	
	@Override
	public void getType(byte[] marshalledBytes) throws IOException {
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

		type = din.readInt();
		timestamp = din.readLong();

		int identifierLength = din.readInt();
		byte[] identifierBytes = new byte[identifierLength];
		din.readFully(identifierBytes);
		m_info = new String(identifierBytes);
		
		baInputStream.close();
		din.close();		
	}
	
	@Override
	public byte[] getBytes() throws IOException {		
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

		dout.writeInt(type);
		dout.writeLong(timestamp);

		byte[] identifierBytes = m_info.getBytes();
		int elementLength = identifierBytes.length;
		dout.writeInt(elementLength);
		dout.write(identifierBytes);
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		baOutputStream.close();
		dout.close();
		return marshalledBytes;
	}
}
