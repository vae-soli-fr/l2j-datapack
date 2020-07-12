package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Titre implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"titre"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {

		activeChar.setTitle(params);
		activeChar.sendMessage("Votre titre a ete modifie.");
		activeChar.broadcastTitleInfo();

		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}