package ch.bemar.dhcp.config.lease;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.element.DefaultLeaseTime;
import ch.bemar.dhcp.config.element.MaxLeaseTime;
import ch.bemar.dhcp.config.element.Subnet;
import lombok.Data;

@Data
public class LeaseAddress implements IAddress {

	private InetAddress ip;
	private Subnet subnet;
	private int defaultLeaseTime;
	private int maxLeaseTime;
	private String hostname;
	private HardwareAddress reservedFor;
	private HardwareAddress leasedTo;
	long lastContact;

	public LeaseAddress() {

	}

	public LeaseAddress(InetAddress ip, Subnet subnet, DefaultLeaseTime defaultLeaseTime, MaxLeaseTime maxLeaseTime) {
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

	public void setReservedFor(HardwareAddress hw) {
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
		if (subnet != null)
			return subnet.getValue();

		return null;
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
				+ lastContact + ", leasedUntil()=" + getLeasedUntil() + ", hostname=" + hostname + "]";
	}

	@Override
	public int compareTo(IAddress o) {

		if (this.getAddress().equals(o.getAddress())) {
			return 0;
		}

		boolean isIPv4First = getAddress().getAddress().length == 4;
		boolean isIPv4Second = o.getAddress().getAddress().length == 4;

		if (isIPv4First && !isIPv4Second) {
			return -1;
		} else if (!isIPv4First && isIPv4Second) {
			return 1;
		}

		byte[] bytes1 = getAddress().getAddress();
		byte[] bytes2 = o.getAddress().getAddress();

		for (int i = 0; i < bytes1.length; i++) {
			int b1 = bytes1[i] & 0xFF;
			int b2 = bytes2[i] & 0xFF;
			if (b1 != b2) {
				return b1 - b2;
			}
		}

		return 0;
	}

}
