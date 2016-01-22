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
        int initK;
        boolean useful = false;
	ArrayList<Node> neighbourNodes = null;
        GridPoint gridPosition = null;
        Node lastCommNode = null;
        Random randGenerator = new Random();
	
	public Node(int xPos, int yPos, int k) {
		this.gridPosition = new GridPoint(xPos, yPos);
		this.messageState = 0;
                this.nodeState = SUSCEPTIBLE;
                this.nodeStateNEXT = SUSCEPTIBLE;
                this.k = k;
                this.initK = k;
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
        
        public void commitPullMessageState() {
            this.messageState = this.messageStateNEXT;
            if(this.nodeState != Node.REMOVED)
                this.nodeState = this.nodeStateNEXT;
            /*
            System.out.println("\nUseful: " + this.useful);
            System.out.println("State: " + this.nodeState);
            System.out.println("k: " + this.k);
               */ 
            if (this.nodeState == Node.INFECTIVE) {
                if(this.useful)
                    this.k  = this.initK;
                
                else {
                    this.k--;
                    if (this.k == 0)
                        this.nodeState = Node.REMOVED;
                }
            }
            this.useful = false;

        }
        
        public int getNodeState() {
            return this.nodeState;
        }
        
        public void setNodeState(int NEWstate) {
            this.nodeState = NEWstate;
        }

       public void setNodeStateNEXT(int NEWstate) {
            this.nodeStateNEXT = NEWstate;
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
         
        public boolean pushCounterMessageState() {
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
                    // We decrement k, since we failed on passing a message
                    this.k--;
                
                    // If k is zero, it means that the node failed on passing a message the supposed number of times, so it removes itself
                    if(k == 0)
                        this.nodeStateNEXT = Node.REMOVED;
                // We return FALSE if the message was already up to date
                return false;  
            }
            
        }
        
        public void pushBlindRemovalMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update it
            if (otherNode.getMessageState() < this.messageState) {
                // We try to push our update to the other node
                otherNode.setMessageState(this.messageState); 
            } 
            
            // Regardless of the success in sending the update, the node removes itself with probability 1/k
            if(Math.random() < 1.0/k)
                this.nodeStateNEXT = Node.REMOVED;    
        }
        
        public boolean pullMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update ourselves
            if (otherNode.getMessageState() > this.messageState) {
                // We try to pull the update from the other node
                this.messageState = otherNode.getMessageState();
                this.nodeStateNEXT = Node.INFECTIVE;
                
                // We return TRUE if we succeded in updating the message
                return true;   
            } else {
                // We return FALSE if the message was already up to date
                return false;  
            }
            
        }
        
         public boolean pullGossipMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
            // If that node's message is different than ours, we update ourselves
            if (otherNode.getMessageState() > this.messageState) {
                // We try to pull the update from the other node
                this.messageState = otherNode.getMessageState();
                this.nodeStateNEXT = Node.INFECTIVE;
                
                // We return TRUE if we succeded in updating the message
                return true;   
            } else {
                // Since the message was already up to date, the node removes itself with probability 1/k
                if(otherNode.getNodeState() == Node.INFECTIVE)
                    if(Math.random() < 1.0/k)
                        otherNode.setNodeStateNEXT(Node.REMOVED);
                // We return FALSE if the message was already up to date
                return false;  
            }
            
        }
         
        public boolean pullCounterMessageState() {
            // Pick a random node from the list of Neighbours
            int i = randGenerator.nextInt(this.neighbourNodes.size());
            Node otherNode = neighbourNodes.get(i); 
            // If that node's message is different than ours, we update ourselves
            if (otherNode.getMessageState() > this.messageState) {
                // We try to pull the update from the other node
                this.setMessageState(otherNode.getMessageState());

                otherNode.useful = true;
                this.useful = true;
                
                
                // We return TRUE if we succeded in updating the message
                return true;   
            } 
            return false;
        }
        
        public void pullBlindRemovalMessageState() {
            // Pick a random node from the list of Neighbours
            Node otherNode = neighbourNodes.get(randGenerator.nextInt(this.neighbourNodes.size()));
            
          // If that node's message is different than ours, we update ourselves
            if (otherNode.getMessageState() > this.messageState) {
                // We try to pull the update from the other node
                this.nodeStateNEXT    = Node.INFECTIVE;
                this.messageState = otherNode.getMessageState();
            } 
            
            //Does it even make sense to remove nodes when talking about pulling?
            //It does
            
            // Regardless of the success in sending the update, the node removes itself with probability 1/k
            if(Math.random() < 1.0/k && (otherNode.getNodeState() == Node.INFECTIVE)) // check if infective?
                //this.nodeStateNEXT = Node.REMOVED;    
                otherNode.setNodeStateNEXT(Node.REMOVED);
        }
        
        
}