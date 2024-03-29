package ch.bemar.dhcp.persistence;

public class Comma extends FirstSecond {

	public static final String FIRST = "";
	public static final String SECOND = ",";

	public String toString() {
		return getNext() + " ";
	}

	@Override
	protected String getFirst() {
		return FIRST;
	}

	@Override
	protected String getSecond() {
		return SECOND;
	}

}
