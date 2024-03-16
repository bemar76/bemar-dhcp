package ch.bemar.dhcp.processor;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.dhcp4java.DHCPPacket;

import com.google.common.collect.Maps;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.AddressManagement;
import ch.bemar.dhcp.exception.TypeNotFoundException;
import ch.bemar.dhcp.util.ReflectionUtils;

public class ProcessorLookup {

	private static Map<Byte, IProcessor> processorSet;

	private static AddressManagement addressManagement;

	private static DhcpSubnetConfig config;
	
	public void close() {
		
	}

	public ProcessorLookup(DhcpSubnetConfig cfg) throws Exception {

		synchronized (this) {

			if (addressManagement == null) {
				addressManagement = new AddressManagement(cfg);
			}

			if (config == null) {
				config = cfg;
			}

			if (processorSet == null) {

				processorSet = Maps.newHashMap();

				for (Class<? extends IProcessor> proc : ReflectionUtils.findImplementations(IProcessor.class)) {

					Constructor<? extends IProcessor> constr = proc.getConstructor(cfg.getClass(),
							addressManagement.getClass());

					IProcessor p = constr.newInstance(cfg, addressManagement);

					processorSet.put(p.processType(), p);

				}

			}

		}

	}

	public IProcessor findProcessor(DHCPPacket packet) throws TypeNotFoundException {

		if (!processorSet.containsKey(packet.getDHCPMessageType())) {
			throw new TypeNotFoundException("No processor for message type " + packet.getDHCPMessageType() + " found");
		}

		return processorSet.get(packet.getDHCPMessageType());

	}

}
