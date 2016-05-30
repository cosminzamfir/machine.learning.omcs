package ml.rl.td.view;

import static com.mxgraph.util.mxConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.Transition;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class MDPViewer {

	private static int stateVertexHeight = 50;
	private static int stateVertexWidth = 150;

	private static int stateActionVertexHeight = 30;
	private static int stateActionVertexWidth = 70;

	private MDP mdp;
	private Map<State, mxCell> stateVertices = new HashMap<State, mxCell>();
	private Map<StateAction, mxCell> stateActionVertices = new HashMap<StateAction, mxCell>();

	private Map<Integer, mxCell> edges = new HashMap<>();

	private mxGraph graph;
	private JFrame frame;
	private mxGraphComponent graphComponent;
	private mxHierarchicalLayout layout;


	private MDPViewer(MDP mdp) {
		super();
		this.mdp = mdp;
		init();
	}

	public void display() {
		SwingUtilities.invokeLater(() -> createAndShowGui());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
	}

	private void init() {
		graph = new mxGraph();
		graph.setAutoSizeCells(true);
		graph.getModel().beginUpdate();
		for (State state : mdp.getStateActions().keySet()) {
			List<StateAction> stateActions = mdp.getStateActions().get(state);
			if (stateActions.size() > 1) { //if more than one Action is available in a state, show State-Action-StatePrime 
				for (StateAction stateAction : stateActions) {
					getStateVertex(stateAction.getState());
					getStateActionVertex(stateAction);
					getStateToActionEdge(stateAction);
					for (Transition transition : stateAction.getTransitions().keySet()) {
						getStateVertex(transition.getsPrime());
						getActionToStateTransitionEdge(stateAction, transition);
					}
				}
			} else { //if a single state action availabe, show only State->StatePrime
				StateAction stateAction = stateActions.get(0);
				for (Transition transition : stateAction.getTransitions().keySet()) {
					getStateVertex(transition.getS());
					getStateVertex(transition.getsPrime());
					getStateToStateTransitionEdge(stateAction, transition);
				}

			}
		}
		graph.getModel().endUpdate();
	}

	/**
	 * Get/Create Edge from s to sprime (case when single action available in state)
	 */
	private mxCell getStateToStateTransitionEdge(StateAction stateAction, Transition transition) {
		mxCell res = edges.get(transition.hashCode());
		if (res != null) {
			return res;
		}
		mxCell s = getStateVertex(transition.getS());
		mxCell sprime = getStateVertex(transition.getsPrime());
		res = (mxCell) graph.insertEdge(graph.getDefaultParent(), String.valueOf(transition.hashCode()), getTransitionLabel(stateAction, transition), s, sprime);
		setDefaultEdgeStyle(res);
		edges.put(transition.hashCode(), res);
		return res;
	}

	/**
	 * Get/Create Edge from ActionState to sprime (case when more than one action available in state)
	 */
	private mxCell getActionToStateTransitionEdge(StateAction stateAction, Transition transition) {
		mxCell res = edges.get(transition.hashCode());
		if (res != null) {
			return res;
		}
		mxCell sa = getStateActionVertex(stateAction);
		mxCell s = getStateVertex(transition.getsPrime());
		res = (mxCell) graph.insertEdge(graph.getDefaultParent(), String.valueOf(transition.hashCode()), getTransitionLabel(stateAction, transition), sa, s);
		setDefaultEdgeStyle(res);
		edges.put(transition.hashCode(), res);
		return res;
	}

	private Object getTransitionLabel(StateAction stateAction, Transition transition) {
		return transition.getReward() + ";p=" + stateAction.getProbability(transition);
	}

	private mxCell getStateToActionEdge(StateAction stateAction) {
		mxCell res = edges.get(stateAction.hashCode());
		if (res != null) {
			return res;
		}
		mxCell s = getStateVertex(stateAction.getState());
		mxCell sa = getStateActionVertex(stateAction);
		mxCell edge = (mxCell) graph.insertEdge(graph.getDefaultParent(), String.valueOf(stateAction.hashCode()), null, s, sa);
		setDefaultEdgeStyle(edge);
		edges.put(stateAction.hashCode(), res);
		return res;
	}

	private mxCell getStateActionVertex(StateAction stateAction) {
		mxCell res = stateActionVertices.get(stateAction);
		if (res != null) {
			return res;
		}
		res = (mxCell) graph.insertVertex(graph.getDefaultParent(), String.valueOf(stateAction.hashCode()), getLabel(stateAction), 20, 20,
				stateActionVertexWidth, stateActionVertexHeight);
		setDefaultStateActionVertexStyle(res);
		stateActionVertices.put(stateAction, res);
		return res;
	}

	private String getLabel(StateAction stateAction) {
		return stateAction.getAction().toString();
	}

	private mxCell getStateVertex(State state) {
		mxCell res = stateVertices.get(state);
		if (res != null) {
			return res;
		}
		res = (mxCell) graph.insertVertex(graph.getDefaultParent(), state.toString(), getLabel(state), 20, 20, stateVertexWidth, stateVertexHeight);
		stateVertices.put(state, res);
		setDefaultStateVertexStyle(res);
		return res;
	}

	private String getLabel(State state) {
		return state.toExtendedString();
	}

	private void setDefaultEdgeStyle(mxCell edge) {
		addStyle(edge, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(edge, STYLE_FONTSIZE, 12);
		addStyle(edge, STYLE_FONTCOLOR, "black");
		addStyle(edge, STYLE_STROKECOLOR, "black");
		addStyle(edge, STYLE_STROKEWIDTH, 1);
	}
	
	private void setSelectedEdgeStyle(mxCell edge) {
		addStyle(edge, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(edge, STYLE_FONTSIZE, 15);
		addStyle(edge, STYLE_FONTCOLOR, "black");
		addStyle(edge, STYLE_STROKECOLOR, "red");
		addStyle(edge, STYLE_STROKEWIDTH, 2);
	}

	private void setDefaultStateVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "white");
		addStyle(vertex, STYLE_SHAPE, SHAPE_ELLIPSE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}

	private void setSelectedStateVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "red");
		addStyle(vertex, STYLE_SHAPE, SHAPE_ELLIPSE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}

	private void setCompletedStateVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "lightgreen");
		addStyle(vertex, STYLE_SHAPE, SHAPE_ELLIPSE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}

	private void setDefaultStateActionVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "white");
		addStyle(vertex, STYLE_SHAPE, SHAPE_RECTANGLE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}

	private void createAndShowGui() {
		frame = new JFrame("MDP viewer");
		graphComponent = new mxGraphComponent(graph);
		layout = new mxHierarchicalLayout(graph);
		layout.setUseBoundingBox(true);
		layout.execute(graph.getDefaultParent());
		//frame.getContentPane().setLayout(new GridBagLayout());
		graphComponent.setOpaque(true);
		graphComponent.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

		frame.setContentPane(graphComponent);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void addStyle(mxCell res, String s, Object val) {
		res.setStyle(res.getStyle() + s + "=" + val + ";");
	}

	public static MDPViewer instance(MDP mdp) {
		return new MDPViewer(mdp);
	}

	public void update(State s) {
		graph.getModel().beginUpdate();
		stateVertices.values().forEach((cell) -> setDefaultStateVertexStyle(cell));
		mxCell vertex = getStateVertex(s); 
		vertex.setValue(getLabel(s));
		setSelectedStateVertexStyle(vertex);
		graph.getModel().endUpdate();
		graph.refresh();
	}
	
	public void setCompleted() {
		graph.getModel().beginUpdate();
		stateVertices.values().forEach((cell) -> setCompletedStateVertexStyle(cell));
		graph.getModel().endUpdate();
		graph.refresh();
	}
	
	public void markPolicy(MDPPolicy policy) {
		graph.getModel().beginUpdate();
		for (State s : mdp.getStates()) {
			StateAction sa = policy.getStatePolicy(s).getStateAction();
			if(mdp.getStateActions(s).size() > 1) {
				setSelectedEdgeStyle(getStateToActionEdge(sa));
			}
		}
		graph.getModel().endUpdate();
		graph.refresh();
	}
	

}
