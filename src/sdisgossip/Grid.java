package sdisgossip;

import java.util.ArrayList;
import java.util.Collections;

public class Grid {
	
	int numNodes;
	int windowFromOrig;
        int maxDistNodes;
	ArrayList<Node> nodes = new ArrayList();

	public Grid(int numNodes, int windowFromOrig, int maxDistNodes) {
		this.windowFromOrig = windowFromOrig;
		this.numNodes = numNodes;
                this.maxDistNodes = maxDistNodes;
		this.nodes.add(0, new Node(0,0));  // Assuming a root node @ (0,0)~
		this.createNetwork();
	}
	
	public void createNetwork() { 
            // Creates an ArrayList with the possible positions for the nodes
            // This guarantees a unique position for every node.
            // The list begins ordered...
            ArrayList<Integer> gridAbsPos = new ArrayList();
            for (int i=0; i < this.windowFromOrig*this.windowFromOrig; i++) {
                gridAbsPos.add(i);
            }
            
            // ...and now we shuffle it!
            Collections.shuffle(gridAbsPos);
            
            // And we assign each new node to its position
            for(int i=0; i < this.numNodes-1; i++) {
                nodes.add(new Node(gridAbsPos.get(i)/this.windowFromOrig, gridAbsPos.get(i) % this.windowFromOrig));
            }
            
	}
	
	public void printNetwork() {
		for (int i = 0; i < this.numNodes; i++){
			System.out.println("node " + i + "(" + nodes.get(i).gridPosition.xPos + "," + nodes.get(i).gridPosition.yPos + ")");
		}
	}
        
        public int buildNeighbourList(Node node) {
            
            int count = 0;
            
            for(int i=0; i < nodes.size(); i++) {
                // If it is the node itself, disregard
                if (nodes.get(i).getXcoordinate() == node.getXcoordinate() && nodes.get(i).getYcoordinate() == node.getYcoordinate())    
                    continue;
                // If it is at a smaller distance than a given value, add it to the neighbour list
                if (node.DistanceFromNode(nodes.get(i)) < this.maxDistNodes) {
                    node.addNodeToNeighbourhood(nodes.get(i));
                    count++;
                }
            }
            
            return count;
        }
}