package ch.bemar.dhcp.persistence;

public abstract class FirstSecond {

	protected abstract String getFirst();
	protected abstract String getSecond();

	private String current = null;

	public String getNext() {

		if (current == null) {

			current = getFirst();

		} else if (current.equals(getFirst())) {

			current = getSecond();
		}

		return current;
	}

	public String toString() {
		return getNext();
	}

}
