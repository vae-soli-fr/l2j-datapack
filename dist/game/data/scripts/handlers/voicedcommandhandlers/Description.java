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
	public static final String NO_TITLE = ".                                                                                ";
	private static final String HTML_TEMPLATE = "<html><title>" + NO_TITLE + "</title><body>" +
												"<table border=0 cellpadding=10 cellspacing=0 height=355 width=235 background=\"L2UI_CH3.refinewnd_back_Pattern\">" +
												"<tr><td><center><br><font name=\"hs12\">%s</font><br></center>%s<br><br><br></td></tr>" +
												"</table></body></html>";

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
			String descriptionHtml = String.format(HTML_TEMPLATE, target.getName(), escapeXml(player.getDescription()));
			descriptionHtml = replaceTags(descriptionHtml);
			html.setHtml(descriptionHtml);
			CustomImage.sendPackets(activeChar, html);
			activeChar.sendPacket(html);
		}

		return true;
	}

	private String replaceTags(String descriptionHtml) {
		return descriptionHtml
				.replace("[br]","<br>")
				.replace("[center]", "<center>")
				.replace("[/center]", "</center>");
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	public static String escapeXml(String s) {
	    return s.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;");
	}
}
