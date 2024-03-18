package ch.bemar.dhcp.core;

import java.io.IOException;
import java.net.DatagramSocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TransportSocket {

	/** default MTU for ethernet */
	public static final int PACKET_SIZE = 1500;

	private final String id;
	private final DatagramSocket serverSocket;

	public DatagramPacket receive() throws IOException {

		java.net.DatagramPacket packet = new java.net.DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);

		log.info("Waiting for UDP packet");
		serverSocket.receive(packet);
		log.info("Got packet");

		return new DatagramPacket(packet, id);
	}

	public void close() {
		try {
			this.serverSocket.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public TransportSocket(DatagramSocket serverSocket, String id) {
		super();
		this.serverSocket = serverSocket;
		this.id = id;

	}

	public void send(DatagramPacket responseDatagram) throws IOException {

		if (responseDatagram != null)
			serverSocket.send(responseDatagram.getPacket());
		else {
			log.warn("response datagramm is null. nothing to send");
		}
	}

}
