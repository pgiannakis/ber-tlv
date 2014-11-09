package tlv;

import java.util.ArrayList;



/**
 * 
 * @author Panagiotis Giannakis
 *
 */
public class TlvTag extends TlvComponent{
	
	TlvTag tlvData;
	ArrayList<TlvComponent> tlvDataList;
	
	/**
	 * 
	 * @param tagName the tag field
	 * @param data the value field
	 */
	TlvTag (byte[] tagName, byte[] data){
		this.tagName = tagName;
		this.tagClassType = getClassType(data[0]);
		this.tagLength = data.length;
		this.tagDataObjectType = getDataObjectType(data[0]);
		this.tagValue = data;
	}
	
	/**
	 * 
	 * @param buffer a serialized tlv 
	 */
	TlvTag (byte[] buffer){
		this.buffer = buffer;
	}
	
	TlvTag(){	
	}
	
	public int tlvParse() throws TlvParsingException{
		int size = 0; 
		size = parse(buffer,0);
		tlvData =new TlvTag(tagName,tagValue);
		return size;
	}
	
	public static String toString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data){
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	 }
	
	@Override
	void parse() throws TlvParsingException {
		
		int offset = 0;
		tlvDataList = new ArrayList<TlvComponent>();
		while (offset < buffer.length){
			offset = parse(buffer,offset);
			tlvDataList.add(new TlvTag(tagName,tagValue));
		}
	}
	
	@Override
	public byte[] serialize(){
   	 	
		ArrayList<Byte> tagContents = new ArrayList<Byte>();
   	 	addRange(tagContents,tagName);
   	 	addRange(tagContents,createBerTlvLengthBytes(tagValue));
   	 	addRange(tagContents,tagValue);
   	 
   	 	return serialize(tagContents);
    }
	
	@Override
	public String toString(){

		StringBuilder sb = new StringBuilder();
		sb.append("Tag   : " + toString(tagName)+"\n");
		sb.append("Length: " + Integer.toString(tagLength)+"\n");
		sb.append("Value : " + toString(tagValue)+"\n");
		
		return sb.toString();
     }
}
