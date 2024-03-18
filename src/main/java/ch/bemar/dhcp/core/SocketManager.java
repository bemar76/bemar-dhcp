package ch.bemar.dhcp.core;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ch.bemar.dhcp.env.EnvConstants;
import ch.bemar.dhcp.env.EnvironmentManager;
import ch.bemar.dhcp.exception.DHCPServerInitException;
import ch.bemar.dhcp.exception.InterfaceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SocketManager {

	public static final String LISTENERS = "listeners";

	private final Set<SocketHandlerHolder> socketHandlerAssignments;

	private final List<ListenerConfig> listenerConfigs;

	public SocketManager() throws IOException, DHCPServerInitException {
		socketHandlerAssignments = Sets.newConcurrentHashSet();
		this.listenerConfigs = Lists.newArrayList();

		for (String listenerCfg : EnvironmentManager.getInstance().getEnvAsStringList(EnvConstants.LISTENERS, ",")) {

			listenerConfigs.add(new ListenerConfig(listenerCfg.trim()));

		}
	}

	public void registerHandler(IDatagramHandler handler) throws Exception {

		for (ListenerConfig cfg : listenerConfigs) {

			if (cfg.getName().equalsIgnoreCase(handler.getSubnetConfig().getDomainName())) {

				DatagramSocket socket = bindToInterface(cfg);

				TransportSocket ts = new TransportSocket(socket, cfg.getName());

				socketHandlerAssignments.add(new SocketHandlerHolder(ts, handler));

				log.info("Handler '{}' was successfully bound to listener '{}' with subnet config {}",
						handler.getSubnetConfig().getDomainName(), ts.getId(), handler.getSubnetConfig().getRange());

				return;
			}

		}

		log.warn("For handler no valid socket was found");

	}

	public void startListen() throws IOException {

		ThreadGroup group = new ThreadGroup(System.currentTimeMillis() + "");

		for (SocketHandlerHolder holder : socketHandlerAssignments) {

			TransportSocketRunnable tsr = new TransportSocketRunnable(holder);

			Thread t = new Thread(group, tsr);
			t.start();

		}

	}

	public void send(DatagramPacket responseDatagram) throws IOException {

		for (SocketHandlerHolder holder : socketHandlerAssignments) {

			if (holder.getSocket().getId().equalsIgnoreCase(responseDatagram.getId())) {
				holder.getSocket().send(responseDatagram);
				return;
			}

		}

		log.error("Datagram could not be send because of unknown id {}", responseDatagram.getId());

	}

	public void close() {

		log.info("Shutting down SocketManager");

		for (SocketHandlerHolder holder : socketHandlerAssignments) {

			log.info("closing {}", holder.getSocket().getId());
			holder.getSocket().close();

		}

	}

	public DatagramSocket bindToInterface(ListenerConfig config) throws InterfaceException {

		if (config.getAddress() != null) {

			return bindToInterface(config.getAddress(), config.getPort());
		} else {

			return bindToInterface(config.getIfaceName(), config.getPort());

		}

	}

	public DatagramSocket bindToInterface(String ifaceName, int port) throws InterfaceException {
		try {

			NetworkInterface networkInterface = NetworkInterface.getByName(ifaceName);
			if (networkInterface == null) {
				throw new InterfaceException("No interface with name " + ifaceName + " was found");
			}

			var networkAddresses = networkInterface.getInetAddresses();
			var interfaceAddress = networkAddresses.hasMoreElements() ? networkAddresses.nextElement() : null;
			if (interfaceAddress == null) {
				throw new InterfaceException("Interface with name " + ifaceName + " has no address");
			}

			SocketAddress socketAddress = new InetSocketAddress(interfaceAddress, port);

			DatagramSocket socket = new DatagramSocket(null);
			socket.bind(socketAddress);

			log.info("Socket erfolgreich an " + interfaceAddress + " gebunden.");

			return socket;

		} catch (Exception e) {
			throw new InterfaceException(e);
		}
	}

	public DatagramSocket bindToInterface(InetAddress address, int port) throws InterfaceException {
		try {

			InetSocketAddress socketAddress = new InetSocketAddress(address, port);

			log.info("DatagramSocket successfully bound to " + address.getHostAddress() + " at port " + port);

			return new DatagramSocket(socketAddress);

		} catch (Exception e) {
			throw new InterfaceException(e);
		}
	}

}
