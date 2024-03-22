package ch.bemar.dhcp.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbLease {

	@Id
	@Column(unique = true, nullable = false)
	private String ip;
	private String hostname;
	private String leasedTo;
	private long lastContact;

}
