package iart;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

public class RoadMap {

	Graph graph = new SingleGraph("RoadMap");


	public RoadMap(){
		
		LoadFileContent filesContent = new LoadFileContent();
		filesContent.read_nodes(this);
		filesContent.read_edges(this);

		graph.display(false);

		Path p = new Path(graph, "8", "15");
		p.route();
	}
	
	public void add_node(String id, String x, String y){
		Node node = graph.addNode(id);
		node.setAttribute("x", Double.parseDouble(x));
		node.setAttribute("y", Double.parseDouble(y));
		node.addAttribute("ui.label", id);
	}
	
	public void add_edge(String id, String firstNodeId, String secondNodeId){
		Edge edge = graph.addEdge(id, firstNodeId, secondNodeId);

		double x1, y1, x2, y2;
		x1 = graph.getNode(firstNodeId).getAttribute("x");
		y1 = graph.getNode(firstNodeId).getAttribute("y");
		x2 = graph.getNode(secondNodeId).getAttribute("x");
		y2 = graph.getNode(secondNodeId).getAttribute("y");

		edge.setAttribute("weight", Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)));

	}

}
