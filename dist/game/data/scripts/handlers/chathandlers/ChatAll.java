/*
 * Copyright (C) 2004-2015 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.chathandlers;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l2jserver.Config;
import com.l2jserver.gameserver.data.xml.impl.RoleplayRewardData;
import com.l2jserver.gameserver.enums.Volume;
import com.l2jserver.gameserver.handler.IChatHandler;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.stat.PcStat;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.ExVitalityPointInfo;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

/**
 * A chat handler
 * @author durgus
 */
public class ChatAll implements IChatHandler
{
	private static Logger _log = Logger.getLogger(ChatAll.class.getName());

	private static final Pattern THREE_LETTER_WORD_PATTERN = Pattern.compile("[A-ZÀ-ÿa-z']{3,}");
	private static final long MAXIMUM_LISTENERS = 8;
	private static final int CHANCE_LISTENER = 25;
	
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	/**
	 * Handle chat type 'all'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String params, String text)
	{
		boolean vcd_used = false;
		if (text.startsWith("."))
		{
			StringTokenizer st = new StringTokenizer(text);
			IVoicedCommandHandler vch;
			String command = "";
			
			if (st.countTokens() > 1)
			{
				command = st.nextToken().substring(1);
				params = text.substring(command.length() + 2);
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			else
			{
				command = text.substring(1);
				if (Config.DEBUG)
				{
					_log.info("Command: " + command);
				}
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			if (vch != null)
			{
				vch.useVoicedCommand(command, activeChar, params);
				vcd_used = true;
			}
			else
			{
				if (Config.DEBUG)
				{
					_log.warning("No handler registered for bypass '" + command + "'");
				}
				vcd_used = false;
			}
		}
		if (!vcd_used)
		{
			if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
			
			/**
			 * Match the character "." literally (Exactly 1 time) Match any character that is NOT a . character. Between one and unlimited times as possible, giving back as needed (greedy)
			 */
			if (text.matches("\\.{1}[^\\.]+"))
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_SYNTAX);
			}
			else
			{
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getAppearance().getVisibleName(), activeChar.getVolume().toString() + activeChar.getLanguage().toString() + text);
				Collection<L2PcInstance> plrs = activeChar.getKnownList().getKnownPlayers().values();
				int audience = 0;
				for (L2PcInstance player : plrs)
				{
					if ((player != null) && activeChar.isInsideRadius(player, 1250, false, true) && !BlockList.isBlocked(player, activeChar))
					{
						if (!player.isInvisible() && activeChar.isInsideRadius(player, Math.min(activeChar.getVolume().getRadius(), Volume.DEFAULT.getRadius()), false, true)) {
							audience++;
						}
						player.sendPacket(cs);
					}
				}

				if (Config.ENABLE_ROLEPLAY_REWARD) {

					long rolepex = 0;
					Matcher matcher = THREE_LETTER_WORD_PATTERN.matcher(text);

					while (matcher.find()) {
						rolepex++;
					}

					rolepex *= Math.min(audience, MAXIMUM_LISTENERS);

					if (rolepex > 0) {

						long addVita = rolepex * Rnd.get(2, 3);

						if ((activeChar.getVitalityPoints() + addVita) <= PcStat.MAX_VITALITY_POINTS) {
							activeChar.updateVitalityPoints(addVita, false, true);
							activeChar.sendPacket(new ExVitalityPointInfo(activeChar.getVitalityPoints()));
						}

						long addExp = (long) (rolepex * RoleplayRewardData.getInstance().getXpForLevel(activeChar.getLevel()) * Config.RATE_XP);
						int addSp = (int) (rolepex * RoleplayRewardData.getInstance().getSpForLevel(activeChar.getLevel()) * Config.RATE_SP);

						activeChar.addExpAndSp(addExp, addSp, false);

						if (Rnd.get(0, 100) >= Math.subtractExact(100, audience * CHANCE_LISTENER)) {
							activeChar.addItem("MoneyByRP", 4355, audience, activeChar, false);
						}
					}
				}

				activeChar.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler.
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}