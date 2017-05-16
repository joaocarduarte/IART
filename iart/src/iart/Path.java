package iart;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class Path {
    public static class Step {
        Node node;
        Step previousStep;
        double distanceToStart;
        double optimisticDistanceToEnd;

        public Step(){}

        public Step(Node n, Step prev, double distStart, double distEnd) {
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

    /**
     * Custom comparator class used by priority queue for sorting purposes (increasingly by optimisticDistanceToEnd)
     */
    public static class StepComparator implements Comparator<Step> {

        @Override
        public int compare(Step o1, Step o2) {
            return o1.compareTo(o2);
        }
    }



    private Graph graph;
    private Node startNode;
    private Node endNode;
    private ArrayList<Node> result;

    public Path(){}

    public Path(Graph graph, Node start, Node finish){
        this.graph = graph;
        this.startNode = start;
        this.endNode = finish;
        result = new ArrayList<>();
    }

    /**
     * @param n
     *
     * calculates euclidean distance between Nodes n and this.endNode
     * and stores it as attribute "heuristic" in Node n
     */
    public void heuristic(Node n){
        double x1, y1, x2, y2;
        x1 = n.getAttribute("x");
        y1 = n.getAttribute("y");
        x2 = endNode.getAttribute("x");
        y2 = endNode.getAttribute("y");

        n.addAttribute("heuristic", Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)));
    }

    /**
     * Calculates route from startNode to endNode (might be sub-optimal) using A* algorithm
     *
     */
    public void route(){
        ArrayList<Step> visited = new ArrayList<>();
        PriorityQueue<Step> unvisited = new PriorityQueue<>(new StepComparator());

        this.heuristic(startNode);
        Step firstStep = new Step(startNode, null, 0, startNode.getAttribute("heuristic"));
        unvisited.add(firstStep);

        while (unvisited.size() != 0){
            Step s = unvisited.remove();
            visited.add(s);

            if (s.node.equals(endNode)){
                break;
            }

            Node n = s.node;

            for (Edge e:n.getEachEdge()){
                Node m = e.getOpposite(n);

                if (!m.getId().equals(visited.get(visited.size()-1))){
                    this.heuristic(m);

                    double distanceToStart = (double) e.getAttribute("weight") + s.distanceToStart;
                    double optimisticDistanceToEnd = distanceToStart + (double) m.getAttribute("heuristic");

                    unvisited.add(new Step(m, s, distanceToStart, optimisticDistanceToEnd));
                }
            }
        }

        Step s = visited.get(visited.size()-1);
        while (true){
            result.add(s.node);

            if (s.previousStep == null){
                break;
            }

            s = s.previousStep;
        }

        Collections.reverse(result);
    }

    /**
     * Highlights both nodes and edges that make the path stored inside result
     */
    public void highlight(){
        graph.addAttribute("ui.stylesheet", "" +
                "node.important {" +
                "		fill-color: red;" +
                "}" +
                "" +
                "edge.important {" +
                "		fill-color: red;" +
                "}" +
                "node.pickUp {" +
                "       fill-color: yellow;" +
                "}");

        for (int i = 0; i < result.size(); i++){
            if (i == result.size()-1){
                Node n = result.get(i);
                n.addAttribute("ui.class", "pickUp");
                break;
            }

            Node n1 = result.get(i);
            Node n2 = result.get(i+1);
            Edge e  = n1.getEdgeBetween(n2);

            if (i == 0){
                n1.addAttribute("ui.class", "pickUp");
            } else {
                n1.addAttribute("ui.class", "important");
            }
            e.addAttribute("ui.class", "important");
        }
    }
}
