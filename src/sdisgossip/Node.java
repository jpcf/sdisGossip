package sdisgossip;

import java.util.ArrayList;
import java.math.*;

public class Node {
	GridPoint gridPosition = null;
	int messageState; 
	ArrayList<Node> neighbourNodes = null;
	
	public Node(int xPos, int yPos) {
		this.gridPosition = new GridPoint(xPos, yPos);
		this.messageState = 0;
                this.neighbourNodes = new ArrayList();
	}
	
        /**
         * Returns this node's X coordinate
         * @return 
         */
        public int getXcoordinate() {
            return this.gridPosition.xPos;
        }
        
        /**
         * Returns this node's Y coordinate
         * @return 
         */
        public int getYcoordinate() {
            return this.gridPosition.yPos;
        }
        
        /**
         * Computes the Manhattan Distance from node otherNode
         * @param otherNode the node to which the distance is computed
         * @return the Manhattan Distance
         */
        public int DistanceFromNode(Node otherNode) {
            return Math.abs(otherNode.getXcoordinate() - this.getXcoordinate()) + Math.abs(otherNode.getYcoordinate() - this.getYcoordinate());
        }
        
        /**
         * Adds a node to the list of Neighbours of this node
         * @param otherNode 
         */
        public void addNodeToNeighbourhood(Node otherNode) {
            this.neighbourNodes.add(otherNode);
        }
        
        public void printNodeNeighbourList() {
            System.out.println("Self Node: (" + this.getXcoordinate() +"," + this.getYcoordinate() +")");
            System.out.println("Neighbour Nodes (size=" + this.neighbourNodes.size() + "): ");
            
            for(int i=0; i < this.neighbourNodes.size(); i++) {
                System.out.println("(" + this.neighbourNodes.get(i).getXcoordinate() +"," + this.neighbourNodes.get(i).getYcoordinate() +")");
            }
        }
}