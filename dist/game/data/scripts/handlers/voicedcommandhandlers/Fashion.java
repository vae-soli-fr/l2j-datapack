package handlers.voicedcommandhandlers;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.gameserver.CustomImage;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

public class Fashion implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"fashion",
		"dressme"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {

		boolean doClear = false;
		List<Integer> updatedSlots = new ArrayList<>();

		switch(String.valueOf(params)) {
			case "none":
				doClear = true;
			case "full":
				updatedSlots.add(Inventory.PAPERDOLL_HEAD);
				updatedSlots.add(Inventory.PAPERDOLL_CHEST);
				updatedSlots.add(Inventory.PAPERDOLL_LEGS);
				updatedSlots.add(Inventory.PAPERDOLL_GLOVES);
				updatedSlots.add(Inventory.PAPERDOLL_FEET);
				updatedSlots.add(Inventory.PAPERDOLL_RHAND);
				updatedSlots.add(Inventory.PAPERDOLL_LHAND);
				break;
			case "chest":
				updatedSlots.add(Inventory.PAPERDOLL_CHEST);
				break;
			case "legs":
				updatedSlots.add(Inventory.PAPERDOLL_LEGS);
				break;
			case "gloves":
				updatedSlots.add(Inventory.PAPERDOLL_GLOVES);
				break;
			case "feet":
				updatedSlots.add(Inventory.PAPERDOLL_FEET);
				break;
			case "hands":
				updatedSlots.add(Inventory.PAPERDOLL_RHAND);
				updatedSlots.add(Inventory.PAPERDOLL_LHAND);
				break;
			default:
				activeChar.sendMessage("Usage: .fashion <full|chest|legs|gloves|feet|hands|none>");
				showMenu(activeChar);
				return false;
		}

		for (int slot : updatedSlots) {
			L2ItemInstance item = activeChar.getInventory().getPaperdollItem(slot);
			if (item == null || doClear) {
				activeChar.getInventory().setFashionItem(slot, null);
			} else {
				activeChar.getInventory().setFashionItem(slot, item.getItem());
			}
		}

		activeChar.broadcastUserInfo();
		activeChar.sendMessage(doClear ? "Equipement supprime." : "Equipement enregistre.");

		showMenu(activeChar);
		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	public void showMenu(L2PcInstance activeChar) {
		String fashionMenu = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/fashion.htm");
		if (fashionMenu != null)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(fashionMenu);
			CustomImage.sendPackets(activeChar, html);
			activeChar.sendPacket(html);
		}
	}
}