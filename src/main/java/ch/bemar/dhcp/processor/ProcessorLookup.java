package ch.bemar.dhcp.processor;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.dhcp4java.DHCPPacket;

import com.google.common.collect.Maps;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.lease.LeaseAddressManagement;
import ch.bemar.dhcp.dns.DnsUpdateManager;
import ch.bemar.dhcp.exception.TypeNotFoundException;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.util.ReflectionUtils;

public class ProcessorLookup {

	private static Map<Byte, IProcessor> processorSet;

	private static LeaseAddressManagement addressManagement;

	private static DnsUpdateManager dnsUpdateManager;

	private static DhcpSubnetConfig config;

	public void close() {

	}

	public ProcessorLookup(DhcpSubnetConfig cfg, Configuration dbCfg) throws Exception {

		synchronized (this) {

			if (dnsUpdateManager == null) {
				dnsUpdateManager = new DnsUpdateManager(cfg);
			}

			if (addressManagement == null) {
				addressManagement = new LeaseAddressManagement(cfg, dbCfg);
			}

			if (config == null) {
				config = cfg;
			}

			if (processorSet == null) {

				processorSet = Maps.newHashMap();

				for (Class<? extends IProcessor> proc : ReflectionUtils.findImplementations(IProcessor.class)) {

					Constructor<? extends IProcessor> constr = proc.getConstructor(cfg.getClass(),
							addressManagement.getClass(), dnsUpdateManager.getClass());

					IProcessor p = constr.newInstance(cfg, addressManagement, dnsUpdateManager);

					for (byte b : p.processTypes()) {
						processorSet.put(b, p);
					}

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
