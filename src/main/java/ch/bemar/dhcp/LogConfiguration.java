package ch.bemar.dhcp;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogConfiguration {

	private LogConfiguration() {
	}

	public static void load() {
		load(null);
	}

	public static void load(String logbackConfigPath) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			if (!Strings.isNullOrEmpty(logbackConfigPath)) {
				context.reset();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);

				if (logbackConfigPath.startsWith("classpath:")) {
					
					InputStream is = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(StringUtils.remove(logbackConfigPath, "classpath:"));
					
					configurator.doConfigure(is);
				} else if (logbackConfigPath.startsWith("file:")) {

					configurator.doConfigure(new File(StringUtils.remove(logbackConfigPath, "file:")));
				}

			}

		} catch (JoranException je) {
			System.err.print(je.getMessage());
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context); // Drucke Statusmeldungen
	}

}
