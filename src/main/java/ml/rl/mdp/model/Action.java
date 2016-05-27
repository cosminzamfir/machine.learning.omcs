package ml.rl.mdp.model;

public class Action {

	public static String defaultName = "Action";
	public static String SOUTH = "south";
	public static String NORTH = "north";
	public static String EAST = "east";
	public static String WEST = "west";

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
	
}
