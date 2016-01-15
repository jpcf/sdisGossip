/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdisgossip;

/**
 *
 * @author José Fonseca, University of Porto
 * 
 *          Porto, Portugal (2016)
 */
public class SdisGossip {
    
    public static void main(String[] args) {
        
        Grid meshNetwork = new Grid(10, 6, 7);
        int currentMsgState = 1;
        
        // DEBUG ONLY: Prints the recently generated network
        meshNetwork.printNetwork();
        
        // Building the neightbour list for EACH of the nodes
        for (int i=0; i < meshNetwork.numNodes; i++)
            meshNetwork.buildNeighbourList(meshNetwork.nodes.get(i));
        
        // DEBUG ONLY: Prints the Neighbour list for a given node
        meshNetwork.nodes.get(0).printNodeNeighbourList();
        
        // The Algorithm Iterations
        
        meshNetwork.nodes.get(0).setMessageState(currentMsgState++); // The initial propagation to the root node
        meshNetwork.nodes.get(0).commitMessageState(); // The initial propagation to the root node
        
        for(int t=0; t < 10; t++) {
            
            // The nodes update their state
            meshNetwork.pushUpdates();
            
            // DEBUG ONLY: Prints the network state
            System.out.println("Iteration " + t + ":");
            meshNetwork.printMessageStateList();
            System.out.println("********************");
            
            // We propagate the next update to the ROOT node
            meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
        }
        
        // DEBUG ONLY: Prints the FINAL network state
        System.out.println("Final Network State: ");
        meshNetwork.printMessageStateList();
        System.out.println("********************");
           
    }
    
}
