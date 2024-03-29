package ch.bemar.dhcp.persistence.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.bemar.dhcp.persistence.IEntityFactory;

public class DbLeaseFactory implements IEntityFactory<DbLease>{

	@Override
	public DbLease getFromResultSet(ResultSet rs) throws SQLException {
		return createDbLeaseFromResultSet(rs);
	}
	
	public DbLease createDbLeaseFromResultSet(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet darf nicht null sein.");
        }
        
        DbLease lease = new DbLease();
        lease.setIp(rs.getString("ip"));
        lease.setHostname(rs.getString("hostname"));
        lease.setLeasedTo(rs.getString("leasedTo"));
        lease.setLastContact(rs.getLong("lastContact"));
        
        return lease;
    }

}
