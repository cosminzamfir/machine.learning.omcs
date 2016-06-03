package ml.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import util.JsonEncoder;

public class JsonEncoderTest {

	@Test
	public void test1() throws Exception {
		MDP mdp = MDP.instance();
		mdp.addSingleOutcomAction(State.instance(0), State.instance(1), 1, Action.defaultName);
		mdp.addSingleOutcomAction(State.instance(1), State.instance(2), 1, Action.defaultName);
		System.out.println(new JsonEncoder(mdp).encode(0.75));
	}
}
