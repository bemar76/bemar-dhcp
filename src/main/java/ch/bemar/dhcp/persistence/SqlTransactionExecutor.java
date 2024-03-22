package ch.bemar.dhcp.persistence;

@FunctionalInterface
public interface SqlTransactionExecutor {
    
	 void updateInTransaction(DbAddress address);
}