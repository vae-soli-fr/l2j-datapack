package handlers.voicedcommandhandlers;

import java.util.Collection;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.util.StringUtil;

public class PetTalk implements IVoicedCommandHandler {

	private static final int CHATALL_ID = 0;
	private static final String[] VOICED_COMMANDS = {
		"pet",
		"dit"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {

		L2Summon pet = activeChar.getSummon();
		if (pet == null) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
		} else {
			handleCreatureSay(pet, params);
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	private void handleCreatureSay(L2Summon summon, String text)
	{
		String name = StringUtil.concat(summon.getName() != null ? summon.getName() : summon.getTemplate().getName(), " (", summon.getOwner().getName(), ")");
		CreatureSay cs = new CreatureSay(summon.getObjectId(), CHATALL_ID, name, text);
		Collection<L2PcInstance> plrs = summon.getKnownList().getKnownPlayers().values();
		for (L2PcInstance player : plrs)
		{
			if ((player != null) && summon.isInsideRadius(player, 1250, false, true))
			{
				player.sendPacket(cs);
			}
		}
	}

}