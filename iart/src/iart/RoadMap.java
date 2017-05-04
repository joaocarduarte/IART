package iart;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

public class RoadMap {

	public final String airport = "12"; //airports are typically located in the outskirts of the city
	Graph graph = new SingleGraph("RoadMap");


	public RoadMap(){
		
		LoadFileContent filesContent = new LoadFileContent();
		filesContent.read_nodes(this);
		filesContent.read_edges(this);



		Path p = new Path(graph, "40", "31");
		p.route();
		p.highlight();

		graph.display(false);
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

	/**
	 *
	 * @param stops Array of nodeId's that represent pickup points for passengers
	 * @return Array with stops rearranged, i.e. sorted pickup spots
	 */
	public String[] sortStops(String[] stops){
		//TODO: calculate linear function that passes through airport, that evenly divides stops into two groups (left and right, so to speak)

		//TODO: sort both groups by distance to airport

		//TODO: chose which one to start with (leave the one closest to airport for last, keeps average passenger trip time down and less "cargo" would also mean less fuel consumption)

		//TODO: reverse group chosen as last

		//TODO: append both arrays back as one

		return stops;
	}

}
