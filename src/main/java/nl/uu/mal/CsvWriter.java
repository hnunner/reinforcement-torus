package nl.uu.mal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public final class CsvWriter {

	public static void generateCsvFile(String payoffsPerSkater) {
		try {
			FileWriter writer = new FileWriter("skaters_"
					+ Calendar.getInstance().getTimeInMillis() + ".csv");

			writer.append("ROUND,SKATER,");
			List<Action> actions = Action.createAvailableActions();
			Iterator<Action> actionsIt = actions.iterator();
			while (actionsIt.hasNext()) {
				Action action = actionsIt.next();
				writer.append(String.valueOf(action.getAngle()));
				if (actionsIt.hasNext()) {
					writer.append(",");
				} else {
					writer.append("\n");
				}
			}

			writer.append(payoffsPerSkater);

			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
