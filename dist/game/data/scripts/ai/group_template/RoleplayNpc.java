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
package ai.group_template;

import com.l2jserver.Config;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

import ai.npc.AbstractNpcAI;

/**
 * Roleplay Npc AI.
 * @author Melua.
 */
public final class RoleplayNpc extends AbstractNpcAI
{
	//@formatter:off
	private static final String[] HUMANOID_SPEECH =
	{
        "Lâche !",
        "Crapule !",
        "Parasite !",
        "Insecte !",
        "Saleté !",
        "Racaille !",
        "Pourceau !",
        "Forban !",
        "Pisseux, tu vas voir !",
        "Ca va saigner !",
        "Coproli... tas d'fiente !",
        "Casse toi pov' con !",
        "Bourses molles, tu vas voir !",
        "Pourquoi moi ?!",
        "J'vais te crever les yeux ! Te crever tout court !",
        "Tête d'aubergine !",
        "Fils de poulpe !",
        "C'est ma femme qui t'envoie ?",
        "Fils d'unijambiste, je vais te faire danser !",
        "Ta maman la mahum !",
        "Ton père le stakato !",
        "Si toi taper moi, moi cogner toi plus fort !",
        "Flaque de pus !",
        "Pisse-froid, tu vas le payer !",
        "Y a que la banque qui a le droit de me régler mon compte !",
        "Je vais te fumer comme un saumon !",
        "Gros bâtard !",
        "Je vais te tuer, même si c'est la dernière chose que je fais !",
        "Toi, tu vas finir par terre !",
        "Le fossoyeur va avoir du boulot !",
        "Bachi-bouzouk !",
        "Pignouf !",
        "Je te prends, je te retourne et je t'enterre !",
        "Pour qui tu me prends ?!",
        "J'ai rien fait, mais tant pis : t'es mort !",
        "Pour qui tu te prends ?!",
        "Tête de Mahum !",
        "Bouffon !",
        "Tocard !",
        "C'est moche la vie, heureusement que la tienne va s'arrêter !",
        "Catin à chevaux !",
        "T'aurais dû te trouver un autre boulot !",
        "Tête d'elpy !",
        "Tête de Keltir !",
        "Je vais tout casser dans ta tête !",
        "Je vais t'écorcher vif !",
        "C'est toi",
        "Mange tes morts !",
        "Sale race !",
        "Pourceau !",
        "Caliborgnon, tu vas voir !",
        "Je vais te faire la peau et les poches !",
        "Tu vas nourrir les vers !",
        "Social-traître !",
        "Tête de keltir !",
        "Elpy des bois !",
        "Oh non, pas encore !",
        "Morue !",
        "Roulure de ruelle !",
        "Fumier à buffalo !",
        "Tu va regretter d'être venu au monde !"
	};
	private static final String[] MERCENARY_SPEECH =
	{
		"Va mourir !",
		"Tu vas mourir !",
		"Je vais t'étriper !",
		"Rejoins tes morts !",
		"Bâtard !",
		"Viens te battre !",
		"Qu'on lui coupe la tête !",
		"A mort !",
		"Tu vas le payer !",
		"Tu penses vraiment survivre ?!",
		"Tu veux te battre ? J'arrive !",
		"Je vais te découper !",
		"Aïe !",
		"Ouch !"
	};
	private static final String[] DIVINE_SPEECH =
	{
		"Païen !",
		"Iconoclaste aveugle !",
		"Pauvre philistin, je vais t'achever !",
		"Alors infidèle, on s'en va sans dire au revoir ?",
		"Charogne impie !",
		"Pourriture abjecte !",
		"Patarin !",
		"Le paradis te sera interdit !",
		"Hérétique, au bûcher !",
		"Vermine !",
		"La sagesse t'a oublié, mortel !",
		"Tu as choisi le mauvais camp !",
		"Parasite !",
		"Pauvre brebis égarée, je vais t'achever !",
		"Fiélon colchique, sois banni !",
		"Béotien, je rappelle ton âme !",
		"Mortel stupide !",
		"Démon !",
		"L'amour du pouvoir te perdra !",
		"J'apporte la mort !",
		"Ton âme te sera retirée !",
		"Bubon des enfers !",
		"Einhasad te brûlera !",
		"Tu viens de perdre ton droit de vivre !",
		"Souillon Shileniste !",
		"A l'assa-saint !",
		"Souillon Kainiste !",
		"La mort sera ta dernière raison de vivre !",
		"Le Mal a une voix, et tu en as pris l'accent !",
		"Tu le paieras de ton sang !",
		"Tu es tombé bien bas, mortel !",
		"Tu vas le payer de ta vie !",
		"Comment oses-tu ?!",
		"Minable scélérat !",
		"Mais tu es fou ?!",
		"Quelle indiginité !"
	};
	private static final String[] DEMONIC_SPEECH =
	{
		"Après toi, j'irais éviscérer ta famille !",
		"Alors, on cherche la mort ?",
		"Direction les enfers pour toi !",
		"La vie est un beau mensonge, ta mort la belle réalité !",
		"Mange tes morts !",
		"Pressé de mourir ?",
		"Marre de vivre ?",
		"Tu es fou, j'aime ça !",
		"Tu es suicidaire, insecte !",
		"Tu as choisi le mauvais camp !",
		"Tu viens de gagner le droit de mourir !",
		"Humus en devenir !",
		"J'apporte ta mort !",
		"La mort sera ta dernière raison de vivre !",
		"Tu veux jouer à ça ? Moi je gagne à ça !",
		"Il veut quoi le singe ?!",
		"Mange tes morts !",
		"C'est pour une pacte ? Hahaha !",
		"Direction les Enfers, c'est toi qui paye le voyage !",
		"Le repas est arrivé ?",
		"Ton âme est à moi !",
		"On me livre à manger maintenant ?!",
		"Ton âme m'affame !",
		"Tu vas souffrir !",
		"Ridicule tocard, je vais t'éventrer !",
		"Tu vas envier les aveugles de ne pas voir la mort en face !",
		"Mortel débile !",
		"Chiure maphrienne !",
		"L'avenir est incertain, le tien se termine ici !",
		"Sympa l'âme, combien ?",
		"Pisse évaïste !",
		"Ca va saigner !",
		"Raclure Pa'agrienne !",
		"C'est un suicide assisté que tu veux ?",
		"Fistule de dieux !",
		"Einhasadien !",
		"Bouffon, tu vas crever !"
	};
	//@formatter:on
	
	private RoleplayNpc()
	{
		super(RoleplayNpc.class.getSimpleName(), "ai/group_template");
		
		if (Config.ROLEPLAY_NPC_MIN_DISTANCE <= 0) {
			return;
		}

		for (L2NpcTemplate template : NpcData.getInstance().getAllNpcOfClassType("L2Monster"))
		{
			if (template.getRace().equals(Race.HUMANOID) || template.getRace().equals(Race.MERCENARY)
				|| template.getRace().equals(Race.DIVINE) || template.getRace().equals(Race.DEMONIC))
			{
				addAttackId(template.getId());
			}
		}
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill)
	{
		final L2Playable playable = (isSummon) ? attacker.getSummon() : attacker;
		final double distance = Util.calculateDistance(npc, playable, true, false);
		
		if ((distance > Config.ROLEPLAY_NPC_MIN_DISTANCE) && (getRandom(100) < 5))
		{
			switch(npc.getRace()) {
				case HUMANOID:
					broadcastNpcSay(npc, Say2.ALL, HUMANOID_SPEECH[Rnd.get(HUMANOID_SPEECH.length)]);
					break;
				case MERCENARY:
					broadcastNpcSay(npc, Say2.ALL, MERCENARY_SPEECH[Rnd.get(MERCENARY_SPEECH.length)]);
					break;
				case DIVINE:
					broadcastNpcSay(npc, Say2.ALL, DIVINE_SPEECH[Rnd.get(DIVINE_SPEECH.length)]);
					break;
				case DEMONIC:
					broadcastNpcSay(npc, Say2.ALL, DEMONIC_SPEECH[Rnd.get(DEMONIC_SPEECH.length)]);
				default:
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	public static void main(String[] args)
	{
		new RoleplayNpc();
	}
}