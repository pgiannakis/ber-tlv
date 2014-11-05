package tlv;

import java.util.ArrayList;
import java.util.Arrays;

import debug.DumpOfHEX;

public class TlvTemplate extends TlvComponent{
	
	//public ArrayList<TlvTag> tags;
	private byte template;
	//private ArrayList<TlvTag> tlvTags;
	private ArrayList<TlvTag> tlvTags;
	private byte[] constructedData;
	
	public TlvTemplate(byte template, ArrayList<TlvTag> tlvData){
		this.setTemplate(template);
		this.setTlvTag(tlvData);
	}
	
	public TlvTemplate (byte[] constructedData){
		this.constructedData = constructedData;
	}
	
	public void parse() throws TlvParsingException{
		
		int offset = 0;
		offset= parse(constructedData,offset);
		offset=0;
		TlvTag tlvTag = new TlvTag (tagValue);
		tlvTag.parse();
		tlvTags = tlvTag.tlvDataList; 
	}
		
	
	
	public byte[] serialize(){
		ArrayList<Byte> tagData = new ArrayList<Byte>();
		for (TlvTag tag : tlvTags){
			addRange(tagData,tag.serialize());	
        }
		
		tagValue = serialize(tagData);
		ArrayList <Byte> constructedTemplate = new ArrayList<Byte>();
		addRange(constructedTemplate,new byte[]{template});
   	 	addRange(constructedTemplate,createBerTlvLengthBytes(tagValue));
   	 	addRange(constructedTemplate,tagValue);
		
		return serialize(constructedTemplate);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Template: " + TlvTag.toString(tagName)+"\n");  
		sb.append("Length  : " + Integer.toString(tagLength)+"\n");
		sb.append("Value   : ");
		sb.append(TlvTag.toString(tagValue)+"\n");
		sb.append(" ** Tags ** \n");
		
		for (TlvTag tag : tlvTags){
			sb.append("---------------------------\n");
			sb.append(tag.toString());
		}
		return sb.toString();
	}
	 
	 public byte[] searchTag(byte[] tag){
		 ArrayList<Byte> tagValue =new ArrayList<Byte>();
		 for (TlvTag currentTag : tlvTags){
			 if (Arrays.equals(currentTag.tagName,tag)){			 
				 addRange(tagValue,currentTag.tagValue);
			 }
		 }
		 return serialize(tagValue);
	 }
	 
	public void addTlv(TlvTag tag) {
		tlvTags.add(tag);
	}
	
	public void removeTlv(int index){
		tlvTags.remove(index);
	}
	
	public TlvTag getTlv(int index){
		return tlvTags.get(index);		
	}
	
	public byte getTemplate()
	{
		return tagName[0];
	}
	public void setTemplate(byte template) {
		this.template = template;
	}
	public ArrayList<TlvTag> getTlvData() {
		return tlvTags;
	}
	public void setTlvTag(ArrayList<TlvTag> tlvTag) {
		this.tlvTags = tlvTag;
	}
}
