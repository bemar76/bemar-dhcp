package ch.bemar.dhcp.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dhcp4java.DHCPOption;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.element.Allow;
import ch.bemar.dhcp.config.element.Authoritative;
import ch.bemar.dhcp.config.element.DdnsDomainName;
import ch.bemar.dhcp.config.element.DdnsRevDomainName;
import ch.bemar.dhcp.config.element.DdnsUpdateStyle;
import ch.bemar.dhcp.config.element.DdnsUpdates;
import ch.bemar.dhcp.config.element.DefaultLeaseTime;
import ch.bemar.dhcp.config.element.LogFacility;
import ch.bemar.dhcp.config.element.MaxLeaseTime;
import ch.bemar.dhcp.util.ReflectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BaseConfiguration {

	public BaseConfiguration() {
		this.options = Sets.newHashSet();
		this.allows = Lists.newArrayList();
		this.keys = Maps.newHashMap();
		this.zones = Maps.newHashMap();
	}

	private Authoritative authoritative;

	private List<Allow> allows;

	private DdnsUpdateStyle ddnsUpdateStyle;

	private DdnsUpdates ddnsUpdates;

	private DdnsDomainName ddnsDomainName;

	private DdnsRevDomainName ddnsRevDomainName;

	private DefaultLeaseTime defaultLeaseTime;

	private MaxLeaseTime maxLeaseTime;

	private LogFacility logFacility;

	private Set<DHCPOption> options;

	private Map<String, DhcpZoneConfig> zones;

	private Map<String, DhcpKeyConfig> keys;

	public String machMalString(Object config) {

		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("\n");
		sb.append("\n");

		Field[] fields = ReflectionUtils.getAllFields(config.getClass());

		for (Field field : fields) {

			if (field.getType().equals(Logger.class)) {
				continue;
			}

			field.setAccessible(true);

			Object value = null;
			try {
				value = field.get(config);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if (isNotNullOrEmpty(value)) {

				if (Collection.class.isAssignableFrom(value.getClass())) {

					sb.append(printCollection((Collection) value));

				} else if (Map.class.isAssignableFrom(value.getClass())) {

					sb.append(printMap((Map) value));

				} else {

					sb.append(value.toString());
				}

				sb.append("\n");
			}

		}

		return sb.append("\n\n").toString();

	}

	private String printCollection(Collection coll) {
		StringBuilder sb = new StringBuilder("[\n");

		for (Object obj : coll) {

			StringBuilder inner = new StringBuilder(obj.getClass().getSimpleName()).append("\n");
			inner.append(obj.toString());
			sb.append(inner.toString());

		}

		return sb.append("\n").toString();

	}

	private String printMap(Map map) {
		StringBuilder sb = new StringBuilder("[\n");

		for (Object obj : map.entrySet()) {

			StringBuilder inner = new StringBuilder(obj.getClass().getSimpleName()).append("\n");
			inner.append(obj.toString());
			sb.append(inner.toString());

		}

		return sb.append("\n").toString();
	}

	private boolean isNotNullOrEmpty(Object value) {
		if (value == null) {
			return false;
		}

		if (!Collection.class.isAssignableFrom(value.getClass()) && !Map.class.isAssignableFrom(value.getClass())) {
			return true;
		}

		if (Collection.class.isAssignableFrom(value.getClass()) && ((Collection) value).isEmpty()) {
			return false;
		}

		if (Map.class.isAssignableFrom(value.getClass()) && ((Map) value).isEmpty()) {
			return false;
		}

		return true;
	}

}
