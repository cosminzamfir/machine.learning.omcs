package prob.distribution.model;

import static util.MLUtils.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import util.MLUtils;

/**
 * Store joint RV probabilities (1+ random variables)
 * <p>Notation: 
 * <p>rv - a single random variable
 * <p> jrv or rvs - a joint random variable - list a single random variable
 * @author Cosmin Zamfir
 *
 */
public class JointProbabilityTable {

	private static final Logger log = Logger.getLogger(JointProbabilityTable.class);
	/** The random variable names */
	private List<String> rvs;

	/** Maps rv name to possible values */
	private Map<String, Set<Object>> rvValues = new HashMap<>();

	/** Maps joint RV value to probability*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<List<Object>, Double> probTable = new TreeMap<List<Object>, Double>((l1, l2) -> {
		for (int i = 0; i < l1.size(); i++) {
			Comparable o1 = (Comparable) l1.get(i);
			Comparable o2 = (Comparable) l2.get(i);
			if (o1 == null)
				return -1;
			if (o2 == null)
				return 1;
			int res = o1.compareTo(o2);
			if (res != 0)
				return res;
		}
		return 0;
	});

	private double total;
	private String descr;

	public JointProbabilityTable(String... rvNames) {
		this(asList(rvNames));
	}

	public JointProbabilityTable(JointProbabilityTable source) {
		this.rvs = source.rvs;
		this.rvValues = source.rvValues;
	}
	
	public JointProbabilityTable(List<String> rvNames) {
		this.rvs = rvNames;
		for (String rvName : rvNames) {
			rvValues.put(rvName, new LinkedHashSet());
		}
	}


	public JointProbabilityTable add(double p, Object... values) {
		return this.add(p, asList(values));
	}

	public JointProbabilityTable add(double p, List<Object> rvValue) {
		for (int i = 0; i < rvValue.size(); i++) {
			this.rvValues.get(rvs.get(i)).add(rvValue.get(i));
		}
		Double currentP = probTable.get(rvValue);
		currentP = (currentP == null ? p : currentP + p);
		probTable.put(rvValue, currentP);
		total += p;
		return this;
	}

	/**
	 * @param values the array of values of the joint random variable. 
	 * There must be an element in the list for each rv 
	 * The array elements can be:
	 * <ul> 
	 * <li> Single values - exact match
	 * <li> Collection of values - rv value must belong to the collection
	 * <li> <code>null</code> - rv can take any value
	 * </ul>
	 * @return
	 */
	public double p(Object... values) {
		Double res = probTable.get(asList(values));
		if (res != null) {
			return res;
		}
		if (isExactSearch(values)) {
			return 0;
		}
		res = 0.0;
		for (List<Object> jrvValue : probTable.keySet()) {
			if (matches(asList(values), jrvValue)) {
				res += probTable.get(jrvValue);
			}
		}
		return res;
	}
	
	/**
	 * @param pair of rvName - rvValue for the desired randomVariable 
	 * </ul>
	 * @return
	 */
	public double shortP(Object... o) {
		List<String> argsRvs = new ArrayList<>();
		List<Object> argsValues = new ArrayList<>();
		for (int i = 0; i < o.length; i=i+2) {
			argsRvs.add((String) o[i]);
			argsValues.add(o[i+1]);
		}
		Object[] args = new Object[getRvs().size()];
		int index = 0;
		for (int i = 0; i < args.length; i++) {
			if(argsRvs.contains(getRvs().get(i))) {
				args[i] = argsValues.get(index++);
			} else {
				args[i] = null;
			}
		}
		return p(args);
	}

	/**
	 * Marginalize the given random variables
	 * @return the new {@link JointProbabilityTable} containing the remaining rv's
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JointProbabilityTable marginalize(String rv) {
		List<String> l = new ArrayList<String>(rvs);
		int mIdx = l.indexOf(rv);
		l.remove(rv);
		JointProbabilityTable res = new JointProbabilityTable(l);

		Map<List<Object>, Double> pTable = new HashMap<>();
		for (List<Object> rvValue : probTable.keySet()) {
			List<Object> mJRVValue = new ArrayList(rvValue);
			mJRVValue.remove(mIdx);
			Double p = probTable.get(rvValue);
			Double mP = pTable.get(mJRVValue);
			mP = (mP == null ? p : mP + p);
			res.add(mP, mJRVValue);
		}
		return res;
	}

	/**
	 * @return the new {@link JointProbabilityTable}, conditioned on the given rv taking the given value
	 * @param value, exact match if object; in clause if collection
	 */
	@SuppressWarnings("unchecked")
	public JointProbabilityTable conditionOn(String rv, Object value) {
		int rvIndex = rvs.indexOf(rv);
		JointProbabilityTable res = new JointProbabilityTable(rvs);
		res.setDescr(this.getDescr());
		res.addConditionedDescr(rv, value);
		for (List<Object> rvValue : probTable.keySet()) {
			if (value.equals(rvValue.get(rvIndex)) || ((value instanceof Collection)) && ((Collection<Object>) value).contains(rvValue.get(rvIndex))) {
				res.add(probTable.get(rvValue), rvValue);
			}
		}
		return res.normalize();
	}
	
	public JointProbabilityTable normalize() {
		probTable.keySet().forEach(rvValue -> probTable.put(rvValue, probTable.get(rvValue) / total));
		total = 1;
		return this;
	}

	/**
	 * Create a List of {@link JointProbabilityTable} instances, one for each combination of the given random variables values
	 * @param rvs
	 * @return
	 */
	public List<JointProbabilityTable> slice(String... rvs) {
		List<JointProbabilityTable> prev = sliceInternal(rvs[0]);
		List<JointProbabilityTable> current = new ArrayList<>();
		for (int i = 1; i < rvs.length; i++) {
			for (JointProbabilityTable jpt : prev) {
				current.addAll(jpt.sliceInternal(rvs[i]));
			}
			prev = current;
			current = new ArrayList<>();
		}
		return prev;
	}

	private List<JointProbabilityTable> sliceInternal(String rv) {
		List<JointProbabilityTable> res = new ArrayList<>();
		rvValues.get(rv).stream().forEach(value -> res.add(this.conditionOn(rv, value)));
		return res;
	}

	@SuppressWarnings("rawtypes")
	private boolean matches(List<Object> values, List<Object> jrvValue) {
		if (values.size() != jrvValue.size()) {
			throw new RuntimeException("invalid argument lenght: " + MLUtils.toString(values.toArray()));
		}
		for (int i = 0; i < jrvValue.size(); i++) {
			Object value = values.get(i);
			Object key = jrvValue.get(i);
			if (value == null)
				continue;
			if (value instanceof Collection && ((Collection) value).contains(key))
				continue;
			if (value.equals(key))
				continue;
			return false;
		}
		return true;
	}

	private boolean isExactSearch(Object[] values) {
		for (Object o : values) {
			if (o == null || o instanceof Collection) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		if (probTable.isEmpty()) {
			return rvs + ": No data";
		}
		StringBuilder sb = new StringBuilder(rvs + (descr == null ? "\n" : " - " + descr + "\n"));
		for (List<Object> rvValue : probTable.keySet()) {
			sb.append(MLUtils.toString(rvValue.toArray())).append(":").append(probTable.get(rvValue)).append("\n");
		}
		return sb.toString();
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	private String getDescr() {
		return descr;
	}
	
	public List<String> getRvs() {
		return rvs;
	}
	
	public Map<String, Set<Object>> getRvValues() {
		return rvValues;
	}

	private void addConditionedDescr(String rv, Object value) {
		if (descr == null) {
			setDescr("Conditioned on " + rv + " = " + value);
		} else {
			setDescr(descr + "; " + rv + " = " + value);
		}
	}
	
	/**
	 * Check that P(rv1=x1)*P(rv2=x2) = P(rv1=x1 ^ rv2=x2) for all values x1 and x2 of rv1 and rv2 respectively
	 * @param rv1
	 * @param rv2
	 * @return
	 */
	public boolean isIndependent(String rv1, String rv2) {
		Set<Object> vals1 = getRvValues(rv1);
		Set<Object> vals2 = getRvValues(rv2);
		for (Object val1 : vals1) {
			for(Object val2: vals2) {
				double p1 = shortP(rv1,val1);
				double p2 = shortP(rv2,val2);
				double p = shortP(rv1,val1,rv2,val2); 
				
				if(MLUtils.effectiveEquals(p1*p2, p)) {
					log.debug(MessageFormat.format("P({0}={1}) * P({2}={3}) = the joint probability: {4}. OK  ", rv1, val1, rv2, val2, p));
				} else {
					log.debug(MessageFormat.format("P({0}={1}) * P({2}={3}) != the joint probability: {4}. Not independent  ", rv1, val1, rv2, val2, p));
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isIndependent(String rv1, String rv2, String... givenRv) {
		List<JointProbabilityTable> l = slice(givenRv);
		for (JointProbabilityTable t : l) {
			if(!t.isIndependent(rv1, rv2)) {
				return false;
			}
		}
		return true;
	}
	
	public Set<Object> getRvValues(String rv) {
		return getRvValues().get(rv);
	}
}
