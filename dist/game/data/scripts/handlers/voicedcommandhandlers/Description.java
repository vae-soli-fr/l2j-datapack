package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.CustomImage;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

public class Description implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"desc"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		L2Object target = activeChar.getTarget();

		if (!(target instanceof L2PcInstance)) {
			return false;
		}

		L2PcInstance player = (L2PcInstance) target;

		if (player.getDescription() == null) {
			activeChar.sendMessage(player.getName() + " n'a pas encore de description.");
		} else {
			NpcHtmlMessage html = new NpcHtmlMessage();
			html.setHtml("<html><title>" + target.getName() + "</title><body><br>" + escapeXml(player.getDescription()) + "</body></html>");
			CustomImage.sendPackets(activeChar, html);
			activeChar.sendPacket(html);
		}

		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	public static String escapeXml(String s) {
	    return s.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;");
	}
}