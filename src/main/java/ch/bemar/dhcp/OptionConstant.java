package ch.bemar.dhcp;

import lombok.Getter;

@Getter
public enum OptionConstant {

	SIMULATION("simulation", "s", "Simulation mode. Read on socket, calculate address but not send it."), //
	FILEINPUT("fileinput", "f", true, "Config file path"), //
	IFACEINFO("ifaceinfo", "i", "Interface info"), //
	PROD_MODE("prod", "p", "Prod mode.");

	private OptionConstant(String longOpt, String shortOpt, boolean expectArgument, String describtion) {
		this.shortOpt = shortOpt;
		this.longOpt = longOpt;
		this.describtion = describtion;
		this.expectArgument = expectArgument;
	}

	private OptionConstant(String longOpt, String shortOpt, String describtion) {
		this(longOpt, shortOpt, false, describtion);
	}

	private boolean expectArgument;
	private String shortOpt;
	private String longOpt;
	private String describtion;

}
