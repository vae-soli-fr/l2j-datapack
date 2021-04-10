package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.Broadcast;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.util.Rnd;


public class Dice implements IVoicedCommandHandler {
	private static int MIN_FACES = 2;
	private static int MAX_FACES = 100;

	private static final String[] VOICED_COMMANDS = {
		"dice"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		// check spam
		int faces;

		try {
			faces = Integer.parseInt("params");
		} catch (NumberFormatException e) {
			return false;
		}
		if ( faces < MIN_FACES || faces > MAX_FACES ) {
			activeChar.sendMessage("Min 2, Max 100");
		}

		String message = activeChar.getName() + " " + Rnd.get(1, faces)  + "/" + faces;
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, SystemMessage.sendString(message), 600);
		return true;
	}


	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}
