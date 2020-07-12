package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

public class Fashion implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"fashion",
		"dressme"
	};

	private static final int[] PAPERDOLL_ALLOWED = new int[] {
			Inventory.PAPERDOLL_UNDER,
			Inventory.PAPERDOLL_HEAD,
			Inventory.PAPERDOLL_GLOVES,
			Inventory.PAPERDOLL_CHEST,
			Inventory.PAPERDOLL_LEGS,
			Inventory.PAPERDOLL_FEET,
		};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {

		final int expertiseLevel = activeChar.getExpertiseLevel() + 1;

		for (int slot : PAPERDOLL_ALLOWED) {
			L2ItemInstance item = activeChar.getInventory().getPaperdollItem(slot);
			if (item == null || item.getItem().getCrystalType().getId() > expertiseLevel) {
				activeChar.setFashionItem(slot, 0);
			} else {
				activeChar.setFashionItem(slot, item.getDisplayId());
			}
		}

		activeChar.broadcastUserInfo();
		activeChar.sendMessage("Equipement enregistre.");

		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}