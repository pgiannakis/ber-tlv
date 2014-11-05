package tlv;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTlvTag {
	@Test
	public void testSerialize(){
		TlvTag tlvTag = new TlvTag(new byte[]{(byte)0x9F, (byte)0x41}, 
								   new byte[]{(byte)0xF1, (byte)0x23,(byte)0x57,(byte)0xA3});
		byte[] serializedData = new byte[]{(byte)0x9F, (byte)0x41, (byte)0x04,(byte)0xF1, (byte)0x23,(byte)0x57,(byte)0xA3};
		assertArrayEquals(serializedData,tlvTag.serialize());
	}
	
	@Test
	public void testParse() throws TlvParsingException{
		TlvTag tlvTag = new TlvTag(new byte[]{(byte)0x9F, (byte)0x21, (byte)0x03, (byte)0x16, (byte)0x29, (byte)0x44});
		assertEquals(6,tlvTag.tlvParse());
	}
}
