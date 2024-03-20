package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

public interface IAddress extends Comparable<IAddress>{

	public InetAddress getAddress();

	public InetAddress getSubnet();

	public int getLeaseTime();

	public long getLeasedUntil();

	public long getLastContact();

	public HardwareAddress getReservedFor();

	public HardwareAddress getLeasedTo();
}
