package iart;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class RoadMap {

	public final static String airport = "12"; //airports are typically located in the outskirts of the city
	private static Graph graph = new SingleGraph("RoadMap");
	private ArrayList<Passenger> passengers = new ArrayList<>();


	public RoadMap(){
		
		LoadFileContent filesContent = new LoadFileContent();
		filesContent.read_nodes(this);
		filesContent.read_edges(this);
		filesContent.read_passengers(this);

		ArrayList<String> stops = new ArrayList<>();
		for (Passenger p: passengers){
			stops.add(p.getPickupSpot());
		}

		ArrayList<Node> sortedStops = sortStops(stops);
		int pathLength = 0;

		for (int i = 0; i < sortedStops.size()-1; i++){
			Node start  = sortedStops.get(i);
			Node finish = sortedStops.get(i+1);

			Path p = new Path(graph, start, finish);
			p.route();
			pathLength += p.getLength();
			p.highlight();
		}


		//450
		/*
		double[] line = lineOfBestFit(stops);
		double y = line[0]*450+line[1];
		this.add_node("48", "450", String.valueOf(y));
		this.add_edge("88", "12", "48");
		*/

		System.out.println(pathLength);
		graph.display(false);
	}

	public static Graph getGraph() {
		return graph;
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

	public void add_passenger(String id, String pickUp, String h, String m){
		int pID = Integer.parseInt(id);
		int hours = Integer.parseInt(h);
		int minutes = Integer.parseInt(m);
		LocalTime pTakeoff = LocalTime.of(hours, minutes);

		passengers.add(new Passenger(pID, pickUp, pTakeoff));
	}

	/**
	 * calculates euclidean distance between Nodes n and this.endNode
	 * and stores it as attribute "heuristic" in Node n
	 *
	 * @param n1
	 * @param n2
	 * @return distance (in a straight line) from n1 to n2
	 */
	public static double heuristic(Node n1, Node n2){
		double x1, y1, x2, y2;
		x1 = n1.getAttribute("x");
		y1 = n1.getAttribute("y");
		x2 = n2.getAttribute("x");
		y2 = n2.getAttribute("y");

		double euclideanDistance = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
		n1.addAttribute("heuristic", euclideanDistance);

		return euclideanDistance;
	}

	/**
	 *
	 * @param stops Array of nodeId's that represent pickup points for passengers
	 * @return Array with stops rearranged, i.e. sorted pickup spots + airport as first and last stop
	 */
	public ArrayList<Node> sortStops(ArrayList<String> stops){
		ArrayList<Path.Step> above = new ArrayList<>();
		ArrayList<Path.Step> below = new ArrayList<>();
		Path p = new Path(graph, null, graph.getNode(airport));
		ArrayList<Node> result = new ArrayList<>();

		//calulate divider line
		double[] bestFit = lineOfBestFit(stops);

		//divide stops
		for (String s: stops){
			Node stop = graph.getNode(s);
			Path.Step step;

			RoadMap.heuristic(stop, p.getEndNode());
			double dist = stop.getAttribute("heuristic");
			//stop.removeAttribute("heuristic");

			step = new Path.Step();
			step.node = stop;
			step.optimisticDistanceToEnd = dist;
			//stop, graph.getNode(airport), 0, dist

			if (isAboveLine(stop, bestFit)){
				above.add(step);
			}
			else below.add(step);
		}

		//sort both groups by distance to airport
		above.sort(new Path.StepComparator());
		below.sort(new Path.StepComparator());

		//TODO: chose which one to start with (leave the one closest to airport for last, keeps average passenger trip time down and less "cargo" would also mean less fuel consumption)
		//ignore for now as route is the same in each direction

		//TODO: reverse group chosen as last
		Collections.reverse(below);

		//append both arrays back as one plus airport as start and finish
		result.add(graph.getNode(airport));
		for (Path.Step st: above){
			result.add(st.node);
		}
		for (Path.Step st: below){
			result.add(st.node);
		}
		result.add(graph.getNode(airport));

		return result;
	}

	public void sortPassengers(){
		this.passengers.sort(new Passenger.PassengerComparator());
	}

	/**
	 *
	 * @param stops Array of nodeId's that represent pickup points for passengers
	 * @return [0]-> slope (m), [1]-> y-intercept (b)  --> as in: y = mx + b
	 */
	public double[] lineOfBestFit(ArrayList<String> stops){
		double xMedian = 0, yMedian = 0;
		double xAirport = graph.getNode(airport).getAttribute("x");
		double yAirport = graph.getNode(airport).getAttribute("y");
		double m, b;

		for (String s: stops){
			Node n = graph.getNode(s);

			xMedian += (double) n.getAttribute("x");
			yMedian += (double) n.getAttribute("y");
		}

		xMedian /= stops.size();
		yMedian /= stops.size();

		if (xMedian != xAirport){
			m = (yMedian-yAirport)/(xMedian-xAirport);
			b = yAirport - m * xAirport;
		}
		else {
			//Not necessary going up to infinity
			m = 999999.9;
			b = 999999.9;
		}

		return new double[] {m, b};
	}

	/**
	 *
	 * @param stop
	 * @param bestFit
	 * @return true if above line of best fit, false otherwise
	 */
	public boolean isAboveLine(Node stop, double[] bestFit){
		double x = stop.getAttribute("x");
		double y = stop.getAttribute("y");

		if (y > bestFit[0]*x + bestFit[1]){
			return true;
		}
		else return false;
	}

}
