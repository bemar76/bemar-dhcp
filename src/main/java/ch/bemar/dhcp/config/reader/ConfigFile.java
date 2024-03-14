package ch.bemar.dhcp.config.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import ch.bemar.dhcp.constants.DhcpConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ConfigFile {

	private final String content;

	private List<String> lines;

	private int cursor = 0;

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

			if (!Strings.isNullOrEmpty(line) && !line.trim().startsWith("#")) {

				if (StringUtils.countMatches(line, DhcpConstants.SEMICOLON) > 1) {

					String[] tokens = StringUtils.split(line, DhcpConstants.SEMICOLON);
					lines.addAll(Lists.newArrayList(tokens));

				} else {

					lines.add(line);
				}
			}

		}

		return lines;

	}

	public void decreaseCursor() {
		cursor--;
		if (cursor < 0) {
			cursor = 0;
		}
	}

	public String getPreview() {
		int tmpCursor = cursor + 1;

		if (tmpCursor < lines.size()) {
			return getLine(tmpCursor);
		}

		return null;
	}

	public synchronized boolean hasElements() {
		return cursor < lines.size();
	}

	/**
	 * returns the next line of config file trimmed and without semicolon. Does
	 * increase line cursor.
	 * 
	 * @return
	 */
	public synchronized String getNextLine() {
		if (hasElements()) {
			return lines.get(++cursor).replace(DhcpConstants.SEMICOLON, "").trim();
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
		if (hasElements()) {
			return lines.get(cursor).replace(DhcpConstants.SEMICOLON, "").trim();
		}

		return null;
	}

	public String getLine(int index) {
		return lines.get(index);
	}
}