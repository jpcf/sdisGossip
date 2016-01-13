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
        
        Grid meshNetwork = new Grid(10, 15, 20);
        
        meshNetwork.printNetwork();
        
        System.out.println(meshNetwork.nodes.get(1).DistanceFromNode(meshNetwork.nodes.get(2)));
        
        // Building the neightbour list for EACH of the nodes
        for (int i=0; i < meshNetwork.numNodes; i++)
            meshNetwork.buildNeighbourList(meshNetwork.nodes.get(i));
        
        // Prints the Neighbour list for a given node
        meshNetwork.nodes.get(0).printNodeNeighbourList();
    }
    
}
