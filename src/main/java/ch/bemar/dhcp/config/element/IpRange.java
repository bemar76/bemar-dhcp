package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.hibernate.tool.hbm2x.StringUtils;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpRange implements IConfigElement<List<InetAddress>> {

	public IpRange(String configLine) throws UnknownHostException {

		String[] tokens = StringUtils.split(configLine);
		if (tokens.length != 3) {
			throw new IllegalArgumentException("IpRange needs 2 parameters");
		}

		this.start = InetAddress.getByName(tokens[1].trim());
		this.end = InetAddress.getByName(tokens[2].trim());

	}

	private final InetAddress start;
	private final InetAddress end;

	@Override
	public String getKeyWord() {
		return "range";
	}

	@Override
	public List<InetAddress> getValue() {
		return Lists.newArrayList(start, end);
	}

}
