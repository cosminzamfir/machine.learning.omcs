package test;

import java.util.ArrayList;
import java.util.List;

import ml.model.Attribute;
import ml.model.DataSet;
import ml.model.Observation;
import ml.slearning.NaiveBayes;

import org.junit.Test;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class NaiveBayesTest {

	@Test
	public void test1() throws Exception {
		List<Attribute> features = new ArrayList<>();
		features.add(new Attribute("A", String.class,0));
		features.add(new Attribute("B", String.class,1));
		Object t = Boolean.TRUE;
		Object f = Boolean.FALSE;
		List<Observation> obs = new ArrayList<>();
		obs.add(new Observation(t, "m", "b"));
		obs.add(new Observation(t, "m", "s"));
		obs.add(new Observation(t, "q", "q"));
		obs.add(new Observation(t, "q", "q"));
		obs.add(new Observation(f, "h", "b"));
		obs.add(new Observation(f, "h", "q"));
		obs.add(new Observation(f, "m", "b"));

		DataSet ds = new DataSet(features, obs, new Attribute("true/false", Boolean.class, 0));
		NaiveBayes nb = new NaiveBayes(ds);
		Object resp = nb.predict(new Observation(f, "m", "q"));
		System.out.println(resp);

	}
}
