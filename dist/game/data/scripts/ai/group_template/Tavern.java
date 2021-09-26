/*
 * Copyright (C) 2004-2016 L2J DataPack
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
package ai.group_template;

import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.clientpackets.Say2;

import ai.npc.AbstractNpcAI;

/**
 * @author Melua
 */
public final class Tavern extends AbstractNpcAI
{
	private static final String CANNOT_CARRY_A_WEAPON = "Les armes ne sont pas autorisees ici !";
	private static final int GUARD = 60017;
	private static final int ZONE = 55501;
	
	public Tavern()
	{
		super(Tavern.class.getSimpleName(), "ai/group_template");
		addNpcHateId(GUARD);
		addAggroRangeEnterId(GUARD);
		addEnterZoneId(ZONE);
	}
	
	@Override
	public boolean onNpcHate(L2Attackable mob, L2PcInstance player, boolean isSummon)
	{
		return player.getActiveWeaponInstance() != null;
	}
	
	@Override
	public final String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (character.isPlayer())
		{
			character.getActingPlayer().disarmWeapons();
		}
		return super.onEnterZone(character, zone);
	}

	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		if (player.getActiveWeaponInstance() != null)
		{
			if (!npc.isInCombat())
			{
				broadcastNpcSay(npc, Say2.NPC_ALL, CANNOT_CARRY_A_WEAPON);
			}
			
			addAttackPlayerDesire(npc, player);
		}
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Tavern();
	}
	
}