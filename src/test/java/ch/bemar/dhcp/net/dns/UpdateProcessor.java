package ch.bemar.dhcp.net.dns;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.xbill.DNS.Message;
import org.xbill.DNS.Opcode;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;

import com.google.common.collect.Lists;

public class UpdateProcessor {

	public Message process(Message request, Map<String, List<Record>> zones) throws IOException {

		List<Record> zoneSection = request.getSection(0); // zone

		checkZone(zoneSection.get(0), zones);

		for (Record r : request.getSection(2)) {

			zones.get(zoneSection.get(0).getName().toString()).add(r);

		}

		Message response = new Message(request.getHeader().getID());
		response.getHeader().setOpcode(Opcode.UPDATE);
		response.getHeader().setRcode(Rcode.NOERROR);

		response.addRecord(zoneSection.get(0), Section.ZONE);

		return response;
	}

	private void checkZone(Record section, Map<String, List<Record>> zones) throws IOException {

		if (!zones.containsKey(section.getName().toString())) {
			zones.put(section.getName().toString(), Lists.newArrayList());
		}

	}

}
