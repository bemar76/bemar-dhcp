package ch.bemar.dhcp.net.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Map;

import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Opcode;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * simple mock server to test dns requests
 * 
 * @author bemar
 *
 */
@Slf4j
public class TestDNSServer {

	public static final String tsigKeyName = "myKey";
	public static final String tsigKeySecret = "Y2hhbmdlaXQK";

	private Thread thread = null;
	private volatile boolean running = false;
	private static final int UDP_SIZE = 512;
	private final int port;
	private int requestCount = 0;

	private Map<String, List<Record>> zones = Maps.newHashMap();

	TestDNSServer(int port) {
		this.port = port;
	}

	public void start() {
		running = true;
		thread = new Thread(() -> {
			try {
				serve();
			} catch (IOException ex) {
				stop();
				throw new RuntimeException(ex);
			}
		});
		thread.start();
	}

	public void stop() {
		running = false;
		thread.interrupt();
		thread = null;
	}

	public int getRequestCount() {
		return requestCount;
	}

	private void serve() throws IOException {
		DatagramSocket socket = new DatagramSocket(port);
		while (running) {
			process(socket);
		}
	}

	private void process(DatagramSocket socket) throws IOException {
		byte[] in = new byte[UDP_SIZE];

		Message response = null;

		// Read the request
		DatagramPacket indp = new DatagramPacket(in, UDP_SIZE);
		socket.receive(indp);
		++requestCount;
		log.info(String.format("processing... {}", requestCount));

		// Build the response
		Message request = new Message(in);

		if (!(request.isSigned() && request.isVerified())) {
			response = new Message(request.getHeader().getID());
			response.getHeader().setRcode(Rcode.NOTAUTH);
		}

		if (response != null) {

			Header head = request.getHeader();

			if (Opcode.UPDATE == head.getOpcode()) {
				log.info("update");

				response = new UpdateProcessor().process(request, zones);

			} else if (Opcode.QUERY == head.getOpcode()) {

				log.info("query");

				response = new QueryProcessor().process(request, zones);
			}

		}

		byte[] resp = response.toWire();
		DatagramPacket outdp = new DatagramPacket(resp, resp.length, indp.getAddress(), indp.getPort());
		socket.send(outdp);
	}
}