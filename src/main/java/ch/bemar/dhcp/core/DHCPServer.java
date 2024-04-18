package ch.bemar.dhcp.core;

import java.io.IOException;
import java.time.Duration;

import org.apache.commons.lang3.ThreadUtils;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DHCPServer implements Runnable {

	private final DhcpServerConfiguration serverConfig;

	private SocketManager socketManager;

	public DHCPServer(DhcpServerConfiguration serverConfig, Configuration dbCfg) throws Exception {
		this.serverConfig = serverConfig;
		init(dbCfg);
	}

	private void init(Configuration dbCfg) throws Exception {

		log.info("Starting bemar-DHCP");

		socketManager = new SocketManager();

		for (DhcpSubnetConfig subnetConfig : serverConfig.getSubnets()) {

			DatagramHandler handler = new DatagramHandler(serverConfig, subnetConfig, dbCfg);

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
