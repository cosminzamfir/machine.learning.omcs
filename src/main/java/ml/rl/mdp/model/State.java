package ml.rl.mdp.model;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ml.rl.model.Environment;

import org.apache.log4j.Logger;

import util.MLUtils;

public class State {

	private static final Logger log = Logger.getLogger(State.class);
	/**
	* States identified by their Id. To support MDPs with predefined states e.g. S1,S2,...,Sn
	* as oposded to problems where States are idenditified by their data
	*/
	private static Map<Integer, State> identifiableStates = new LinkedHashMap<Integer, State>();

	/** Optional. For States solely identified by their id*/
	private Integer id;

	/**The value assigned to this state*/
	private double value;

	private Map<String, Object> data = new LinkedHashMap<>();

	public static List<State> allIdentifiableStates() {
		return new ArrayList<>(identifiableStates.values());
	}

	public static State instance() {
		return new State();
	}

	public static State instance(int id) {
		State res = identifiableStates.get(id);
		if (res == null) {
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

	public State() {
	}

	public void setValue(double value) {
		this.value = value;
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

	public Integer getInteger(String name) {
		return (Integer) get(name);
	}

	public Long getLong(String name) {
		return (Long) get(name);
	}

	public Double getDouble(String name) {
		return (Double) get(name);
	}

	public String getString(String name) {
		return (String) get(name);
	}

	public Boolean getBoolean(String name) {
		return true == (Boolean) get(name);
	}

	public void set(String name, Object value) {
		data.put(name, value);
	}

	@Override
	public String toString() {
		if (id != null) {
			return "S" + id;
		}
		return toExtendedString();
	}

	public String toExtendedString() {
		StringBuilder sb = new StringBuilder();
		if(id != null) {
			sb.append("S" + id);
		} else {
			sb.append("State");
		}
		sb.append("[val=" + MLUtils.format(value) + "]");
		for (String key : data.keySet()) {
			sb.append(MessageFormat.format("[{0}={1}]", key, MLUtils.format(get(key))));
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof State)) {
			return false;
		}
		if (id != null) {
			return ((State) obj).getId() == id;
		}
		return data.equals(((State) obj).data);
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return data.hashCode();
	}

}
