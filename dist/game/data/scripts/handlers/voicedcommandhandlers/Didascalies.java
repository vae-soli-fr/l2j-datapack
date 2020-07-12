package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.enums.Language;
import com.l2jserver.gameserver.enums.Volume;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Didascalies implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"crie",
		"murmure",
		"elfe",
		"sombre",
		"nain",
		"orc",
		"kamael"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		switch(command) {
		case "crie":
			activeChar.setVolume(Volume.SHOUT.equals(activeChar.getVolume()) ? Volume.DEFAULT : Volume.SHOUT);
			break;
		case "murmure":
			activeChar.setVolume(Volume.WHISPER.equals(activeChar.getVolume()) ? Volume.DEFAULT : Volume.WHISPER);
			break;
		case "elfe":
			activeChar.setLanguage(Language.ELVEN.equals(activeChar.getLanguage()) ? Language.COMMON : Language.ELVEN);
			break;
		case "sombre":
			activeChar.setLanguage(Language.DARKELF.equals(activeChar.getLanguage()) ? Language.COMMON : Language.DARKELF);
			break;
		case "nain":
			activeChar.setLanguage(Language.DWARVEN.equals(activeChar.getLanguage()) ? Language.COMMON : Language.DWARVEN);
			break;
		case "orc":
			activeChar.setLanguage(Language.ORCISH.equals(activeChar.getLanguage()) ? Language.COMMON : Language.ORCISH);
			break;
		case "kamael":
			activeChar.setLanguage(Language.KAMAEL.equals(activeChar.getLanguage()) ? Language.COMMON : Language.KAMAEL);
			break;
		}

		activeChar.sendMessage("Didascalies changees.");
		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

}