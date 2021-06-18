package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Camp implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "camp"
    };

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("camp")) {
            activeChar.evolveCamp();
        }
        return true;

    }

    @Override
	public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}
