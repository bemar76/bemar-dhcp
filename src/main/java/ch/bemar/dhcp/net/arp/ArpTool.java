package ch.bemar.dhcp.net.arp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import ch.bemar.dhcp.env.EnvironmentManager;

public class ArpTool {

	ArpTable buildTable() throws Exception {

		try {

			Process process = Runtime.getRuntime()
					.exec(EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.CMD));
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String content = IOUtils.toString(reader);

			process.waitFor();
			reader.close();
			
			return buildTable(content);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	ArpTable buildTable(String content) {
		return new ArpTableBuilder().buildTable(content);
	}

}