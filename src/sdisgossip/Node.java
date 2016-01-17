package sdisgossip;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;



public class Node {

        // Some useful defines for the possible states, self-explanatory
        public static final int SUSCEPTIBLE = 0;
        public static final int INFECTIVE  = 1;
        public static final int REMOVED     = 2;
    
	int messageState; 
        int messageStateNEXT;
        int nodeState;
        int nodeStateNEXT;
        int k;
	ArrayList<Node> neighbourNodes = null;
        GridPoint gridPosition = null;
        Node lastCommNode = null;
        Random randGenerator = new Random();
	
	public Node(int xPos, int yPos, int k) {
		this.gridPosition = new GridPoint(xPos, yPos);
		this.messageState = 0;
                this.nodeState = SUSCEPTIBLE;
                this.k = k;
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
            this.nodeStateNEXT    = Node.INFECTIVE;
        }
        
        
        
        public void commitMessageState() {
            this.messageState = this.messageStateNEXT;
            this.nodeState = this.nodeStateNEXT;
        }
        
        public int getNodeState() {
            return this.nodeState;
        }
        
        public void setNodeState(int NEWstate) {
            this.nodeState = NEWstate;
        }
        
        public boolean pushMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update it
            if (otherNode.getMessageState() < this.messageState) {
                // We try to push our update to the other node
                otherNode.setMessageState(this.messageState);
                
                // We return TRUE if we succeded in updating the message
                return true;   
            } else {
                // We return FALSE if the message was already up to date
                return false;  
            }
            
        }
        
         public boolean pushGossipMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update it
            if (otherNode.getMessageState() < this.messageState) {
                // We try to push our update to the other node
                otherNode.setMessageState(this.messageState);
                
                // We return TRUE if we succeded in updating the message
                return true;   
            } else {
                // Since the message was already up to date, the node removes itself with probability 1/k
                if(this.nodeState == Node.INFECTIVE)
                    if(Math.random() < 1.0/k)
                        this.nodeStateNEXT = Node.REMOVED;
                // We return FALSE if the message was already up to date
                return false;  
            }
            
        }
        
        
}