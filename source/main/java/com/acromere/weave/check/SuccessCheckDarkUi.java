package com.acromere.weave.check;

import com.acromere.weave.WeaveFlag;

import java.util.ArrayList;
import java.util.List;

public class SuccessCheckDarkUi extends SuccessCheck {

	public static void main( String[] commands ) {
		new SuccessCheckDarkUi().run();
	}

	@Override
	protected String getLogFile() {
		return "target/success-check-ui.log";
	}

	@Override
	public List<String> getProgramCommands() {
		List<String> commands = new ArrayList<>( super.getProgramCommands() );
		commands.addAll( List.of( WeaveFlag.DARK, WeaveFlag.TITLE, "Success Check" ) );
		return commands;
	}

}
