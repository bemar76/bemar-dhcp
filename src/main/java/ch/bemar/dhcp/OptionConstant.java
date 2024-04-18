package ch.bemar.dhcp;

import lombok.Getter;

@Getter
public enum OptionConstant {

	SIMULATION("simulation", "s", "Simulation mode. Read on socket, calculate address but not send it."), //
	FILEINPUT("fileinput", "f", true, "Config file path"), //
	DBINPUT("dbinput", "d", true, "DB Config file path"), //
	IFACEINFO("ifaceinfo", "i", "Interface info"), //
	PROD_MODE("prod", "p", "Prod mode."), //
	HELP("help", "h", "Help menu");

	private OptionConstant(String longOpt, String shortOpt, boolean expectArgument, String describtion,
			boolean mandatory) {
		this.shortOpt = shortOpt;
		this.longOpt = longOpt;
		this.describtion = describtion;
		this.expectArgument = expectArgument;
		this.mandatory = mandatory;
	}

	private OptionConstant(String longOpt, String shortOpt, boolean expectArgument, String describtion) {
		this(longOpt, shortOpt, expectArgument, describtion, false);
	}

	private OptionConstant(String longOpt, String shortOpt, String describtion) {
		this(longOpt, shortOpt, false, describtion, false);
	}

	private boolean expectArgument;
	private String shortOpt;
	private String longOpt;
	private String describtion;
	private boolean mandatory;

}
