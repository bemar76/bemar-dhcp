package ch.bemar.dhcp.config;

import java.util.List;

import ch.bemar.dhcp.config.element.Allow;
import ch.bemar.dhcp.config.element.DdnsUpdateStyle;
import ch.bemar.dhcp.config.element.DdnsUpdates;
import ch.bemar.dhcp.config.element.DefaultLeaseTime;
import ch.bemar.dhcp.config.element.LogFacility;
import ch.bemar.dhcp.config.element.MaxLeaseTime;
import lombok.Data;

@Data
public class BaseConfiguration {

	private String authoritative;

	private List<Allow> allows;

	private DdnsUpdateStyle ddnsUpdateStyle;

	private DdnsUpdates ddnsUpdates;

	private DefaultLeaseTime defaultLeaseTime;

	private MaxLeaseTime maxLeaseTime;

	private LogFacility logFacility;
}
