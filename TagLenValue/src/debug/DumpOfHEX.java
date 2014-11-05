package debug;

import java.util.ArrayList;

public class DumpOfHEX {

	private static final byte[] HEX_CHAR = new byte[]
		      { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static final String dumpBytes( byte[] buffer )
	{
		if ( buffer == null )
		{
			return "";
		}
		
		StringBuffer sb = new StringBuffer();

		for ( int i = 0; i < buffer.length; i++ )
		{
			sb.append( "0x" ).append( ( char ) ( HEX_CHAR[( buffer[i] & 0x00F0 ) >> 4] ) ).append(
					( char ) ( HEX_CHAR[buffer[i] & 0x000F] ) ).append( " " );
		}
		return sb.toString();
	  }
	public static final String dumpBytes (ArrayList<Byte> buffer)
	{

		if (buffer == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();
		
		for ( int i = 0; i < buffer.size(); i++)
		{
			sb.append( "0x" ).append( ( char ) ( HEX_CHAR[( buffer.get(i) & 0x00F0 ) >> 4] ) ).append(
					( char ) ( HEX_CHAR[buffer.get(i) & 0x000F] ) ).append( " " );
		}
		return sb.toString();
	}
}
