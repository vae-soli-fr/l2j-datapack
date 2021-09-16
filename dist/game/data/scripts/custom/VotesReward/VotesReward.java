package custom.VotesReward;

import com.l2jserver.gameserver.VotesManager;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.SystemMessageId;

public class VotesReward extends Quest {
    private static final int NPC = 60003;
    private static final int MULTISELL = 65001099;
    private static final int GOLD_EINHASAD = 4356;
    private static final int RATIO = 10;

    private VotesReward() {
        super(-1, VotesReward.class.getSimpleName(), "custom");
        this.addStartNpc(NPC);
        this.addTalkId(NPC);
        this.addFirstTalkId(NPC);
    }

    public static void main(String[] args) {
        new VotesReward();
    }

    @Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        return "info.htm";
    }

    @Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (event == null) {
            return null;
        }

        if (event.startsWith("list")) {
            return this.buildHtml(player, available(player));

        } else if (event.startsWith("items")) {
            MultisellData.getInstance().separateAndSend(MULTISELL, player, npc, false);
            return null;

        } else if (event.startsWith("exchange")) {
            String quantity = event.substring(9);
            return this.buildHtml(player, exchange(player, quantity));

        } else {
            return null;
        }
    }

    private String buildHtml(L2PcInstance player, String value) {
        if (value == null) {
            return null;
        }

        String html = this.getHtm(player.getHtmlPrefix(), "list.htm");
        return html.replace("%value%", value);
    }

    private static String available(L2PcInstance player) {
        try {
            return VotesManager.available(player);
        } catch (Exception ex) {
            player.sendPacket(SystemMessageId.NOTHING_HAPPENED);
            return null;
        }
    }

    private static String exchange(L2PcInstance player, String value) {
        try {
            Integer quantity = Integer.valueOf(value);
            String remaining = VotesManager.exchange(player, quantity);
            giveItems(player, GOLD_EINHASAD, Math.multiplyExact(quantity, RATIO));
            return remaining;
        } catch (Exception ex) {
            player.sendPacket(SystemMessageId.NOTHING_HAPPENED);
            return null;
        }
    }
}
