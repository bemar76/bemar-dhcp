package ch.bemar.dhcp.persistence.model;

import lombok.Data;

@Data
public class DbLease {

	@Id
	private String ip;
	private String hostname;
	private String leasedTo;
	private Long lastContact;

}
