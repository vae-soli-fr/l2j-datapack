package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.data.xml.impl.TransformData;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;
import com.l2jserver.gameserver.util.Broadcast;

public class Monture implements IVoicedCommandHandler {

	private static final int HORSE_TRANSFO_ID = 106;
	private static final int LION_TRANSFO_ID = 109;
	private static final int SLEDGE_TRANSFO_ID = 110;
	private static final int WYVERN_PET_ID = 12621;
	private static final int FENRIR_PET_ID = 16041;
	private static final int SNOWFENRIR_PET_ID = 16042;
	private static final int STRIDER_PET_ID = 12526;

	private static final String[] VOICED_COMMANDS = {
		"monture",
		"ride"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {

		int transformId = 0;
		int petRideId = 0;

		/*
		 * Unmount
		 */
		if (params == null || params.isEmpty())
		{
			if (activeChar.isTransformed())
			{
				activeChar.untransform();
			}
			else
			{
				activeChar.dismount();
			}
			return true;
		}

		if (activeChar.isSitting())
		{
			activeChar.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return false;
		}

		if (activeChar.hasSummon() || activeChar.isMounted())
		{
			activeChar.sendPacket(SystemMessageId.YOU_ALREADY_HAVE_A_PET);
			return false;
		}

		if (activeChar.isTransformed() || activeChar.isInStance())
		{
			activeChar.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
			return false;
		}

		if (activeChar.isAttackingNow())
		{
			activeChar.sendPacket(SystemMessageId.YOU_CANNOT_SUMMON_IN_COMBAT);
			return false;
		}

		/*
		 * Mount
		 */
		switch(params) {
			case "lion":
			{
				transformId = LION_TRANSFO_ID;
				break;
			}
			case "horse":
			case "cheval":
			{
				transformId = HORSE_TRANSFO_ID;
				break;
			}
			case "sledge":
			case "scarabee":
			{
				transformId = SLEDGE_TRANSFO_ID;
				break;
			}
			case "strider":
			{
				petRideId = STRIDER_PET_ID;
				break;
			}
			case "wyvern":
			{
				petRideId = WYVERN_PET_ID;
				break;
			}
			case "fenrir":
			case "loup":
			{
				if (activeChar.isNoble()) {
					petRideId = SNOWFENRIR_PET_ID;
				} else {
					petRideId = FENRIR_PET_ID;
				}
				break;
			}
			default:
		}

		if (transformId > 0 || petRideId > 0)
		{
			delayedRide(activeChar, transformId, petRideId);
		}
		else
		{
			activeChar.sendMessage("Usage: .monture <lion|cheval|scarabee|strider|wyvern|loup>");
		}

		return true;
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}

	private void delayedRide(L2PcInstance activeChar, int transformId, int petRideId) {
		int mountTimer = Config.MOUNT_INTERVAL * 1000;
		activeChar.forceIsCasting(GameTimeController.getInstance().getGameTicks() + (mountTimer / GameTimeController.MILLIS_IN_TICK));

		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		// SoE Animation section
		activeChar.setTarget(activeChar);
		activeChar.disableAllSkills();

		MagicSkillUse msk = new MagicSkillUse(activeChar, 1050, 1, mountTimer, 0);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, msk, 900);
		SetupGauge sg = new SetupGauge(0, mountTimer);
		activeChar.sendPacket(sg);
		// End SoE Animation section

		// continue execution later
		activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(new MountFinalizer(activeChar, transformId, petRideId), mountTimer));
	}

	private static class MountFinalizer implements Runnable {
		private final L2PcInstance _activeChar;
		private final int _transformId;
		private final int _petRideId;

		MountFinalizer(L2PcInstance activeChar, int transformId, int petRideId) {
			_activeChar = activeChar;
			_transformId = transformId;
			_petRideId = petRideId;
		}

		@Override
		public void run() {
			if (_activeChar.isDead())
			{
				return;
			}
			_activeChar.enableAllSkills();
			_activeChar.setIsCastingNow(false);

			if (_transformId > 0)
			{
				// handled using transformation
				TransformData.getInstance().transformPlayer(_transformId, _activeChar);
			}
			else if (_petRideId > 0)
			{
				_activeChar.mount(_petRideId, 0, false);
			}
		}
	}

}