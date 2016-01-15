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
	
        public void pushUpdates() {
            // The Nodes Push their updates
            for(int i=0; i < this.numNodes; i++) {
                this.nodes.get(i).pushMessageState();
            }
            
            // The updates are committed
            this.commitUpdates();
        }
        
	public void commitUpdates() {
            // We commit the current message state in all nodes
            // This prevents that nodes change state in chain, during the same timestep
            for(int i=0; i < this.numNodes; i++) {
                nodes.get(i).commitMessageState();
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
        
        public void printNetwork() {
		for (int i = 0; i < this.numNodes; i++){
			System.out.println("node " + i + "(" + nodes.get(i).gridPosition.xPos + "," + nodes.get(i).gridPosition.yPos + ")");
		}
	}
        
        public void printMessageStateList() {
            for(int i=0; i < this.numNodes; i++) {
                System.out.println("Node (" + i + ") @(" + nodes.get(i).getXcoordinate() +","+nodes.get(i).getYcoordinate() +"): " + nodes.get(i).getMessageState());
            }
        }
}