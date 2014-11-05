package tlv;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestTlvTemplate {

	
	@Test
	public void testSerialize(){
		TlvTag tlvtag1 = new TlvTag(new byte[]{(byte)0x9A},new byte[]{(byte)0x12,(byte)0x10,(byte)0x16}); 
		TlvTag tlvtag2 = new TlvTag(new byte[]{(byte)0x9F,(byte)0x21},new byte[]{(byte)0x16,(byte)0x29,(byte)0x04});
		
		ArrayList<TlvTag> tlvData = new ArrayList<TlvTag>();
		tlvData.add(tlvtag1);
		tlvData.add(tlvtag2);
		TlvTemplate tlvTemplate = new TlvTemplate((byte)0xE1,tlvData);
		
		byte[] serializedData = new byte[]{(byte)0xE1, (byte)0x0B, (byte)0x9A,(byte)0x03, (byte)0x12,(byte)0x10,(byte)0x16,(byte)0x9F,(byte)0x21,(byte)0x03,(byte)0x16,(byte)0x29,(byte)0x04};
		assertArrayEquals(serializedData,tlvTemplate.serialize());
	}
	
	@Test
	public void testSearchTag() throws TlvParsingException{
		TlvTemplate tlvTemplate = new TlvTemplate(new byte[]{(byte)0xE6,(byte)0x10,(byte)0xC3,(byte)0x01,(byte)0x02,(byte)0xC4,(byte)0x06,
															 (byte)0x50,(byte)0x49,(byte)0x4E,(byte)0x20,(byte)0x4F,(byte)0x4B,(byte)0xDF,
															 (byte)0xA1,(byte)0x02,(byte)0x01,(byte)0x02});
		
		byte[] tagName = new byte[]{(byte)0xDF, (byte)0xA1,(byte)0x02};
		byte[] tagValue = new byte[]{(byte)0x02};
		
		tlvTemplate.parse();
		assertArrayEquals(tagValue,tlvTemplate.searchTag(tagName));
	}

}
