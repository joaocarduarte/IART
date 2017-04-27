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
	}
	
	public void add_node(String id, String x, String y){
		Node node = graph.addNode(id);
		node.setAttribute("x", Integer.parseInt(x));
		node.setAttribute("y", Integer.parseInt(y));
	}
	
	public void add_edge(String id, String firstNodeId, String secondNodeId){
		Edge edge = graph.addEdge(id, firstNodeId, secondNodeId);
	}

}
