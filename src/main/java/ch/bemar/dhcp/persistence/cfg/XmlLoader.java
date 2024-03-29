package ch.bemar.dhcp.persistence.cfg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlLoader {

	private XmlLoader() {
	}

	public static Configuration loadConfiguration(File xmlFile) throws JAXBException, IOException {
		return loadConfiguration(FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8));
	}

	public static Configuration loadConfiguration(InputStream xmlStream) throws JAXBException, IOException {
		return loadConfiguration(IOUtils.toString(xmlStream, StandardCharsets.UTF_8));
	}

	public static Configuration loadConfiguration(String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Configuration.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Configuration config = (Configuration) unmarshaller.unmarshal(new StringReader(xml));
		
		return config;
	}

}
