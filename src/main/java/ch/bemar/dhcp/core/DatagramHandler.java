package ch.bemar.dhcp.core;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;

import org.dhcp4java.DHCPPacket;
import org.dhcp4java.old.DHCPServletDispatcher;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.exception.DHCPBadPacketException;
import ch.bemar.dhcp.exception.TypeNotFoundException;
import ch.bemar.dhcp.processor.ProcessorLookup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatagramHandler implements IDatagramHandler {

	private final ProcessorLookup lookup;

	private final DhcpServerConfiguration serverConfig;
	private final DhcpSubnetConfig subnetConfig;

	public DatagramHandler(DhcpServerConfiguration serverConfig, DhcpSubnetConfig subnetConfig) throws Exception {
		this.lookup = new ProcessorLookup(subnetConfig);
		this.serverConfig = serverConfig;
		this.subnetConfig = subnetConfig;
	}

	public DHCPPacket processPacket(DHCPPacket request) throws TypeNotFoundException {

		return lookup.findProcessor(request).processPacket(request);
	}

	/**
	 * Low-level method for receiving a UDP Daragram and sending one back.
	 * 
	 * <p>
	 * This methode normally does not need to be overriden and passes control to
	 * <tt>service()</tt> for DHCP packets handling. Howerever the
	 * <tt>service()</tt> method is not called if the DHCP request is invalid (i.e.
	 * could not be parsed). So overriding this method gives you control on every
	 * datagram received, not only valid DHCP packets.
	 * 
	 * @param requestDatagram the datagram received from the client
	 * @return response the datagram to send back, or <tt>null</tt> if no answer
	 */
	public DatagramPacket handlePacket(DatagramPacket requestDatagram) {
		DatagramPacket responseDatagram;

		if (requestDatagram == null) {
			return null;
		}

		try {
			// parse DHCP request
			DHCPPacket request = DHCPPacket.getPacket(requestDatagram);

			if (request == null) {
				return null;
			} // nothing much we can do

			log.debug(request.toString());

			// do the real work
			DHCPPacket response = this.processPacket(request); // call
																// service
																// function
			// done

			log.debug("service() done");

			if (response == null) {
				return null;
			}

			// check address/port
			InetAddress address = response.getAddress();
			if (address == null) {
				log.warn("Address needed in response");
				return null;
			}
			int port = response.getPort();

			// we have something to send back
			byte[] responseBuf = response.serialize();

			log.debug("Buffer is " + responseBuf.length + " bytes long");

			log.info("response: " + response.toString());

			responseDatagram = new DatagramPacket(responseBuf, responseBuf.length, address, port,
					requestDatagram.getId());

			log.debug("Sending back to" + address.getHostAddress() + '(' + port + ')');

			// this.postProcess(requestDatagram, responseDatagram);

			return responseDatagram;
		} catch (DHCPBadPacketException e) {
			log.error("Invalid DHCP packet received", e);
		} catch (Exception e) {
			log.error("Unexpected Exception", e);
		}

		// general fallback, we do nothing
		return null;
	}

	@Override
	public DhcpSubnetConfig getSubnetConfig() {
		return subnetConfig;
	}

	@Override
	public void close() {
		this.lookup.close();
	}

}
