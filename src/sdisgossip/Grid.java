package sdisgossip;

import java.util.ArrayList;
import java.util.Collections;

public class Grid {
	
	int numNodes;
	int windowFromOrig;
        int maxDistNodes;
        int k;
	ArrayList<Node> nodes = new ArrayList();
        int susceptibleNodes = 0;
        int infectiveNodes   = 0;
        int removedNodes     = 0;
        int trafficTotal     = 0;

	public Grid(int numNodes, int windowFromOrig, int maxDistNodes, int k) {
		this.windowFromOrig = windowFromOrig;
		this.numNodes = numNodes;
                this.maxDistNodes = maxDistNodes;
		this.nodes.add(0, new Node(0,0,k));  // Assuming a root node @ (0,0)
                this.k = k;
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
                nodes.add(new Node(gridAbsPos.get(i)/this.windowFromOrig, gridAbsPos.get(i) % this.windowFromOrig, k));
            }
            
	}
	
        public void pushUpdates() {
            // The Nodes Push their updates
            for(int i=0; i < this.numNodes; i++) {
                this.nodes.get(i).pushMessageState();
            }
            
            // The updates are committed
            this.updateNetworkVariables();
        }
        
        public void pushGossipUpdates() {
            // The Nodes Push their updates
            for(int i=0; i < this.numNodes; i++) {
                // Only infective nodes can actually communicate
                if (this.nodes.get(i).getNodeState() == Node.INFECTIVE) {
                    boolean success = this.nodes.get(i).pushGossipMessageState();
                    
                    // DEBUG ONLY
                    //if(!success) {
                    //    System.out.println("I didn't succeeded in gossiping :( (Node " + i + ")");
                    //}
                    
                    // We update the traffic counter for another exchanged message
                    this.trafficTotal++;
                    
                }
            }
            
            
            
            // The updates are committed
            this.commitUpdates();
            
            // The network updates its internal statistics
            this.updateNetworkVariables();
            
        }
        
	public void commitUpdates() {
            // We commit the current message state in all nodes
            // This prevents that nodes change state in chain, during the same timestep
            for(int i=0; i < this.numNodes; i++) {
                nodes.get(i).commitMessageState();
            }
        }
        
        public void updateNetworkVariables() {
            this.susceptibleNodes = 0;
            this.infectiveNodes   = 0;
            this.removedNodes     = 0;
            
            for(int i=0; i < this.numNodes; i++) {
                if ( nodes.get(i).getNodeState()        == Node.SUSCEPTIBLE) {
                    this.susceptibleNodes += 1;
                } else if ( nodes.get(i).getNodeState() == Node.INFECTIVE  ) {
                    this.infectiveNodes   += 1;
                } else if ( nodes.get(i).getNodeState() == Node.REMOVED    ) {
                    this.removedNodes     += 1;
                }   
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
        
        public void infectRootNode(int valueToInfect) {
            this.nodes.get(0).setMessageState(valueToInfect); // The initial propagation to the root node
            this.nodes.get(0).commitMessageState(); // The initial propagation to the root node
            this.updateNetworkVariables();
        }        
        
        public void printNetwork() {
		for (int i = 0; i < this.numNodes; i++){
			System.out.println("node " + i + "(" + nodes.get(i).gridPosition.xPos + "," + nodes.get(i).gridPosition.yPos + ")");
		}
	}
        
        public void printStateList() {
            for(int i=0; i < this.numNodes; i++) {
                System.out.println("Node (" + i + ") @(" + nodes.get(i).getXcoordinate() +","+nodes.get(i).getYcoordinate() +"): " + nodes.get(i).getMessageState()
                                                + "--- STATE: " + nodes.get(i).getNodeState());
            }
        }
        
        public void printNetworkVariables() {
            System.out.println("SUSCEPTIBLE NODES : " + this.susceptibleNodes);
            System.out.println("INFECTIVE   NODES : " + this.infectiveNodes);
            System.out.println("REMOVED     NODES : " + this.removedNodes);
            System.out.println("TOTAL TRAFFIC     : " + this.trafficTotal);
        }
}
        
