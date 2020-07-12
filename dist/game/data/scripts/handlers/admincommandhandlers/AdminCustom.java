package handlers.admincommandhandlers;

import java.util.Collection;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

public class AdminCustom implements IAdminCommandHandler
{
	private static final int CHATALL_ID = 0;
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_dit"
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_dit"))
		{
			L2Object target = activeChar.getTarget();
			if (target == null || !(target instanceof L2Npc)) {
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			} else {
				String sentence = command.substring(10);
				handleCreatureSay((L2Npc) target, sentence);
			}
		}

		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}

	private void handleCreatureSay(L2Npc npc, String text)
	{
		CreatureSay cs = new CreatureSay(npc.getObjectId(), CHATALL_ID, npc.getName(), text);
		Collection<L2PcInstance> plrs = npc.getKnownList().getKnownPlayers().values();
		for (L2PcInstance player : plrs)
		{
			if ((player != null) && npc.isInsideRadius(player, 1250, false, true))
			{
				player.sendPacket(cs);
			}
		}
	}

}
