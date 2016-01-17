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
        
        // Creates the grid object
        Grid meshNetwork = new Grid(10, 5, 10, 2);
        
        // The current message state
        int currentMsgState = 1;
        
        // The iteration counter
        int t = 1;
        
        // DEBUG ONLY: Prints the recently generated network
        meshNetwork.printNetwork();
        
        // Building the neightbour list for EACH of the nodes
        for (int i=0; i < meshNetwork.numNodes; i++)
            meshNetwork.buildNeighbourList(meshNetwork.nodes.get(i));
        
        // DEBUG ONLY: Prints the Neighbour list for a given node
        //meshNetwork.nodes.get(0).printNodeNeighbourList();
        
        // ------- The Algorithm Iterations -------- //
        
        // Infecting the root node to start the epidemy
        meshNetwork.infectRootNode(currentMsgState++);
        
        while(meshNetwork.infectiveNodes > 0) {
            
            // The nodes exchange messages
            meshNetwork.pushGossipUpdates();
            
            // The network updates itself;
            meshNetwork.updateNetworkVariables();
            
            // DEBUG ONLY: Prints the network state
            System.out.println("Iteration " + t++ + ":");
            meshNetwork.printStateList();
            meshNetwork.printNetworkVariables();
            System.out.println("********************");
          
            // We propagate the next update to the ROOT node
            //meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
        }
        
        // DEBUG ONLY: Prints the FINAL network state
        //System.out.println("Final Network State: ");
        //meshNetwork.printStateList();
        //System.out.println("********************");
        
        
           
    }
    
}
