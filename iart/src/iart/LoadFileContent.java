package iart;

import java.io.BufferedReader;
import java.io.FileReader;

public class LoadFileContent {

	public LoadFileContent(){}

	public void read_nodes(RoadMap map){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader("src/iart/nodes.txt"));
			while ((line = br.readLine()) != null) {
				map.add_node(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void read_edges(RoadMap map){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader("src/iart/edges.txt"));
			while ((line = br.readLine()) != null) {
				map.add_edge(parse_line(line)[0], parse_line(line)[1], parse_line(line)[2]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String[] parse_line(String line) {
		String[] split = line.split(";");
        return split;
	}

}
