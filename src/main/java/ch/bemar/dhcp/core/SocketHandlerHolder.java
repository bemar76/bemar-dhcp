package ch.bemar.dhcp.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocketHandlerHolder {

	private final TransportSocket socket;
	private final IDatagramHandler handler;

}
