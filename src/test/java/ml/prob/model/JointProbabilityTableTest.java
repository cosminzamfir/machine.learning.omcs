package ml.prob.model;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import prob.distribution.model.JointProbabilityTable;
import util.MLUtils;

public class JointProbabilityTableTest {

	private static JointProbabilityTable pt;

	@BeforeClass
	public static void beforeClass() {
		
	}

	/** Simpson's paradox with {@link JointProbabilityTable}s */
	@Test
	public void test1() throws Exception {
		pt = new JointProbabilityTable("gender", "department", "admission");
		pt.add(0.019566946531153304, "female", "A", "admitted").
		add(0.004295183384887301, "female", "A", "rejected").
		add(0.0037560760053027007, "female", "B", "admitted").
		add(0.0017675651789660005, "female", "B", "rejected").
		add(0.044547061422890007, "female", "C", "admitted").
		add(0.086473707467962915, "female", "C", "rejected").
		add(0.028999116217410508, "female", "D", "admitted").
		add(0.053855501546619514, "female", "D", "rejected").
		add(0.020839593460008802, "female", "E", "admitted").
		add(0.065992045956694709, "female", "E", "rejected").
		add(0.0052739726027397011, "female", "F", "admitted").
		add(0.070068493150684918, "female", "F", "rejected").
		add(0.11301369863013702, "male", "A", "admitted").
		add(0.069266460450729109, "male", "A", "rejected").
		add(0.077949624392399511, "male", "B", "admitted").
		add(0.045779938135218703, "male", "B", "rejected").
		add(0.026568714096332307, "male", "C", "admitted").
		add(0.045238621299160404, "male", "C", "rejected").
		add(0.030404330534688506, "male", "D", "admitted").
		add(0.061730004418912916, "male", "D", "rejected").
		add(0.011816173221387503, "male", "E", "admitted").
		add(0.030384445426425107, "male", "E", "rejected").
		add(0.0049447635881573011, "male", "F", "admitted").
		add(0.077467962881131211, "male", "F", "rejected");
		
		//compute the overall admission probability per gender in 2 ways
		Assert.assertTrue(pt.conditionOn("gender", "female").p("female", null, "rejected") > pt.conditionOn("gender", "male").p("male", null, "rejected"));
		
		//or
		JointProbabilityTable pt1 = pt.marginalize("department").conditionOn("gender", "female");
		JointProbabilityTable pt2 = pt.marginalize("department").conditionOn("gender", "male");
		Assert.assertTrue(pt1.p("female","admitted") < pt2.p("male", "admitted"));
		Assert.assertTrue(pt1.p("female","rejected") > pt2.p("male", "rejected"));
		
		List<JointProbabilityTable> slicesDeptGender = pt.slice("department", "gender");
		Assert.assertTrue(slicesDeptGender.get(0).p("female", "A", "admitted") > slicesDeptGender.get(1).p("male", "A", "admitted"));
		Assert.assertTrue(slicesDeptGender.get(0).p("female", "A", "rejected") < slicesDeptGender.get(1).p("male", "A", "rejected"));
		//....
	}
	
}
