package tlv;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestTlvComponent extends TlvComponent{

	@Rule 
	public ExpectedException thrown = ExpectedException.none(); 
	
	@Test
	public void testParse_throwTlvParsingExceptionNoLengthForTlvTag() throws TlvParsingException{	
		byte[] buffer = new byte[] {(byte)0xDF,(byte)0xA3,(byte)0x01}; 			
		
		thrown.expect(TlvParsingException.class);
		thrown.expectMessage("No length for TLV tag");
		parse(buffer,0);
	   }
	
	@Test
	public void testParse_throwTlvParsingExceptionInsufficientDataForTlvTag() throws TlvParsingException{	
		byte[] buffer = new byte[] {(byte)0xDF,(byte)0xA3,(byte)0x01,(byte)0x13,(byte)0xF3, (byte)0x6B,(byte)0x11,(byte)0xFA}; 			
		
		thrown.expect(TlvParsingException.class);
		thrown.expectMessage("Insufficient data for TLV tag");
		parse(buffer,0);
	   }
	
	@Test
	public void testParse_parsTlv() throws TlvParsingException{
		byte[] buffer = new byte[] {(byte)0xDF,(byte)0xA3,(byte)0x01,(byte)0x03,(byte)0xF3, (byte)0x6B,(byte)0x11,(byte)0xFA}; 
		assertEquals(7,parse(buffer,0));
	}
	
	@Test
	public void testGetClassType_universalClass(){
		assertEquals(TlvComponent.TlvClassType.UniversalClass,getClassType((byte)0x3F));
	}
	
	@Test
	public void testGetClassType_applicationClass(){
		assertEquals(TlvComponent.TlvClassType.ApplicationClass,getClassType((byte)0x42));
	}
	
	@Test
	public void testGetClassType_contextSpecificClass(){
		assertEquals(TlvComponent.TlvClassType.ContextSpecificClass,getClassType((byte)0x88));
	}
	
	@Test
	public void testGetClassType_privateClass(){
		assertEquals(TlvComponent.TlvClassType.PrivateClass,getClassType((byte)0xEC));
	}
	
	@Test
	public void testGetDataObjectType_contructedDataObject(){
		assertEquals(TlvComponent.TlvDataObjectType.ContructedDataObject,getDataObjectType((byte)0xE7));
	}
	
	@Test
	public void testGetDataObjectType_primitiveDataObject(){
		assertEquals(TlvComponent.TlvDataObjectType.PrimitiveDataObject,getDataObjectType((byte)0xDC));
	}
	
	@Test
	public void testGetBytesFromInt_maxInteger(){
		assertArrayEquals(new byte[] {(byte) 0x7F,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF},getBytesFromInt((int) (Math.pow(2, 31)-1)));
	}
	
	@Test
	public void testGetBytesFromInt_minInteger(){
		assertArrayEquals(new byte[] {(byte) 0x80,(byte) 0x00,(byte) 0x00,(byte) 0x00},getBytesFromInt((int) (Math.pow(-2, 31))));
	}
	
	@Test
	public void testGetBytesFromInt_zeroInteger(){	
		assertArrayEquals(new byte[] {(byte) 0x00},getBytesFromInt(0));
	}
	
	@Test
	public void testCreateBerTlvLengthBytes_upTo127Bytes(){
		byte[] data = new byte[127];
		assertArrayEquals(new byte[] {(byte) 0x7F},createBerTlvLengthBytes(data));
	}
	
	
	@Test
	public void tetsCreateBerTlvLengthBytes_upTo255Bytes(){
		byte[] data = new byte[255];
		assertArrayEquals(new byte[] {(byte) 0x81,(byte) 0xFF},createBerTlvLengthBytes(data));
	}
	@Test
	public void testCreateBerTlvLengthBytes_multiByteLengthMask(){
		byte[] data = new byte[260];
		assertArrayEquals(new byte[] {(byte) 0x82,(byte) 0x01,(byte) 0x04},createBerTlvLengthBytes(data));
	}
	
	@Test
	public void testGetTag_singleByteTag(){
		byte[] buffer = new byte[]{(byte)0x98,(byte)0x30,(byte)0xF1,};
		assertArrayEquals(new byte[] {(byte) 0x98},getTag(buffer,0));
	}
	
	@Test
	public void testGetTag_multiByteTag(){
		byte[] buffer = new byte[]{(byte)0xF3,(byte)0x9F,(byte)0x20,(byte)0x11};
		assertArrayEquals(new byte[] {(byte) 0x9F,(byte) 0x20},getTag(buffer,1));
	}
	
	@Test
	public void testGetTag_multi2ByteTag(){
		byte[] buffer = new byte[]{(byte)0xF3,(byte)0x31,(byte)0xDF,(byte)0xA3,(byte)0x03,(byte)0x45};
		assertArrayEquals(new byte[] {(byte)0xDF,(byte)0xA3,(byte)0x03},getTag(buffer,2));
	}
	
	@Test
	public void testGetLengthSize_multiByteLengthMask() {
		assertEquals(3,getLengthSize((byte)0x82));
	}
	
	@Test
	public void testGetLengthSize_singleByteLengthMask(){
		assertEquals(1,getLengthSize((byte)0x12));
	}

	@Override
	void parse() throws TlvParsingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
