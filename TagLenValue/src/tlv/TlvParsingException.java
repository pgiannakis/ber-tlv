package tlv;

public class TlvParsingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message = null;
	public TlvParsingException() {
        super();
    }
	public TlvParsingException(String message) {
        super(message);
        this.message = message;
    }
	public TlvParsingException(Throwable cause) {
        super(cause);
    }
	@Override
	public String toString() {
		return message;
	}
	@Override
    public String getMessage() {
        return message;
    }
}
