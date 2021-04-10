package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.cache.HtmCache;

public class Help implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"help",
		"aide"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		String fashionMenu = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/help.htm");
		if (fashionMenu != null)
		{
			NpcHtmlMessage	 html = new NpcHtmlMessage(fashionMenu);
			activeChar.sendPacket(html);
		}
		return true;
	}


	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}
