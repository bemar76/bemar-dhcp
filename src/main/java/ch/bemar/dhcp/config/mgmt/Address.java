package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.element.DefaultLeaseTime;
import ch.bemar.dhcp.config.element.MaxLeaseTime;
import ch.bemar.dhcp.config.element.Subnet;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Address implements IAddress {

	private InetAddress ip;
	private Subnet subnet;
	private int defaultLeaseTime;
	private int maxLeaseTime;
	private HardwareAddress reservedFor;
	private HardwareAddress leasedTo;
	long lastContact;
	@Setter
	boolean conflict;
	boolean arp;

	public Address(InetAddress ip, Subnet subnet, DefaultLeaseTime defaultLeaseTime, MaxLeaseTime maxLeaseTime) {
		super();
		this.ip = ip;
		this.subnet = subnet;

		if (defaultLeaseTime != null)
			this.defaultLeaseTime = defaultLeaseTime.getValue() * 1000;
		else
			this.defaultLeaseTime = 86400 * 1000;

		if (maxLeaseTime != null)
			this.maxLeaseTime = maxLeaseTime.getValue() * 1000;
		else
			this.maxLeaseTime = 604800 * 1000;

	}
	
	void setReservedFor(HardwareAddress hw) {
		this.reservedFor = hw;
	}

	public void setLeasedTo(HardwareAddress mac) {
		this.leasedTo = mac;
		this.lastContact = System.currentTimeMillis();
	}

	public void evictLeasing() {
		this.leasedTo = null;
		this.lastContact = 0;
	}

	@Override
	public InetAddress getAddress() {
		return ip;
	}

	@Override
	public InetAddress getSubnet() {
		return subnet.getValue();
	}

	@Override
	public int getLeaseTime() {
		if (maxLeaseTime < defaultLeaseTime) {
			return maxLeaseTime;
		}

		return defaultLeaseTime;
	}

	@Override
	public long getLeasedUntil() {
		return lastContact + getLeaseTime();
	}

	@Override
	public String toString() {
		return "Address [ip=" + ip + ", subnet=" + subnet + ", defaultLeaseTime=" + defaultLeaseTime + ", maxLeaseTime="
				+ maxLeaseTime + ", reservedFor=" + reservedFor + ", leasedTo=" + leasedTo + ", lastContact="
				+ lastContact + ", conflict=" + conflict + ", arp=" + arp + ", leasedUntil()=" + getLeasedUntil()
				+ "]";
	}

	

}
