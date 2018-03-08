package ml.prob.model;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import prob.distribution.model.JointProbabilityTable;
import static java.lang.Math.*;

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
		Assert.assertTrue(pt1.p("female", "admitted") < pt2.p("male", "admitted"));
		Assert.assertTrue(pt1.p("female", "rejected") > pt2.p("male", "rejected"));

		List<JointProbabilityTable> slicesDeptGender = pt.slice("department", "gender");
		Assert.assertTrue(slicesDeptGender.get(0).p("female", "A", "admitted") > slicesDeptGender.get(1).p("male", "A", "admitted"));
		Assert.assertTrue(slicesDeptGender.get(0).p("female", "A", "rejected") < slicesDeptGender.get(1).p("male", "A", "rejected"));
		//....
	}

	@Test
	public void test2() throws Exception {
		JointProbabilityTable pt = new JointProbabilityTable("x", "y");
		pt.add(2, 1, 1).add(10, 1, 3).add(5, 2, 1).add(13, 2, 3).add(17, 4, 1).add(25, 4, 3);
		System.out.println(pt.normalize());
		System.out.println(pt.marginalize("y"));
		System.out.println(pt.marginalize("x"));
	}

	@Test
	public void testShortP() throws Exception {
		JointProbabilityTable pt = new JointProbabilityTable("x", "y");
		pt.add(0.25, 0, 0).add(0.25, 0, 1).add(0.25, 1, 0).add(0.25, 1, 1);
		Assert.assertEquals(0.5, pt.shortP("x", 0), 0.01);
		Assert.assertEquals(0.5, pt.shortP("x", 1), 0.01);
		Assert.assertEquals(0.5, pt.shortP("y", 0), 0.01);
		Assert.assertEquals(0.5, pt.shortP("y", 1), 0.01);
		Assert.assertEquals(0.25, pt.shortP("x", 1, "y", 0), 0.01);
	}

	@Test
	public void testInindependent1() throws Exception {
		JointProbabilityTable pt = new JointProbabilityTable("x", "y");
		pt.add(0.25, 0, 0).add(0.25, 0, 1).add(0.25, 1, 0).add(0.25, 1, 1);
		Assert.assertTrue(pt.isIndependent("x", "y"));
	}
	
	@Test
	public void testIndependent2() throws Exception {
		int[] a = {1,-1};
		double alfa12 = 3.45;
		double alfa13 = 0;
		double alfa23 = 5.65;
		JointProbabilityTable pt = new JointProbabilityTable("x1", "x2", "x3");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				for (int k = 0; k < a.length; k++) {
					pt.add(exp(alfa12*a[i]*a[j]) * exp(alfa13*a[i]*a[k]) * exp(alfa23*a[j]*a[k]), a[i],a[j],a[k]);
				}
			}
		}
		pt.normalize();
		System.out.println(pt);
		System.out.println("x1 is independent on x3, given x2? " + pt.isIndependent("x1", "x3", "x2"));
		
	}

	@Test
	public void testIndependent3() throws Exception {
		int[] a = {1,-1};
		double alfa12 = 3.45;
		double alfa13 = 3;
		double alfa23 = 5.65;
		JointProbabilityTable pt = new JointProbabilityTable("x1", "x2", "x3");
		for (int i1 = 0; i1 < a.length; i1++) {
			for (int i2 = 0; i2 < a.length; i2++) {
				for (int i3 = 0; i3 < a.length; i3++) {
					pt.add(exp(alfa12*a[i1]*a[i2]) * exp(alfa13*a[i1]*a[i3]) * exp(alfa23*a[i2]*a[i3]), a[i1],a[i2],a[i3]);
				}
			}
		}
		pt.normalize();
		System.out.println(pt);
		System.out.println("x1 is independent on x3, given x2? " + pt.isIndependent("x1", "x3", "x2"));
	}

	@Test
	public void testInindependent2() throws Exception {
		JointProbabilityTable pt = new JointProbabilityTable("x", "y");
		pt.add(0.20, 0, 0).add(0.30, 0, 1).add(0.25, 1, 0).add(0.25, 1, 1);
		Assert.assertFalse(pt.isIndependent("x", "y"));
	}

}
