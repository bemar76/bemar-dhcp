package ch.bemar.dhcp.config.lease;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.persistence.service.LeaseDbService;
import ch.bemar.dhcp.util.IPRangeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaseTable {

	private DhcpSubnetConfig subnetConfig;

	private List<LeaseAddress> addresses;

	private final LeaseDbService addressService;

	public LeaseTable(DhcpSubnetConfig subnet, Configuration dbCfg) throws Exception {
		this(subnet, new LeaseDbService(dbCfg));
	}

	private LeaseTable(DhcpSubnetConfig subnet, LeaseDbService dbService) throws Exception {

		this.addressService = dbService;

		this.subnetConfig = subnet;
		this.addresses = Lists.newArrayList();

		log.info("Building AddressManagement DB");

		log.info("handling addresses for subnet {}", subnet);

		List<InetAddress> ips = IPRangeCalculatorUtil.calculateAllIPsInRange(subnet.getRange(),
				subnet.getSubnetAddress().getValue());

		if (ips.isEmpty()) {
			throw new NoAddressFoundException("There are no addresses calculated for range " + subnetConfig.getRange());
		}

		for (InetAddress ip : ips) {

			LeaseAddress foundFromDb = addressService.findByAddress(ip);

			if (foundFromDb == null) {

				LeaseAddress created = new LeaseAddress(ip, subnet.getSubnetAddress(), subnet.getDefaultLeaseTime(),
						subnet.getMaxLeaseTime());

				for (DhcpHostConfig host : subnet.getHosts()) {

					if (host.getFixedIpAddress().getValue().equals(ip)) {
						created.setReservedFor(host.getHardwareAddress());
					}

				}
				log.debug("adding created address to map: {}", created);
				addresses.add(created);

				log.debug("saving created address to db: {}", created);
				addressService.saveOrUpdate(created);

			} else {

				log.debug("adding db address: {}", foundFromDb);
				addresses.add(foundFromDb);
			}

		}

		Collections.sort(addresses);

		log.info("{} addresses calculated for range {}", addresses.size(), subnetConfig.getRange());

	}

	public Collection<LeaseAddress> getLeaseAddresses() {
		return addresses;
	}

	public void persist(LeaseAddress a)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		addressService.saveOrUpdate(a);

	}

	public void close() {
		addressService.close();
	}

}
