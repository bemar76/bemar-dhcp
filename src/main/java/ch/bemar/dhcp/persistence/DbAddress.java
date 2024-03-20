package ch.bemar.dhcp.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbAddress {

	@Id
	@Column(unique = true, nullable = false)
	private String ip;

	private String subnet;
	private int defaultLeaseTime;
	private int maxLeaseTime;
	private int leasedUntil;
	private String hostname;
	private String reservedFor;
	private String leasedTo;
	long lastContact;
	boolean conflict;
	boolean arp;

}
