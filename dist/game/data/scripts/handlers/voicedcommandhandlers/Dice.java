package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.Broadcast;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.util.Rnd;
import java.lang.NumberFormatException;
import java.lang.Math;

public class Dice implements IVoicedCommandHandler {
	private static int MIN_FACES = 3;
	private static int MAX_FACES = 100;
	private static int MIN_DICES = 2;
	private static int MAX_DICES = 10;
	private static int BROADCAST_DISTANCE = 450; // 1/3 of the Giran stars
	private static String DEFAULT_FACES = "6";
	private static String DICE_MAN = "Usage : .dice n m where n is in ["+MIN_FACES+";"+MAX_FACES+"] and optional m is in ["+MIN_DICES+";"+MAX_DICES+"]";

	private static final String[] VOICED_COMMANDS = {
		"dice",
		"coin"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		if(command.equals("dice")) {
			dice(activeChar, params);
		} else {
			coin(activeChar, params);
		}
		return true;
	}

	/**
	 * Roll one or more dices.
	 * .dice 12 to roll a dice with 12 faces
	 * .dice 12 10 to roll ten dices with 12 faces
	 *
	 * .dice n m where n is in [3;100] and optional m is in [1;10]
	 */
	private void dice(L2PcInstance activeChar, String params) {
		if(params == null) {
			params = DEFAULT_FACES; // default
		}
		int count = params.split(" ").length;
		if(count == 2) {
			multipleDices(activeChar, params);
		} else {
			oneDice(activeChar, params);
		}
	}

	/**
	 * Toss a coin with two faces.
	 */
	private void coin(L2PcInstance activeChar, String params) {
		String throwerName = activeChar.getName();
		String coinType;
		String coinResult;
		switch(Math.abs(throwerName.hashCode())%4) {
			case 0: coinType = " en or";
					break;
			case 1: coinType = " en argent";
					break;
			case 2: coinType = " en bronse";
					break;
			case 3: coinType = " en fer";
					break;
			default : coinType = "";
		}
		if(Rnd.get(1, 42)==42) { // just for the fun
			coinResult = "parfaitement... sur la tranche...";
		} else {
			if(Rnd.get(0, 1)==1) {
				coinResult = "le côté arborant un visage bien visible.";
			} else {
				coinResult = "le côté arborant un chiffre bien visible.";
			}
		}
		String message = String.format("La pièce%s lancée par %s retombe, %s",
				coinType,
				throwerName,
				coinResult);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, SystemMessage.sendString(message), BROADCAST_DISTANCE);
	}

	private void multipleDices(L2PcInstance activeChar, String params) {
		String[] paramsArray = params.split(" ");
		int faces;
		int numberOfDices;
		try {
			faces = Integer.parseInt(paramsArray[0]);
			numberOfDices = Integer.parseInt(paramsArray[1]);
		} catch (NumberFormatException e) {
			activeChar.sendMessage(DICE_MAN);
			return;
		}
		if (areValuesOutOfBoundes(faces, numberOfDices)) {
			activeChar.sendMessage(DICE_MAN);
			return;
		}
		StringBuilder message = new StringBuilder(String.format("%s a lancé %d dés à %d faces : ",
				activeChar.getName(),
				numberOfDices,
				faces));
		for (int i = 0; i < numberOfDices; i++) {
			message.append("[");
			message.append(Rnd.get(1, faces));
			message.append("] ");
		}
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, SystemMessage.sendString(message.toString()), BROADCAST_DISTANCE);
	}

	private boolean areValuesOutOfBoundes(int faces, int numberOfDices) {
		return isValueOutOfBoundes(faces) || numberOfDices < MIN_DICES || numberOfDices > MAX_DICES;
	}

	private boolean isValueOutOfBoundes(int faces) {
		return faces < MIN_FACES || faces > MAX_FACES;
	}

	private void oneDice(L2PcInstance activeChar, String params) {
		int faces;
		try {
			faces = Integer.parseInt(params);
		} catch (NumberFormatException e) {
			activeChar.sendMessage("Format : .dice 6");
			return;
		}
		if (isValueOutOfBoundes(faces)) {
			activeChar.sendMessage(DICE_MAN);
			return;
		}
		int diceResult = Rnd.get(1, faces);
		String antipode = "";
		if (diceResult == 1) {
			antipode = ", la poisse incarnée,";
		}
		else if (diceResult == faces) {
			antipode = ", le cul bordé de nouilles,";
		}
		String message = String.format("%s%s a obtenu [%d] sur un dé à %d faces.",
				activeChar.getName(),
				antipode,
				diceResult,
				faces);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, SystemMessage.sendString(message), BROADCAST_DISTANCE);
	}

	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}
