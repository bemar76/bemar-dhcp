package ch.bemar.dhcp;

import ch.bemar.dhcp.persistence.LeaseDbDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutdownHook implements Runnable {

	public ShutdownHook() {
		log.info("ShutdownHook installed");
	}

	@Override
	public void run() {
		log.info("Shutdown Hook wird ausgeführt...");

		new LeaseDbDao().close();

		log.info("Aufräumen abgeschlossen.");
	}

	public static void installShutdownHook() {

		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
	}
}
