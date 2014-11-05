package tlv;

public class TlvData {
	
	private byte[] tag;
	private int len;
	private byte[] value;
	public TlvData(byte[] tag, int len, byte[] value){
		this.setTag(tag);
		this.setLen(len);
		this.setValue(value);
	}
	public TlvData(byte[] tag, byte[] value){
		this.setTag(tag);
		this.setLen(0);
		this.setValue(value);
	}
	
	public byte[] getTag() {
		return tag;
	}
	public void setTag(byte[] tag) {
		this.tag = tag;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
}
