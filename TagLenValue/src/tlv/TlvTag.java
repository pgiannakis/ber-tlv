package tlv;

import java.util.ArrayList;




public class TlvTag extends TlvComponent{
	
	TlvTag tlvData;
	ArrayList<TlvTag> tlvDataList;
	
	TlvTag (byte[] tagName, byte[] data){
		this.tagName = tagName;
		this.tagClassType = getClassType(data[0]);
		this.tagLength = data.length;
		this.tagDataObjectType = getDataObjectType(data[0]);
		this.tagValue = data;
	}
	
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
	
	public String toString(){

		StringBuilder sb = new StringBuilder();
		sb.append("Tag   : " + toString(tagName)+"\n");
		sb.append("Length: " + Integer.toString(tagLength)+"\n");
		sb.append("Value : " + toString(tagValue)+"\n");
		
		return sb.toString();
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
		tlvDataList = new ArrayList<TlvTag>();
		while (offset < buffer.length){
			offset = parse(buffer,offset);
			tlvDataList.add(new TlvTag(tagName,tagValue));
		}
	}
	
	public byte[] serialize(){
   	 	
		ArrayList<Byte> tagContents = new ArrayList<Byte>();
   	 	addRange(tagContents,tagName);
   	 	addRange(tagContents,createBerTlvLengthBytes(tagValue));
   	 	addRange(tagContents,tagValue);
   	 
   	 	return serialize(tagContents);
    }
}
