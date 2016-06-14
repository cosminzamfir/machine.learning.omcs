package ml.rl.mdp.model;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Action {

	public static String defaultName = "Action";
	public static String SOUTH = "south";
	public static String NORTH = "north";
	public static String EAST = "east";
	public static String WEST = "west";
	public static String NORTH_EAST = "north-east";

	private String name;
	
	private Action(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public static Action defaultAction() {
		return new Action("Action");
	}

	public static Action instance(String actionName) {
		return new Action(actionName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Action)) {
			return false;
		}
		Action other = (Action) obj;
		return name.equals(other.getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
