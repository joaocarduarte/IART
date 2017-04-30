package iart;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Path {
    public class Step {
        Node node;
        Node previousStep;
        double distanceToStart;
        double optimisticDistanceToEnd;

        public Step(Node n, Node prev, double distStart, double distEnd) {
            this.node = n;
            this.previousStep = prev;
            this.distanceToStart = distStart;
            this.optimisticDistanceToEnd = distEnd;
        }

        public int compareTo(Step s){
            if (this.optimisticDistanceToEnd < s.optimisticDistanceToEnd){
                return -1;
            }
            else if (this.optimisticDistanceToEnd == s.optimisticDistanceToEnd){
                return 0;
            }
            else return 1;
        }
    }

    public class StepComparator implements Comparator<Step> {

        @Override
        public int compare(Step o1, Step o2) {
            return o1.compareTo(o2);
        }
    }



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

    /**
     * Calculates route from startNode to endNode (might be sub-optimal) using A* algorithm
     *
     */
    public void route(){
        Node start  = graph.getNode(startNode);
        Node end    = graph.getNode(endNode);

        ArrayList<Step> visited = new ArrayList<>();
        ArrayList<Step> unvisited = new ArrayList<>();

        this.heuristic(start);
        Step s = new Step(start, start, 0, start.getAttribute("heuristic"));
        visited.add(s);

        for (Edge e:start.getEachEdge()){
            Node n = e.getOpposite(start);
            this.heuristic(n);

            unvisited.add(new Step(n, start, e.getAttribute("weight"), n.getAttribute("heuristic")));
        }

        unvisited.sort(new StepComparator());

        for (Step st: unvisited){
            System.out.println(st.node.getId());
        }
    }

}
