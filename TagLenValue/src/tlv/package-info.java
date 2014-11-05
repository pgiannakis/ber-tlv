/**
 * <H1>Tag Length and Value (TLV)</H1>
 * Based in 'Rules for BER-TLV Data Objects' EMV 4.3 , Book 3 , Annex B
 * <P>As defined in ISO/IEC 8825, a BER-TLV data object consists of 2-3 consecutive fields:</P>
 * <li>The tag field (T) consists of one or more consecutive bytes. It indicates a class, a type, 
 * and a number (see Table 35). The tag field of the data objects described in this specification is coded on one or two bytes.</li>
 * <li>The length field (L) consists of one or more consecutive bytes. It indicates the length of the following field. 
 * The length field of the data objects described in this specification which are transmitted over the card-terminal 
 * interface is coded on one or two bytes.<BR>
 * <B>Note:</B> Three length bytes may be used if needed for templates '71' and '72' and tag '86' 
 * (to express length greater than 255 bytes), as they are not transmitted over the card-terminal interface.</li>
 * <li>The value field (V) indicates the value of the data object. If L = '00', the value field is not present.</li>
 * <BR><BR>
 * <P>A BER-TLV data object belongs to one of the following two categories:</P>
 * <li>A primitive data object where the value field contains a data element for financial transaction interchange.</li>
 * <li>A constructed data object where the value field contains one or more primitive or constructed data objects. 
 * The value field of a constructed data object is called a template.</li>
 * <BR><BR>
 * The coding of BER-TLV data objects is defined as follows.
 * 
 * <H2>Coding of the Tag Field of BER-TLV Data Objects</H2>
 * <P>the table below describes the first byte of the tag field of a BER-TLV data object:</P>
 * <BR><BR>
 *<table>
 *	<thead align = left>
 *		<tr><th>b8</th><th>b7</th><th>b6</th><th>b5</th><th>b4</th><th>b3</th><th>b2</th><th>b1</th><th>Meaning</th></tr>
 *	</thead>
 *	<tbody>
 *		<tr><td>0</td><td>0</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Universal class</td></tr>
 *		<tr><td>0</td><td>1</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Application class</td></tr>
 *		<tr><td>1</td><td>0</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Context-specific class</td></tr>
 *		<tr><td>1</td><td>1</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Private class</td></tr>
 *		<tr><td></td><td></td><td>0</td><td></td><td></td><td></td><td></td><td></td><td>Primitive data object</td></tr>
 *		<tr><td></td><td></td><td>1</td><td></td><td></td><td></td><td></td><td></td><td>Constructed data object</td></tr>
 *		<tr><td></td><td></td><td></td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td><td>See subsequent bytes</td></tr>
 *		<tr><td></td><td></td><td></td><td colspan="5">Any other value <31</td><td>Tag number</td></tr>
 *	</tbody>
 *<H4>Tag Field Structure (First Byte) BER-TLV</H4>
 *</table>
 *<BR><BR>
 *<P> According to ISO/IEC 8825, Table 36 defines the coding rules of the subsequent bytes of a BER-TLV tag when tag numbers >= 31 are used (that is, bits b5 - b1 of the first byte equal '11111').</P>
 *<H4>Tag Field Structure (Subsequent Bytes) BER-TLV</H4>
 *<table>
 *	<thead align = left>
 *	<tr><th>b8</th><th>b7</th><th>b6</th><th>b5</th><th>b4</th><th>b3</th><th>b2</th><th>b1</th><th>Meaning</th></tr>
 *	</thead>
 *	<tbody>
 *		<tr><td>1</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Another byte follows</td></tr>
 *		<tr><td>0</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Another byte follows</td></tr>
 *		<tr><td></td><td colspan="7">Any value > 0</td><td>(Part of) tag number</td></tr>
 *	</tbody>
 *</table>
 *<BR><BR>
 *<P>Before, between, or after TLV-coded data objects, '00' bytes without any meaning may occur (for example, due to erased or modified TLV-coded data objects).</P>
 *<P><B>Note:</B> It is strongly recommended that issuers do not use tags beginning with ‘FF’ for proprietary purposes, as existing terminals may not recognise ‘FF’ as the beginning of a constructed private class tag.</P>
 *<BR><BR>
 *<P>The tag field of a BER-TLV data object is coded according to the following rules:</P>
 *<li>The following application class templates defined in ISO/IEC 7816 apply: '61' and '6F'</li>
 *<li>The following range of application class templates is defined in Part II: '70' to '7F'. The meaning is then specific to the context of an application according to this specification. Tags '78', '79', '7D', and '7E' 
 *are defined in ISO/IEC 7816-6 and are not used in this specification.</li>
 *<li>The application class data objects defined in ISO/IEC 7816 and described in Part II are used according to the ISO/IEC 7816 definition.</li>
 *<li>Context-specific class data objects are defined in the context of this specification or in the context of the template in which they appear.</li>
 *<li>The coding of primitive context-specific class data objects in the ranges '80' to '9E' and '9F00' to '9F4F' is reserved for this specification.</li>
 *<li>The coding of primitive context-specific class data objects in the range '9F50' to '9F7F' is reserved for the payment systems.</li>
 *<li>The coding of tag 'BF0C' and constructed context-specific class data objects in the range 'BF20' to 'BF4F' is reserved for this specification.</li>
 *<li>The coding of constructed context-specific class data objects in the ranges 'BF10' to 'BF1F' and 'BF50' to 'BF6F' is reserved for the payment systems.</li>
 *<li>The coding of constructed context-specific class data objects in the ranges 'BF01' to 'BF0B', 'BF0D' to 'BF0F', and 'BF70' to 'BF7F' 
 *is left to the discretion of the issuer.</li>
 *<li>The coding of primitive and constructed private class data objects is left to the discretion of the issuer.</li>
 *
 *<H2>Coding of the Length Field of BER-TLV Data Objects</H2>
 *<P>When bit b8 of the most significant byte of the length field is set to 0, the length field consists of only one byte. 
 *Bits b7 to b1 code the number of bytes of the value field. The length field is within the range 1 to 127.</P>
 *<P>When bit b8 of the most significant byte of the length field is set to 1, the subsequent bits b7 to b1 of the most significant byte code the number of 
 *subsequent bytes in the length field. The subsequent bytes code an integer representing the number of bytes in the value field. Two bytes are necessary to 
 *express up to 255 bytes in the value field.</P>
 *<H2>Coding of the Value Field of Data Objects</H2>
 *<P>A data element is the value field (V) of a primitive BER-TLV data object. A data element is the smallest data field that receives an identifier (a tag).</P>
 *<BR><BR>
 *<P>A primitive data object is structured as illustrated below</P>
 *<H4>Primitive BER-TLV Data Object (Data Element)</H4>
 *<table border="1">
 *<tbody>
 *<tr><td>Tag (T)</td><td>Length (L)</td><td>Value (V)</td></tr>
 *</tbody>
 *</table>
 *<BR><BR>
 *<P>A constructed BER-TLV data object consists of a tag, a length, and a value field composed of one or more BER-TLV data objects. A record in an AEF governed by 
 *this specification is a constructed BER-TLV data object</P>
 *<H4>Constructed BER-TLV Data Object</H4>
 *<table border="1">
 *<tbody>
 *<tr><td>Tag (T)</td><td>Length (L)</td><td>Primitive or constructed BER-TLV data object number 1</td><td>...</td><td>Primitive or constructed BER-TLV data object number n</td></tr>
 *</tbody>
 *</table>
 */
package tlv;

