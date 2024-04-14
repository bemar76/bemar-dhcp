package ch.bemar.dhcp.config.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ConfigFile {

	private final String content;

	private List<String> lines;

	private int cursor = -1;

	public ConfigFile(File file) throws IOException {
		this.content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		log.debug("read content {} from file {}", content, file);
		this.lines = getLines(content);
	}

	public ConfigFile(String content) throws IOException {
		super();
		this.content = content;
		this.lines = getLines(content);
	}

	private List<String> getLines(String content) throws IOException {

		lines = Lists.newArrayList();

		String line = null;

		BufferedReader br = new BufferedReader(new StringReader(content));
		log.debug("created buffered reader from content");

		while ((line = br.readLine()) != null) {
			log.debug("current line: {}", line);

			if (!Strings.isNullOrEmpty(line.trim()) && !line.trim().startsWith("#")) {

				if (StringUtils.countMatches(line, DhcpConstants.SEMICOLON) > 1) {

					String[] tokens = StringUtils.split(line, DhcpConstants.SEMICOLON);
					lines.addAll(Lists.newArrayList(cleanUpLines(tokens)));

				} else {

					lines.add(cleanUpLine(line));
				}
			}

		}

		return lines;

	}

	public synchronized boolean hasElements() {
		return cursor + 1 < lines.size();
	}

	/**
	 * returns the next line of config file trimmed and without semicolon. Does
	 * increase line cursor.
	 * 
	 * @return
	 */
	public synchronized String getNextLine() {
		if (hasElements()) {
			return lines.get(++cursor);
		}

		return null;
	}

	/**
	 * returns the current line of config file trimmed and without semicolon. Does
	 * not increase line cursor.
	 * 
	 * @return
	 */
	public synchronized String getCurrentLine() {

		return lines.get(cursor);
	}

	private String cleanUpLine(String line) {
		return filterSemicolon(removeComment(line));
	}

	private String[] cleanUpLines(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			lines[i] = filterSemicolon(removeComment(lines[i]));
		}
		return lines;
	}

	private String filterSemicolon(String line) {
		if (line.trim().endsWith(DhcpConstants.SEMICOLON)) {
			return StringUtils.substringBeforeLast(line, DhcpConstants.SEMICOLON).trim();
		}
		return line;
	}

	private String[] filterSemicolons(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			lines[i] = filterSemicolon(lines[i]);
		}
		return lines;
	}

	private String removeComment(String line) {
		if (line.contains("#")) {

			return StringUtils.substringBefore(line, "#");
		}

		return line;
	}
}
