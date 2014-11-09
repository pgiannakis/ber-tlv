/**
 * <H1>Tag Length and Value (TLV)
 */
package tlv;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
/**
 * 
 * @author Panagiotis Giannakis
 *
 */


public abstract class TlvComponent {
	/**
	 * 
	 * class type of tag
	 * <li>{@link #Unknown}</li>
	 * <li>{@link #UniversalClass}</li>
	 * <li>{@link #ApplicationClass}</li>
	 * <li>{@link #ContextSpecificClass}</li>
	 * <li>{@link #PrivateClass}</li>
	 */
	public enum TlvClassType{
		/**
		 * in case of unknown class type
		 */
		Unknown,
		/**
		 * universal class type
		 */
		UniversalClass,
		/**
		 * application class type
		 */
	    ApplicationClass,
	    /**
	     * context specific class type
	     */
	    ContextSpecificClass,
	    /**
	     * private class type
	     */
	    PrivateClass
	}

	/**
	 * data object of tag
	 * <li>{@link #Unknown}</li>
	 * <li>{@link #PrimitiveDataObject}</li>
	 * <li>{@link #ContructedDataObject}</li>
	 *
	 */
	public enum TlvDataObjectType{
		/**
		 * in case of unknown data object type
		 */
		Unknown,
		/**
		 * primitive data object
		 */
		PrimitiveDataObject,
		/**
		 * constructed data object
		 */
	    ContructedDataObject
	}

    /**
     * byte 0x40 = 0100 0000
     */
	private static final byte APPLICATION_CLASS_MASK =			(byte) 0x40;    
	/**
     * byte 0x80 = 1000 0000
     */
	private static final byte CONTEXT_SPECIFIC_CLASS_MASK = 	(byte) 0x80;
	/**
     * byte 0xC0 = 1100 0000
     */
	private static final byte PRIVATE_CLASS_MASK = 				(byte) 0xC0;     
	/**
	 * byte 0x20 = 0010 0000
	 */
	private static final byte CONSTRUCTED_DATAOBJECT_MASK = 	(byte) 0x20;
	/**
	 * byte 0x1F = 0001 1111 used for first tag-byte
	 */
	private static final byte MULTI_BYTE_TAG_MASK = 			(byte) 0x1F;     
	/**
	 * byte 0x80 = 1000 0000 used for subsequent tag-bytes
	 */
	private static final byte MULTI_BYTE_TAG_2_MASK = 			(byte) 0x80;     
	/**
	 * byte 0x80 = 1000 0000 
	 */
	private static final byte MULTI_BYTE_LENGTH_MASK = 			(byte) 0x80;     
	/**
	 * byte 0x7F = 0111 1111
	 */
	private static final byte SINGLE_BYTE_LENGTH_MASK = 		(byte) 0x7F;     
     
	public TlvClassType tagClassType;
	public TlvDataObjectType tagDataObjectType;
	public byte[] tagName;
	public int tagLength;
	public byte[] tagValue;
	public byte[] buffer;
    
	/**
	 * 
	 * @param data a byte array of tlv
	 * @param offset the offset of byte array
	 * @return the offset after a successfully tlv parsing 
	 * @throws TlvParsingException
	 */
    public int parse(byte[] data, int offset) throws TlvParsingException{
    	// get class class type
    	tagClassType = getClassType(data[offset]);
         
        // get data object type
        tagDataObjectType = getDataObjectType(data[offset]);

        // and parse tag
        byte[] tag = getTag(data, offset);
        tagName = new byte[tag.length];
        offset+=tag.length;
        System.arraycopy(tag,0,tagName,0, tag.length);
   
        if (offset >= data.length){
        	throw new TlvParsingException("No length for TLV tag");
        }      
        
        tagLength = getLength(data,offset);
        offset += getLengthSize(data[offset]);
        if (tagLength > (data.length - offset)){
         throw new TlvParsingException("Insufficient data for TLV tag");
        }

        tagValue = new byte[tagLength];
        System.arraycopy(data, offset, tagValue, 0, tagLength);
        offset += tagLength;

        return offset;
    }
    
    /**
     * 
     * @param valueField the byte array of tlv value field
     * @return a byte array representing the tlv length filed
     */
    public byte[] createBerTlvLengthBytes(byte[] valueField){
    	byte[] lenBytes = getBytesFromInt(valueField.length);

    	if (valueField.length <= 127){
    		return lenBytes;
    	}
         
        byte[] tlvLenBytes = new byte[lenBytes.length + 1];
        tlvLenBytes[0] = (byte)(((byte)lenBytes.length) | (byte)MULTI_BYTE_LENGTH_MASK);
          
        System.arraycopy(lenBytes, 0, tlvLenBytes, 1, lenBytes.length);
        return tlvLenBytes;
    }
    
    /**
    * 
    * @param value an integer value
    * @return the hex value of the integer
    */
    public byte[] getBytesFromInt(int value){    	 
    	String s = Integer.toHexString(value);
    	int numBytes = (int)Math.ceil((double)(s.length() / 2f));
    	byte[] paddedBytes = getBytes(value);  
         
    	byte[] result = new byte[numBytes];
    	System.arraycopy(paddedBytes, paddedBytes.length - numBytes, result, 0, numBytes);
    	return result;
    }
     
    /**
     * 
     * @param data the first byte of length field
     * @return the number of bytes that represent the length field 
     * <br><b>example:</b>
     * <table>
     * <tr align = center><th>data</th><th>return</th><th>description</th></tr>
     * <tr align = center><td>0x82</td><td>3</td><td>the length filed is reprenested by 3 bytes</td></tr>
     * <tr align = center><td>0x65</td><td>1</td><td>the length filed is reprenested by 1 bytes</td></tr>
     * </table>
     */
    public int getLengthSize(byte data){
    	 
    	 int size = 1;
    	 if ((data & MULTI_BYTE_LENGTH_MASK) == MULTI_BYTE_LENGTH_MASK){
    		 size += (int)(data & SINGLE_BYTE_LENGTH_MASK);
    	 } 	 
    	 return size;
     }
    
    /**
     * 
     * @param data byte array with a sequence of tlv data objects 
     * @param offset the offset of byte array
     * @return the length of value field
     */
    public int getLength(byte[] data, int offset){
    	int len = (int)(data[offset] & SINGLE_BYTE_LENGTH_MASK);
    	
    	if ((data[offset] & MULTI_BYTE_LENGTH_MASK) == MULTI_BYTE_LENGTH_MASK){ 
    		ArrayList<Byte> lenBytes = new ArrayList<Byte>();
    		for (int i = 0; i < len; i++){
    			lenBytes.add(data[offset]);
    			offset++;
    		}
    		
            lenBytes.toArray();
            int size = lenBytes.size();
             
            byte[] paddedLengthBytes = new byte[4]; 
            for (int j = 0; j < 4 - size; j++){
            	lenBytes.add(j, (byte)0);
            }
            paddedLengthBytes = serialize(lenBytes);
            len = fromByteArray(paddedLengthBytes);
    	}
    	return len;
    }
     
    /**
     * 
     * @param tag the first byte of a tag
     * @return the class type {@link TlvClassType }
     */
    protected TlvClassType getClassType(byte tag){ 

    	TlvClassType type = TlvClassType.Unknown;
    	if ((tag & PRIVATE_CLASS_MASK) == PRIVATE_CLASS_MASK){
    		type = TlvClassType.PrivateClass;
        }else if ((tag & CONTEXT_SPECIFIC_CLASS_MASK) == CONTEXT_SPECIFIC_CLASS_MASK){
            type = TlvClassType.ContextSpecificClass;
        }else if ((tag & APPLICATION_CLASS_MASK) == APPLICATION_CLASS_MASK){
            type = TlvClassType.ApplicationClass;
        }else{
            type = TlvClassType.UniversalClass;
        }
        return type;
    }
    
     /**
      * 
      * @param tag the first byte of a tag
      * @return the data object type {@link TlvDataObjectType }
      */
     protected TlvDataObjectType getDataObjectType(byte tag){
    	 
    	 TlvDataObjectType type = TlvDataObjectType.Unknown;
         if ((tag & CONSTRUCTED_DATAOBJECT_MASK) == CONSTRUCTED_DATAOBJECT_MASK){
        	 type = TlvDataObjectType.ContructedDataObject;
         }else{
            type = TlvDataObjectType.PrimitiveDataObject;
         }      
         return type;
     }
     
     /**
      * 
      * @param data byte array with a sequence of tlv data objects 
      * @param offset the offset of byte array
      * @return the tag filed of tlv data object
      */
     protected byte[] getTag(byte[] data, int offset){ 

    	// Parse the Tag
     	
     	// step one
        // get 1st tag-byte
    	ArrayList<Byte> tag = new ArrayList<Byte>();
        boolean isNextByteTag = false;
        tag.add(data[offset]);
         
        // step two
        // check if the tag is multiple byte 
        if ((data[offset] & MULTI_BYTE_TAG_MASK) == MULTI_BYTE_TAG_MASK){
        	isNextByteTag = true;
        }else{
        	isNextByteTag = false;
        }
        
        offset++;
         
        // step three
        // get subsequent tag-bytes
        while (isNextByteTag && offset < data.length){ 
        	if ((data[offset] & MULTI_BYTE_TAG_2_MASK) == MULTI_BYTE_TAG_2_MASK)
            {
                isNextByteTag = true;
            }else{
                isNextByteTag = false;
            }

            tag.add(data[offset]);
            offset++;
        }
        return serialize(tag);
     }
     
     public  byte[] serialize(ArrayList<Byte> list) {
    	 byte []toArray = new byte[list.size()];
    	 
    	 for (int i = 0; i<list.size();i++){
    		 toArray[i]=list.get(i);
    	 }
    	 return toArray;
     }

     int fromByteArray(byte[] bytes) {
    	 return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
     }
 
     public void addRange(ArrayList<Byte> list, byte[] addValue){
    	 for (int i=0; i< addValue.length; i++ ){
    		 list.add(addValue[i]);
    	 }
    }
     
    public byte[] getBytes(int value){
    	ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }
    
    abstract void parse() throws TlvParsingException;
    abstract byte[] serialize();
    abstract public String toString();
}
