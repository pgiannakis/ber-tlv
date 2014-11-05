package tlv;

import java.util.ArrayList;

import debug.DumpOfHEX;

public class Sample {

	/**
	 * @param args
	 * @throws TlvParsingException 
	 */
	public static void main(String[] args) throws TlvParsingException {
		
		byte[] tagName = new byte[]{(byte) 0xDF,(byte) 0xAE,(byte) 0x05};
		byte[] tagValue = new byte[]{(byte) 0xA1,(byte) 0x23, (byte) 0xB7, (byte) 0x99, (byte) 0xC1};
		TlvTag tag = new TlvTag(tagName,tagValue);
		
		System.out.println("=====TLV=====");
		System.out.println("serialize TLV :"+ DumpOfHEX.dumpBytes(tag.serialize()));
		
		byte[] buffer = new byte[]{(byte)0x9F,(byte)0x37,(byte)0x04,(byte)0x01,(byte)0x01,(byte)0x15,(byte)0x20}; 
		
		System.out.println("\nbuffer :" + DumpOfHEX.dumpBytes(buffer));
		System.out.println("parse");
		tag = new TlvTag(buffer);
		tag.parse();
		
		System.out.println("parse data");
		System.out.println(tag.toString());
		
		System.out.println("=====Template=====");
		byte[] tagName1 = new byte[]{(byte)0x81};
		byte[] tagValue1 = new byte[]{(byte)0xA2,(byte)0x76,(byte)0xA1,(byte)0xC3,(byte)0x55};
		TlvTag tag1 = new TlvTag(tagName1,tagValue1);
        
		byte[] tagName2 = new byte[]{(byte)0x5F,(byte)0x24};
		byte[] tagValue2= new byte[]{(byte)0x05,(byte)0x12,(byte)0x20}; 
		TlvTag tag2 = new TlvTag(tagName2,tagValue2);
		
		ArrayList<TlvTag> tags = new ArrayList<TlvTag>();
        tags.add(tag1);
        tags.add(tag2);

        TlvTemplate tlvTemplate = new TlvTemplate((byte)0xE0, tags);
        
        System.out.println("serialize Tempate: "+DumpOfHEX.dumpBytes(tlvTemplate.serialize()));
        
		buffer = new byte[]{(byte)0xE1,(byte)0x0C,(byte)0x9F,(byte)0x37,(byte)0x04,(byte)0x01,(byte)0x01,
							(byte)0x15,(byte)0x20,(byte)0x81,(byte)0x03,(byte)0xA2,(byte)0x76,(byte)0xA1};    
		
		
		System.out.println("\nbuffer   :" + DumpOfHEX.dumpBytes(buffer));
		System.out.println("parse");
		tlvTemplate = new TlvTemplate(buffer); 
		tlvTemplate.parse();
		System.out.println(tlvTemplate.toString());
	}
}
