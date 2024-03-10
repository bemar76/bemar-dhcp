package ch.bemar.dhcp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPCoreServer;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;
import org.dhcp4java.DHCPServerInitException;
import org.dhcp4java.DHCPServlet;

public class DHCPSnifferServlet extends DHCPServlet {

	private static final Logger logger = Logger.getLogger("org.dhcp4java.examples.dhcpsnifferservlet");

	private int leaseTime = 360;

	public DHCPSnifferServlet(int leaseTime) {
		this.leaseTime = leaseTime;

	}

	private static int lastOctet = 100;

	/**
	 * Print received packet as INFO log, and do not respnd.
	 * 
	 * @see org.dhcp4java.DHCPServlet#service(org.dhcp4java.DHCPPacket)
	 */
	@Override
	public DHCPPacket service(DHCPPacket request) {
		logger.info(request.toString());
		try {
			InetAddress offered = InetAddress.getByName("192.168.64." + lastOctet++);
			InetAddress timeServer = InetAddress.getByName("192.168.64.1");
			InetAddress subnet = InetAddress.getByName("255.255.255.0");

			List<DHCPOption> options = new ArrayList<>();

			options.add(new DHCPOption(DHCPConstants.DHO_DOMAIN_NAME, "bemar.local".getBytes()));
			options.add(new DHCPOption(DHCPConstants.DHO_DOMAIN_NAME_SERVERS, this.server.getSockAddress().getAddress().getAddress()));
			options.add(new DHCPOption(DHCPConstants.DHO_SUBNET_MASK, subnet.getAddress()));
			options.add(new DHCPOption(DHCPConstants.DHO_TIME_SERVERS, timeServer.getAddress()));

			return DHCPResponseFactory.makeDHCPOffer(request, offered, this.leaseTime, this.server.getSockAddress().getAddress(), "Hello World",
					options.toArray(new DHCPOption[0]));
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
		}
		return null;
	}
	
	

	/**
	 * Launcher for the server.
	 * 
	 * <p>
	 * No args.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DHCPCoreServer server = DHCPCoreServer.initServer(new DHCPSnifferServlet(720), null);
			new Thread(server).start();
		} catch (DHCPServerInitException e) {
			logger.log(Level.SEVERE, "Server init", e);
		}
	}
}
