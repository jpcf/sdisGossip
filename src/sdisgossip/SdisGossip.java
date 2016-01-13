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
        
        Grid meshNetwork = new Grid(10, 7, 7);
        
        meshNetwork.printNetwork();
        
        // Building the neightbour list for EACH of the nodes
        for (int i=0; i < meshNetwork.numNodes; i++)
            meshNetwork.buildNeighbourList(meshNetwork.nodes.get(i));
        
        // DEBUG ONLY: Prints the Neighbour list for a given node
        meshNetwork.nodes.get(0).printNodeNeighbourList();
        
        // The Algorithm Iterations
        
        meshNetwork.nodes.get(0).setMessageState(1); // The initial propagation to the root node
        
        for(int t=0; t < 20; t++) {
            // The Nodes Push their updates
            for(int i=0; i < meshNetwork.numNodes; i++) {
                meshNetwork.nodes.get(i).pushMessageState();
            }
            
            System.out.println("Iteration " + t + ":");
            meshNetwork.printMessageStateList();
            System.out.println("********************");
        }
           
    }
    
}
