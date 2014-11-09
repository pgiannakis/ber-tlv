package tlv;

import java.util.ArrayList;
import java.util.Arrays;

import debug.DumpOfHEX;
/**
 * 
 * @author Panagiotis Giannakis
 * </p>Any tlv data objects can be further encapsulated in a context-specific template<p>
 */
public class TlvTemplate extends TlvComponent{
	
	private byte template;
	private ArrayList<TlvComponent> tlvTags;
	private byte[] constructedData;
	
	/**
	 * 
	 * @param template 
	 * @param tlvData tlv data objects which encapsulated in the template 
	 */
	public TlvTemplate(byte template, ArrayList<TlvComponent> tlvData){
		this.setTemplate(template);
		this.setTlvComponent(tlvData);
	}
	
	/**
	 * 
	 * @param constructedData a serialized template
	 */
	public TlvTemplate (byte[] constructedData){
		this.constructedData = constructedData;
	}
	
	/**
	 * parse a template data object
	 */
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
		for (TlvComponent tag : tlvTags){
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
		
		for (TlvComponent tag : tlvTags){
			sb.append("---------------------------\n");
			sb.append(tag.toString());
		}
		return sb.toString();
	}
	
	 /**
	  * 
	  * @param tagName  
	  * @return the tag value 
	  */
	 public byte[] searchTag(byte[] tagName){
		 ArrayList<Byte> tagValue =new ArrayList<Byte>();
		 for (TlvComponent currentTag : tlvTags){
			 if (Arrays.equals(currentTag.tagName,tagName)){			 
				 addRange(tagValue,currentTag.tagValue);
			 }
		 }
		 return serialize(tagValue);
	 }
	 
	 /**
	  * 
	  * @param tag data object element
	  */
	 public void addTlvComponent(TlvComponent tag) {
		 tlvTags.add(tag);
	 }
	
	 /**
	  * 
	  * @param index the index of data object element
	  */
	 public void removeTlvComponent(int index){
		 tlvTags.remove(index);
	 }
	
	/**
	 * 
	 * @param index the index of data object element
	 * @return the data object element
	 */
	public TlvComponent getChild(int index){
		return tlvTags.get(index);		
	}
	
	public byte getTemplate(){
		return tagName[0];
	}
	public void setTemplate(byte template) {
		this.template = template;
	}
	public ArrayList<TlvComponent> getTlvComponent() {
		return tlvTags;
	}
	public void setTlvComponent(ArrayList<TlvComponent> tlvTag) {
		this.tlvTags = tlvTag;
	}
}
