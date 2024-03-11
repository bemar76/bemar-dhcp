package ch.bemar.dhcp.config.element;

public interface IConfigElement<T> {
	
	public String getKeyWord();
	
	public T getValue();

}
