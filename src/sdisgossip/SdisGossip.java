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
        
        int k = 2;
        
        // Creates the grid object
        Grid meshNetwork = new Grid(1000, 40, 80, k);
        
        // The current message state
        int currentMsgState = 1;
        
        // The iteration counter
        int t = 1;
        
        // DEBUG ONLY: Prints the recently generated network
        //meshNetwork.printNetwork();
        
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
            
            // DEBUG ONLY: Prints the network state
            System.out.println("Iteration " + t++ + ":");
            //meshNetwork.printStateList();
            //meshNetwork.printNetworkVariables();
            //System.out.println("********************");
          
            // We propagate the next update to the ROOT node
            //meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
        }
        
        // Prints the FINAL network state
        System.out.println("Final Network State (k=" + k +"): ");
        //meshNetwork.printStateList();
        meshNetwork.printNetworkVariables();
        System.out.println("\ns      = " + meshNetwork.susceptibleNodes/1000.0 + "\ne^(-m) = " + Math.exp(-meshNetwork.trafficTotal/1000.0));
        System.out.println("********************");
        
        
           
    }
    
}
