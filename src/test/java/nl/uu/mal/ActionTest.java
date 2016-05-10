package nl.uu.mal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class ActionTest {

	@Test
	public void payoffsTest() {
		List<Action> actions = Action.createAvailableActions();
		Action action = actions.get(0);

		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				action.giveHighReward();
			} else {
				action.giveLowReward();
			}
		}
		action.updateMeanPayoff(10);	// all together 10 round of simulation

		int expCumulatedPayoff = 5 * Properties.HIGH_REWARD + 5 * Properties.LOW_REWARD;
		double expMeanPayoff = Double.valueOf(expCumulatedPayoff) / Double.valueOf(10);

		Assert.assertEquals(expCumulatedPayoff, action.getCumulatedPayoff());
		Assert.assertEquals(expMeanPayoff, action.getMeanPayoff(), 0.01);
	}

	@SuppressWarnings("deprecation")	// action.setCumulatedPayoff(..) kept for this kind of testing, not meant for production
	@Test
	public void sortingActionsTest() {
		Random rand = new Random();

		List<Action> actions = Action.createAvailableActions();
		Iterator<Action> it = actions.iterator();
		while (it.hasNext()) {
			Action action = it.next();
			action.setCumulatedPayoff(rand.nextInt());
		}

		Collections.sort(actions);
		Action prevAction = null;
		it = actions.iterator();
		while (it.hasNext()) {
			Action action = it.next();
			if (prevAction == null) {
				prevAction = action;
			} else {
				Assert.assertTrue(prevAction.getCumulatedPayoff() > action.getCumulatedPayoff());
			}
		}
	}

}
