package ch.bemar.dhcp.persistence;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.mgmt.IAddress;

public interface IService  extends IDao<IAddress, HardwareAddress, InetAddress>{

	

}
