import java.util.ArrayList;
import java.util.List;

import ml.model.Attribute;
import ml.model.DataSet;
import ml.model.Observation;
import ml.slearning.classification.bayes.NaiveBayes;

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
		double t = 1;
		double f = 0;
		List<Observation> obs = new ArrayList<>();
		obs.add(new Observation(t, 1, 1));
		obs.add(new Observation(t, 1, 2));
		obs.add(new Observation(t, 2, 3));
		obs.add(new Observation(t, 2, 3));
		obs.add(new Observation(f, 3, 1));
		obs.add(new Observation(f, 3, 2));
		obs.add(new Observation(f, 1, 1));

		DataSet ds = new DataSet(features, obs, new Attribute("true/false", Boolean.class, 0));
		NaiveBayes nb = new NaiveBayes(ds);
		Object resp = nb.predict(new Observation(0, 1, 2));
		System.out.println(resp);

	}
}
