package ch.bemar.dhcp.core;

import java.io.IOException;
import java.time.Duration;

import org.apache.commons.lang3.ThreadUtils;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.util.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DHCPServer implements Runnable {

	private final DhcpServerConfiguration serverConfig;

	private SocketManager socketManager;

	public DHCPServer(DhcpServerConfiguration serverConfig) throws Exception {
		this.serverConfig = serverConfig;
		init();
	}

	private void init() throws Exception {

		log.info("Starting bemar-DHCP");

		socketManager = new SocketManager(PropertiesLoader.loadProperties(DhcpConstants.DEFAULT_PROPERTIES_FILE));

		for (DhcpSubnetConfig subnetConfig : serverConfig.getSubnets()) {

			DatagramHandler handler = new DatagramHandler(serverConfig, subnetConfig);

			socketManager.registerHandler(handler);

		}

	}

	@Override
	public void run() {

		boolean run = true;

		try {
			socketManager.startListen();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			run = false;
		}

		while (run) {

			try {
				ThreadUtils.sleep(Duration.ofMillis(1000l));
			} catch (InterruptedException ex) {
				log.warn("Got interrupted exception. Shutting down");
				run = false;
			}

		}

		socketManager.close();

	}

}
