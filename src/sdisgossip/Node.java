package sdisgossip;

import java.util.ArrayList;
import java.util.Random;

public class Node {
	
	int messageState; 
        int messageStateNEXT;
	ArrayList<Node> neighbourNodes = null;
        GridPoint gridPosition = null;
        Node lastCommNode = null;
        Random randGenerator = new Random();
	
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
        
        public int getMessageState() {
            return this.messageState;
        }
        
        public void setMessageState(int NEWmessageState) {
            this.messageStateNEXT = NEWmessageState;
        }
        
        public boolean pushMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update it
            if (otherNode.getMessageState() < this.messageState) {
                otherNode.setMessageState(this.messageState);
                return true;   // We return TRUE if we succeded in updating the message
            } else {
                return false;  // We return FALSE if the message was already up to date
            }
            
        }
        
        public void commitMessageState() {
            this.messageState = this.messageStateNEXT;
        }
}