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
import java.util.List;
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
	private Episode episode;
	private mxGraph graph;
	private JFrame frame;
	private mxGraphComponent graphComponent;
	private mxGraphLayout layout;
	private List<mxCell> vertices = new ArrayList<mxCell>();
	private List<mxCell> edges = new ArrayList<mxCell>();
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
		Optional<mxCell> o = edges.stream().filter((cell) -> (cell.getId().equals(t.toString()))).findAny();
		
		if(o.isPresent()) {
			return o.get();
		}
		mxCell vertexFrom = getVertex(t.getS());
		mxCell vertexTo = getVertex(t.getsPrime());
		mxCell edge = (mxCell) graph.insertEdge(graph.getDefaultParent(), t.toString(), (int) t.getReward(), vertexFrom, vertexTo);
		setDefaultEdgeStyle(edge);
		edges.add(edge);
		return edge;
	}
	
	public void setDefaultStyle() {
		vertices.forEach((v)-> setDefaultVertexStyle(v));
		
		edges.forEach((e) -> setDefaultEdgeStyle(e));
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
		Optional<mxCell> o = vertices.stream().filter((cell) -> (cell.getId().equals(s.toString()))).findAny();
		if(o.isPresent()) {
			return o.get();
		}
		mxCell res = (mxCell) graph.insertVertex(graph.getDefaultParent(), s.toString(), getLabel(s), 20, 20, 120, 70);
		vertices.add(res);
		setDefaultVertexStyle(res);
		return res;
	}

	private void addStyle(mxCell res, String s, Object val) {
		res.setStyle(res.getStyle() + s + "=" + val + ";");
	}

	private Object getLabel(State s) {
		return s.toExtendedString();
	}

	public void markTransition(Transition transition) {
		graph.getModel().beginUpdate();
		setSelectedEdgeStyle(getEdge(transition));
		setSelectedVertexStyle(getVertex(transition.getS()));
		setSelectedVertexStyle(getVertex(transition.getsPrime()));
		graph.getModel().endUpdate();
		layout.execute(graph.getDefaultParent());
		graph.refresh();
	}
	
	public void update(Transition transition) {
		graph.getModel().beginUpdate();
		episode.getAllStates().forEach((s) -> refresh(s));
		graph.getModel().endUpdate();
		layout.execute(graph.getDefaultParent());
		graph.refresh();
	}
	
	public void markCompleted() {
		vertices.forEach((v) -> setCompletedVertexStyle(v));
		graph.refresh();
	}

	private void refresh(State s) {
		mxCell vertex = getVertex(s);
		vertex.setValue(s.toExtendedString());
		setDefaultVertexStyle(vertex);
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
