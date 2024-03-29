package ch.bemar.dhcp.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IEntityFactory<T> {
	
	public T getFromResultSet(ResultSet rs) throws SQLException;

}
