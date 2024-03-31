package ch.bemar.dhcp.net.dns;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;

public class QueryProcessor {

	public Message process(Message request, Map<String, List<Record>> zones) throws IOException {

		Message response = new Message(request.getHeader().getID());
		response.addRecord(request.getQuestion(), Section.QUESTION);

		for (String zone : zones.keySet()) {

			for (Record record : zones.get(zone)) {

				if (record.getName().equals(request.getQuestion().getName())) {
					response.addRecord(record, Section.ANSWER);
				}

			}

		}

		return response;
	}
}
