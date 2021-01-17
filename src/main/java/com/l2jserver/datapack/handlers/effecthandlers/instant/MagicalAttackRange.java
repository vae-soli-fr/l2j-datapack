/*
 * Copyright © 2004-2021 L2J DataPack
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
package com.l2jserver.datapack.handlers.effecthandlers.instant;

import static com.l2jserver.gameserver.enums.ShotType.BLESSED_SPIRITSHOTS;
import static com.l2jserver.gameserver.enums.ShotType.SPIRITSHOTS;
import static com.l2jserver.gameserver.model.effects.L2EffectType.MAGICAL_ATTACK;
import static com.l2jserver.gameserver.model.stats.Stats.VENGEANCE_SKILL_MAGIC_DAMAGE;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.stats.Formulas;

/**
 * Magical Attack Range instant effect implementation.
 * @author Zoey76
 * @version 2.6.2.0
 */
public final class MagicalAttackRange extends AbstractEffect {
	
	private final double power;
	
	private final double shieldDefensePercent;
	
	public MagicalAttackRange(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		
		power = params.getDouble("power", 0);
		
		shieldDefensePercent = params.getDouble("shieldDefensePercent", 0);
	}
	
	@Override
	public L2EffectType getEffectType() {
		return MAGICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info) {
		final L2Character target = info.getEffected();
		final L2Character activeChar = info.getEffector();
		final Skill skill = info.getSkill();
		
		if (target.isPlayer() && target.getActingPlayer().isFakeDeath()) {
			target.stopFakeDeath(true);
		}
		
		boolean sps = skill.useSpiritShot() && activeChar.isChargedShot(SPIRITSHOTS);
		boolean bss = skill.useSpiritShot() && activeChar.isChargedShot(BLESSED_SPIRITSHOTS);
		final boolean mcrit = Formulas.calcMCrit(activeChar.getMCriticalHit(target, skill));
		final byte shld = Formulas.calcShldUse(activeChar, target, skill);
		double damage = Formulas.calcMagicDam(activeChar, target, skill, shld, shieldDefensePercent, sps, bss, mcrit, power);
		
		if (damage > 0) {
			// Manage attack or cast break of the target (calculating rate, sending message...)
			if (!target.isRaid() && Formulas.calcAtkBreak(target, damage)) {
				target.breakAttack();
				target.breakCast();
			}
			
			// Shield Deflect Magic: Reflect all damage on caster.
			if (target.getStat().calcStat(VENGEANCE_SKILL_MAGIC_DAMAGE, 0, target, skill) > Rnd.get(100)) {
				activeChar.reduceCurrentHp(damage, target, skill);
				activeChar.notifyDamageReceived(damage, target, skill, mcrit, false, true);
			} else {
				target.reduceCurrentHp(damage, activeChar, skill);
				target.notifyDamageReceived(damage, activeChar, skill, mcrit, false, false);
				activeChar.sendDamageMessage(target, (int) damage, mcrit, false, false);
			}
		}
	}
}