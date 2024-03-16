package ch.bemar.dhcp.core;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TransportSocketRunnable implements Runnable {

	private final SocketHandlerHolder holder;

	@Override
	public void run() {

		boolean run = true;

		while (run) {

			try {

				DatagramPacket request = holder.getSocket().receive();
				log.debug("got request");

				DatagramPacket response = holder.getHandler().handlePacket(request);
				log.debug("got response");

				holder.getSocket().send(response);

			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

		}

	}

}
