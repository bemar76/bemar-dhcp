package ch.bemar.dhcp.config;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;

import org.dhcp4java.DHCPOption;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.config.element.Allow;
import ch.bemar.dhcp.config.element.Authoritative;
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
		this.options = Lists.newArrayList();
		this.allows = Lists.newArrayList();
	}

	private Authoritative authoritative;

	private List<Allow> allows;

	private DdnsUpdateStyle ddnsUpdateStyle;

	private DdnsUpdates ddnsUpdates;

	private DefaultLeaseTime defaultLeaseTime;

	private MaxLeaseTime maxLeaseTime;

	private LogFacility logFacility;

	private List<DHCPOption> options;

	public String toString() {
		return machMalString(this);
	}

	public static String machMalString(Object config) {

		StringBuilder sb = new StringBuilder();

		Field[] fields = ReflectionUtils.getAllFields(config.getClass());

		for (Field field : fields) {

			field.setAccessible(true);

			Object value = null;
			try {
				value = field.get(config);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if ((value != null && (!Collection.class.isAssignableFrom(value.getClass())
					|| (Collection.class.isAssignableFrom(value.getClass()) && ((Collection) value).size() > 0)))) {
				sb.append(value.toString());
				sb.append("\n");
			}

		}

		return sb.toString();

	}

	
}
