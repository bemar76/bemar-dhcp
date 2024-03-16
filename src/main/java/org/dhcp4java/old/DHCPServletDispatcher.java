package org.dhcp4java.old;

import ch.bemar.dhcp.core.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

/**
 * Servlet dispatcher
 */

@Slf4j
public class DHCPServletDispatcher {

	private final DHCPCoreServer server;
	private final DHCPServlet dispatchServlet;
	private final DatagramPacket dispatchPacket;

	public DHCPServletDispatcher(DHCPCoreServer server, DHCPServlet servlet, DatagramPacket req) {
		this.server = server;
		this.dispatchServlet = servlet;
		this.dispatchPacket = req;
	}

	public void run() {

		try {
			DatagramPacket response = this.dispatchServlet.serviceDatagram(this.dispatchPacket);

			if (response != null)
				this.server.sendResponse(response); // invoke callback method
			else
				log.warn("got null response packet. Ignoring");

		} catch (Exception e) {
			log.debug("Exception in dispatcher", e);
		}
	}
}