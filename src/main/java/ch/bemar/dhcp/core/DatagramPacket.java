package ch.bemar.dhcp.core;

import java.net.InetAddress;

import lombok.Getter;

@Getter
public class DatagramPacket {

	public DatagramPacket(java.net.DatagramPacket packet, String id) {
		this.packet = packet;
		this.id = id;
	}

	public DatagramPacket(byte[] bs, int packetSize, String id) {
		this.packet = new java.net.DatagramPacket(bs, packetSize);
		this.id = id;
	}

	public DatagramPacket(byte[] responseBuf, int length, InetAddress address, int port, String id) {
		this.packet = new java.net.DatagramPacket(responseBuf, 0, length, address, port);
		this.id = id;
	}

	private final java.net.DatagramPacket packet;

	private final String id;

	public InetAddress getAddress() {
		return packet.getAddress();
	}

	public int getPort() {
		return packet.getPort();
	}

	public byte[] getData() {
		return packet.getData();
	}

	public int getOffset() {
		return packet.getOffset();
	}

	public int getLength() {
		return packet.getLength();
	}

}
