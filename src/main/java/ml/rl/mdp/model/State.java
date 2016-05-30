package ml.rl.mdp.model;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class State {

	private static final Logger log = Logger.getLogger(State.class);
	/**States identified by their Id. To support MDPs with predefined states e.g. S1,S2,...,Sn
	 * , as oposded to problems where States are idenditified by their data*/
	private static Map<Integer, State> identifiableStates = new LinkedHashMap<Integer, State>();
	private static NumberFormat nf = NumberFormat.getInstance();
	static {
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
	}
	
	/**The value assigned to this state*/
	private double value;
	private int id;
	private List<Double> valueHistory = new ArrayList<Double>();
	private Map<String, Object> data = new LinkedHashMap<>();
	
	public static List<State> allIdentifiableStates() {
		return new ArrayList<>(identifiableStates.values());
	}
	
	public static State instance(int id) {
		State res = identifiableStates.get(id);
		if(res == null) {
			res = new State(id);
			identifiableStates.put(id, res);
			log.trace("Created new state:" + res);
			return res;
		}
		log.trace("Served existing state:" + res);
		return res;
	}
	
	public static State newInstance() {
		return new State();
	}
	
	public static void removeAll() {
		identifiableStates.clear();
	}
	
	private State(int index) {
		this.id = index;
	}
	
	public State(){}

	public void setValue(double value) {
		this.value = value;
		//valueHistory.add(value);
	}

	public double getValue() {
		return value;
	}
	
	public int getId() {
		return id;
	}
	
	public Object get(String name) {
		return data.get(name);
	}
	
	public void put(String name, Object value) {
		data.put(name, value);
	}

	@Override
	public String toString() {
		return "S" + id;
	}
	 
	public String toExtendedString() {
		StringBuilder sb = new StringBuilder();
		sb.append("S" + id);
		sb.append("\n val=" + nf.format(value)+ "");
		for (String key : data.keySet()) {
			sb.append(MessageFormat.format("\n {0} = {1}",key,nf.format(get(key))));
		}
		return sb.toString();
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof State)) {
			return false;
		}
		return ((State) obj).getId() == id;
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
}
