package ml.rl.mdp.model;

public class Action {

	private String name;
	
	public Action(String name) {
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
		return new Action("default");
	}
	
}
