package iart;

import java.io.BufferedReader;
import java.io.FileReader;

public class LoadFileContent {
	public final String ROOTDIR = "iart/src/iart/";		//intelliJ
	//public final String ROOTDIR = "src/iart/";			//eclipse

	public LoadFileContent(){}

	private String[] parse_line(String line) {
		String[] split = line.split(";");
		return split;
	}

	public void read_nodes(RoadMap map){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader("nodes.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = parse_line(line);
				map.add_node(parts[0], parts[1], parts[2]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void read_edges(RoadMap map){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader("edges.txt"));
			while ((line = br.readLine()) != null) {
				map.add_edge(parse_line(line)[0], parse_line(line)[1], parse_line(line)[2]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void read_passengers(RoadMap map){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader("passengers.txt"));
			while ((line = br.readLine()) != null) {
				map.add_passenger(parse_line(line)[0], parse_line(line)[1], parse_line(line)[2], parse_line(line)[3]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
