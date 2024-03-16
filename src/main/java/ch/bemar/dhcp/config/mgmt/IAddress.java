package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;

public interface IAddress {

	public InetAddress getAddress();

	public InetAddress getSubnet();

	public int getLeaseTime();

	public long getLeasedUntil();
}
