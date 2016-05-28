package ml.rl.td.view;

import static com.mxgraph.util.mxConstants.FONT_BOLD;
import static com.mxgraph.util.mxConstants.SHAPE_DOUBLE_RECTANGLE;
import static com.mxgraph.util.mxConstants.SHAPE_RECTANGLE;
import static com.mxgraph.util.mxConstants.STYLE_FILLCOLOR;
import static com.mxgraph.util.mxConstants.STYLE_FONTCOLOR;
import static com.mxgraph.util.mxConstants.STYLE_FONTSIZE;
import static com.mxgraph.util.mxConstants.STYLE_FONTSTYLE;
import static com.mxgraph.util.mxConstants.STYLE_SHAPE;
import static com.mxgraph.util.mxConstants.STYLE_STROKECOLOR;
import static com.mxgraph.util.mxConstants.STYLE_STROKEWIDTH;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.Transition;
import ml.rl.td.TDLambdaRunner;

public class EpisodeViewer {

	private static final Logger log = Logger.getLogger(EpisodeViewer.class);
	private static final String title = "TDLambda solver";
	
	private static int vertexHeighth = 70;
	private static int vertexWidth = 150;
	private Episode episode;
	private mxGraph graph;
	private JFrame frame;
	private mxGraphComponent graphComponent;
	private mxGraphLayout layout;
	private Map<State, mxCell> vertices = new HashMap<>();
	private Map<Transition, mxCell> edges = new HashMap<>();
	private AtomicBoolean clicked = new AtomicBoolean(false);

	public EpisodeViewer(Episode episode) {
		super();
		this.episode = episode;
		init();
	}

	public void display() {
		SwingUtilities.invokeLater(()-> createAndShowGui());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException("",e);
		}
	}
	
	public void createAndShowGui() {
		frame = new JFrame(title);
		graphComponent = new mxGraphComponent(graph);
		layout = new mxHierarchicalLayout(graph);
		layout.setUseBoundingBox(true);
		layout.execute(graph.getDefaultParent());
		//frame.getContentPane().setLayout(new GridBagLayout());
		graphComponent.setOpaque(true);
		graphComponent.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
		
		frame.setContentPane(graphComponent);
		addMouseListener();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void addMouseListener() {
		Component[] components = frame.getComponents();
		for (Component component : components) {
			component.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("clicked");
					clicked.set(true);
				}
			});
		}
	}

	public void init() {
		graph = new mxGraph();
		graph.setAutoSizeCells(true);
		graph.getModel().beginUpdate();
		for (Transition transition : episode.getTransitions()) {
			getEdge(transition);
		}
		graph.getModel().endUpdate();
	}

	private mxCell getEdge(Transition t) {
		mxCell res = edges.get(t);
		if(res != null) {
			return res;
		}
		mxCell vertexFrom = getVertex(t.getS());
		mxCell vertexTo = getVertex(t.getsPrime());
		res = (mxCell) graph.insertEdge(graph.getDefaultParent(), t.toString(), (int) t.getReward(), vertexFrom, vertexTo);
		setDefaultEdgeStyle(res);
		edges.put(t,res);
		return res;
	}
	
	public void setDefaultStyle() {
		vertices.values().forEach((v)-> setDefaultVertexStyle(v));
		edges.values().forEach((e) -> setDefaultEdgeStyle(e));
	}

	private void setDefaultVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "white");
		addStyle(vertex, STYLE_SHAPE, SHAPE_RECTANGLE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}
	
	private void setCompletedVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "lightgreen");
		addStyle(vertex, STYLE_SHAPE, SHAPE_RECTANGLE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}


	private void setSelectedVertexStyle(mxCell vertex) {
		addStyle(vertex, STYLE_STROKECOLOR, "black");
		addStyle(vertex, STYLE_FILLCOLOR, "lightblue");
		addStyle(vertex, STYLE_SHAPE, SHAPE_DOUBLE_RECTANGLE);
		addStyle(vertex, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(vertex, STYLE_FONTSIZE, 12);
	}
	
	private void setDefaultEdgeStyle(mxCell edge) {
		addStyle(edge, STYLE_FONTSTYLE, FONT_BOLD);
		addStyle(edge, STYLE_FONTSIZE, 15);
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
	
	private mxCell getVertex(State s) {
		mxCell res = vertices.get(s);
		if(res != null) {
			return res;
		}
		res = (mxCell) graph.insertVertex(graph.getDefaultParent(), s.toString(), getLabel(s), 20, 20, vertexWidth, vertexHeighth);
		vertices.put(s, res);
		setDefaultVertexStyle(res);
		return res;
	}

	private void addStyle(mxCell res, String s, Object val) {
		res.setStyle(res.getStyle() + s + "=" + val + ";");
	}

	private Object getLabel(State s) {
		return s.toExtendedString();
	}

	public void update(Transition transition) {
		graph.getModel().beginUpdate();
		
		//vertices.values().forEach((v) -> setDefaultVertexStyle(v));
		setSelectedEdgeStyle(getEdge(transition));
		setSelectedVertexStyle(getVertex(transition.getS()));
		setSelectedVertexStyle(getVertex(transition.getsPrime()));
		episode.getAllStates().forEach((s) -> getVertex(s).setValue(s.toExtendedString()));
		
		graph.getModel().endUpdate();
		layout.execute(graph.getDefaultParent());
		graph.refresh();
	}
	
	public void markCompleted() {
		vertices.values().forEach((v) -> setCompletedVertexStyle(v));
		graph.refresh();
	}


	public void waitForMouseClick(String message) {
		//MLUtils.readFromConsole(message);
		
		try {
			Thread.sleep(TDLambdaRunner.updateSpeed);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
		// clicked.set(false);
		// frame.setTitle(title + ". " + message);
		// while (!clicked.get()) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// throw new RuntimeException("", e);
		// }
		// }
	}

}
