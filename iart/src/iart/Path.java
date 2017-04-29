package iart;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;

//how to iterate Edges connected to Node n
/*
for (Edge e:n.getEachEdge()){

}
*/
public class Path {
    private Graph graph;
    private String startNode;
    private String endNode;
    private ArrayList<Node> result;

    public Path(Graph graph, String start, String finish){
        this.graph = graph;
        this.startNode = start;
        this.endNode = finish;
        result = new ArrayList<>();
    }

    /**
     * @param n
     *
     * calculates euclidean distance between Nodes n and endNode
     * and stores it as attribute "heuristic" in Node n
     */
    public void heuristic(Node n){
        double x1, y1, x2, y2;
        x1 = n.getAttribute("x");
        y1 = n.getAttribute("y");
        x2 = graph.getNode(endNode).getAttribute("x");
        y2 = graph.getNode(endNode).getAttribute("y");

        n.addAttribute("heuristic", Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)));
    }

}
