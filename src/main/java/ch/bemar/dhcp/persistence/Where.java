package ch.bemar.dhcp.persistence;

public class Where extends FirstSecond {

	public static final String WHERE = "WHERE";
	public static final String AND = "AND";

	public String toString() {
		return " " + getNext() + " ";
	}

	@Override
	protected String getFirst() {
		return WHERE;
	}

	@Override
	protected String getSecond() {
		return AND;
	}

}
