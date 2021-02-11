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
package quests.Q00289_NoMoreSoupForYou;

import com.l2jserver.gameserver.enums.QuestSound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.util.Util;
import quests.Q00251_NoSecrets.Q00251_NoSecrets;

/**
 * No More Soup For You (289)
 * @author Ayuki
 */
public class Q00289_NoMoreSoupForYou extends Quest
{
	// NPC
	public static final int STAN = 30200;
	// Item
	public static final int SOUP = 15712;

	private static final int MIN_LEVEL = 82;
	
	private static final int[] MOBS = {18908,22779,22786,22787,22788};

	private static final int ICARUS_TRIDENT_REC = 10377;
	private static final int ICARUS_TRIDENT_PART = 10401;
	
	private static final int[] ARMORS_RECIPE ={15812,15813,15814,15791,15787,15784,15781,15778,15775};
	private static final int[] ARMORS_PART ={15774,15773,15772,15693,15657,15654,15651,15648,15645};

	public Q00289_NoMoreSoupForYou()
	{
		super(289, Q00289_NoMoreSoupForYou.class.getSimpleName(), "No More Soup For You");
		addStartNpc(STAN);
		addTalkId(STAN);
		addKillId(MOBS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}

		String htmltext = null;

		switch (event)
		{
			case "30200-03.htm" :
				st.startQuest();
				break;
			case "30200-05.htm" :
				if (st.getQuestItemsCount(SOUP) >= 500)
				{
					if(getRandom(7)<1)
						st.giveItems(ICARUS_TRIDENT_REC,1);
					else
						st.giveItems(ICARUS_TRIDENT_PART,getRandom(6));
					st.takeItems(SOUP, 500);
					st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
					htmltext = "30200-04.htm";
				}
				else
				{
					htmltext = "30200-07.htm";
				}
				break;
			case "30200-06.htm" :
				if (st.getQuestItemsCount(SOUP) >= 100)
				{
					int rnd = getRandom(18);
					if(rnd>=9)
						st.giveItems(ARMORS_PART[rnd-9], 5);
					else
						st.giveItems(ARMORS_RECIPE[rnd],1);
					st.takeItems(SOUP, 100);
					st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
					htmltext = "30200-04.htm";
				}
				else
				{
					htmltext = "30200-07.htm";
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		QuestState st = getQuestState(player, false);
		int npcId = npc.getId();
		if (Util.contains(MOBS, npcId)) {
			st.giveItemRandomly(npc,SOUP,1,1,0,1,true);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = getQuestState(player, true);

		if (st == null)
		{
			return htmltext;
		}

		switch (st.getState())
		{
			case State.CREATED:
				st = player.getQuestState(Q00251_NoSecrets.class.getSimpleName());
				htmltext = ((player.getLevel() >= MIN_LEVEL) && (st != null) && (st.isCompleted())) ? "30200-01.html" : "30200-00.htm";
				break;
			case State.STARTED:
				if (st.isCond(1))
				{
					htmltext = (st.getQuestItemsCount(SOUP) >= 100) ? "30200-04.htm" : "30200-03.htm";
				}
				break;
		}
		return htmltext;
	}
}
