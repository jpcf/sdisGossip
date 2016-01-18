/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdisgossip;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Fonseca, University of Porto
 * 
 *          Porto, Portugal (2016)
 */

public class SdisGossip {
    
    public static void main(String[] args) {
        // Checks for the correct number of arguments
        if (args.length != 5) 
            System.out.println("USAGE: java -cp ./src sdisgossip.SdisGossip <numNodes> <maxk> <sim_type> <numSims> <verbose>");
        
        // Parsing the simualtion parameters from the input
        int k = Integer.parseInt(args[1]);
        int numSims  = Integer.parseInt(args[3]);
        int numNodes = Integer.parseInt(args[0]);
        
        // Testing the  Pure Feedback Approach
        switch(args[2]) {
            case "push_gossip" :
                new SdisGossip().pureGossipPush_sim(numNodes, numSims, k);
                break;
            case "push_counter" :
                new SdisGossip().counterGossipPush_sim(numNodes, numSims, k);
                break;
            case "push_blind" :
                new SdisGossip().blindRemovalPush_sim(numNodes, numSims, k);
                break;
        }
        
    }  
    
    
    
    public void pureGossipPush_sim(int numNodes, int numSims, int maxk) {
        // The simualtion result vectors
        float[] s = new float[numSims];
        float[] m = new float[numSims];
        float[] t_avrg = new float[numSims];
        int[]   t_last = new int[numSims];
        
        // The output file to which to write
        PrintWriter outputFile = null;
        try {
            outputFile = new PrintWriter("pureFeedback_Push.csv", "US-ASCII");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        outputFile.println("k, S, S(std), m, m(std), e^(-m), t_average, t_average(std), t_last, t_last(std), t_average/t_last");
        
        for(int k=1; k <= maxk; k++) {
        
            float s_sum      = 0;
            float m_sum      = 0;
            float t_avrg_sum = 0;
            float t_last_sum = 0;
            
            System.out.println("Simulating Naive Gossiping (k=" + k+")...");
            
            for (int i=0; i < numSims; i++) {

                Grid meshNetwork = new Grid(numNodes, 40, 80, k);

                // Infecting the root node to start the epidemy
                meshNetwork.infectRootNode(1);

                while(meshNetwork.infectiveNodes > 0) {

                    // The nodes exchange messages
                    meshNetwork.pushGossipUpdates();

                    // DEBUG ONLY: Prints the network state
                    //System.out.println("Iteration " + t++ + ":");
                    //meshNetwork.printStateList();
                    //meshNetwork.printNetworkVariables();
                    //System.out.println("********************");

                    // We propagate the next update to the ROOT node
                    //meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
                }

                // DEBUG ONLY: Prints the FINAL network state
                // System.out.println("Final Network State (k=" + k +"): ");
                // meshNetwork.printStateList();
                // meshNetwork.printNetworkVariables();
                // System.out.println("\ns      = " + meshNetwork.susceptibleNodes/1000.0 + "\ne^(-m) = " + Math.exp(-meshNetwork.trafficTotal/1000.0));
                // System.out.println("********************");

                s[i] = (float)meshNetwork.susceptibleNodes/meshNetwork.numNodes;
                m[i] = (float)meshNetwork.trafficTotal/meshNetwork.numNodes;
                t_avrg[i] = meshNetwork.t_avrg;
                t_last[i] = meshNetwork.t_last;
                s_sum += s[i];
                m_sum += m[i];
                t_avrg_sum += t_avrg[i];
                t_last_sum += t_last[i];
            }

            // DEBUG ONLY
            //for (int i=0; i < numSims; i++) {
            //    System.out.println(s[i] + " " + m[i] + " " + t_avrg[i] + " " + t_last[i]);
            //}
            
            // Prints the average and covariances to a text file
            float s_cov = 0;
            float m_cov = 0;
            float t_avrg_cov = 0;
            float t_last_cov = 0;
            
            for(int i=0; i < numSims; i++) {
                s_cov += ((float)s_sum/numSims - s[i])*((float)s_sum/numSims - s[i]);
                m_cov += ((float)m_sum/numSims - m[i])*((float)m_sum/numSims - m[i]);
                t_avrg_cov += ((float)t_avrg_sum/numSims - t_avrg[i])*((float)t_avrg_sum/numSims - t_avrg[i]);
                t_last_cov += ((float)t_last_sum/numSims - t_last[i])*((float)t_last_sum/numSims - t_last[i]);
            }
            
            outputFile.println(k + "," + (float)s_sum/numSims + "," + Math.sqrt(s_cov/(numNodes-1)) + "," +
                               (float)m_sum/numSims + "," + Math.sqrt(m_cov/(numNodes-1)) + "," + 
                               Math.exp(-(float)m_sum/numSims) + "," +
                               (float)t_avrg_sum/numSims + "," + Math.sqrt(t_avrg_cov/(numNodes-1)) + "," + 
                               (float)t_last_sum/numSims + "," + Math.sqrt(t_last_cov/(numNodes-1)) + "," +
                               (float)t_avrg_sum/t_last_sum);
        }
        
        outputFile.close();
           
    }
    
    public void counterGossipPush_sim(int numNodes, int numSims, int maxk) {
        // The simualtion result vectors
        float[] s = new float[numSims];
        float[] m = new float[numSims];
        float[] t_avrg = new float[numSims];
        int[]   t_last = new int[numSims];
        
        // The output file to which to write
        PrintWriter outputFile = null;
        try {
            outputFile = new PrintWriter("counterGossip_Push.csv", "US-ASCII");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        outputFile.println("k, S, S(std), m, m(std), e^(-m), t_average, t_average(std), t_last, t_last(std), t_average/t_last");
        
        for(int k=1; k <= maxk; k++) {
        
            float s_sum      = 0;
            float m_sum      = 0;
            float t_avrg_sum = 0;
            float t_last_sum = 0;
            
            System.out.println("Simulating Counters Gossiping (k=" + k+")...");
            
            for (int i=0; i < numSims; i++) {

                Grid meshNetwork = new Grid(numNodes, 40, 80, k);

                // Infecting the root node to start the epidemy
                meshNetwork.infectRootNode(1);

                while(meshNetwork.infectiveNodes > 0) {

                    // The nodes exchange messages
                    meshNetwork.pushCounterUpdates();

                    // DEBUG ONLY: Prints the network state
                    //System.out.println("Iteration " + t++ + ":");
                    //meshNetwork.printStateList();
                    //meshNetwork.printNetworkVariables();
                    //System.out.println("********************");

                    // We propagate the next update to the ROOT node
                    //meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
                }

                // DEBUG ONLY: Prints the FINAL network state
                // System.out.println("Final Network State (k=" + k +"): ");
                // meshNetwork.printStateList();
                // meshNetwork.printNetworkVariables();
                // System.out.println("\ns      = " + meshNetwork.susceptibleNodes/1000.0 + "\ne^(-m) = " + Math.exp(-meshNetwork.trafficTotal/1000.0));
                // System.out.println("********************");

                s[i] = (float)meshNetwork.susceptibleNodes/meshNetwork.numNodes;
                m[i] = (float)meshNetwork.trafficTotal/meshNetwork.numNodes;
                t_avrg[i] = meshNetwork.t_avrg;
                t_last[i] = meshNetwork.t_last;
                s_sum += s[i];
                m_sum += m[i];
                t_avrg_sum += t_avrg[i];
                t_last_sum += t_last[i];
            }

            // DEBUG ONLY
            //for (int i=0; i < numSims; i++) {
            //    System.out.println(s[i] + " " + m[i] + " " + t_avrg[i] + " " + t_last[i]);
            //}
            
            // Prints the average and covariances to a text file
            float s_cov = 0;
            float m_cov = 0;
            float t_avrg_cov = 0;
            float t_last_cov = 0;
            
            for(int i=0; i < numSims; i++) {
                s_cov += ((float)s_sum/numSims - s[i])*((float)s_sum/numSims - s[i]);
                m_cov += ((float)m_sum/numSims - m[i])*((float)m_sum/numSims - m[i]);
                t_avrg_cov += ((float)t_avrg_sum/numSims - t_avrg[i])*((float)t_avrg_sum/numSims - t_avrg[i]);
                t_last_cov += ((float)t_last_sum/numSims - t_last[i])*((float)t_last_sum/numSims - t_last[i]);
            }
            
            outputFile.println(k + "," + (float)s_sum/numSims + "," + Math.sqrt(s_cov/(numNodes-1)) + "," +
                               (float)m_sum/numSims + "," + Math.sqrt(m_cov/(numNodes-1)) + "," + 
                               Math.exp(-(float)m_sum/numSims) + "," +
                               (float)t_avrg_sum/numSims + "," + Math.sqrt(t_avrg_cov/(numNodes-1)) + "," + 
                               (float)t_last_sum/numSims + "," + Math.sqrt(t_last_cov/(numNodes-1)) + "," +
                               (float)t_avrg_sum/t_last_sum);
        }
        
        outputFile.close();
           
    }
    
    public void blindRemovalPush_sim(int numNodes, int numSims, int maxk) {
        // The simualtion result vectors
        float[] s = new float[numSims];
        float[] m = new float[numSims];
        float[] t_avrg = new float[numSims];
        int[]   t_last = new int[numSims];
        
        // The output file to which to write
        PrintWriter outputFile = null;
        try {
            outputFile = new PrintWriter("blindRemoval_Push.csv", "US-ASCII");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SdisGossip.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        outputFile.println("k, S, S(std), m, m(std), e^(-m), t_average, t_average(std), t_last, t_last(std), t_average/t_last");
        
        for(int k=1; k <= maxk; k++) {
        
            float s_sum      = 0;
            float m_sum      = 0;
            float t_avrg_sum = 0;
            float t_last_sum = 0;
            
            System.out.println("Simulating Blind Removal Gossiping (k=" + k+")...");
            
            for (int i=0; i < numSims; i++) {

                Grid meshNetwork = new Grid(numNodes, 40, 80, k);

                // Infecting the root node to start the epidemy
                meshNetwork.infectRootNode(1);

                while(meshNetwork.infectiveNodes > 0) {

                    // The nodes exchange messages
                    meshNetwork.pushBlindRemovalUpdates();

                    // DEBUG ONLY: Prints the network state
                    //System.out.println("Iteration " + t++ + ":");
                    //meshNetwork.printStateList();
                    //meshNetwork.printNetworkVariables();
                    //System.out.println("********************");

                    // We propagate the next update to the ROOT node
                    //meshNetwork.nodes.get(0).setMessageState(currentMsgState++);
                }

                // DEBUG ONLY: Prints the FINAL network state
                // System.out.println("Final Network State (k=" + k +"): ");
                // meshNetwork.printStateList();
                // meshNetwork.printNetworkVariables();
                // System.out.println("\ns      = " + meshNetwork.susceptibleNodes/1000.0 + "\ne^(-m) = " + Math.exp(-meshNetwork.trafficTotal/1000.0));
                // System.out.println("********************");

                s[i] = (float)meshNetwork.susceptibleNodes/meshNetwork.numNodes;
                m[i] = (float)meshNetwork.trafficTotal/meshNetwork.numNodes;
                t_avrg[i] = meshNetwork.t_avrg;
                t_last[i] = meshNetwork.t_last;
                s_sum += s[i];
                m_sum += m[i];
                t_avrg_sum += t_avrg[i];
                t_last_sum += t_last[i];
            }

            // DEBUG ONLY
            //for (int i=0; i < numSims; i++) {
            //    System.out.println(s[i] + " " + m[i] + " " + t_avrg[i] + " " + t_last[i]);
            //}
            
            // Prints the average and covariances to a text file
            float s_cov = 0;
            float m_cov = 0;
            float t_avrg_cov = 0;
            float t_last_cov = 0;
            
            for(int i=0; i < numSims; i++) {
                s_cov += ((float)s_sum/numSims - s[i])*((float)s_sum/numSims - s[i]);
                m_cov += ((float)m_sum/numSims - m[i])*((float)m_sum/numSims - m[i]);
                t_avrg_cov += ((float)t_avrg_sum/numSims - t_avrg[i])*((float)t_avrg_sum/numSims - t_avrg[i]);
                t_last_cov += ((float)t_last_sum/numSims - t_last[i])*((float)t_last_sum/numSims - t_last[i]);
            }
            
            outputFile.println(k + "," + (float)s_sum/numSims + "," + Math.sqrt(s_cov/(numNodes-1)) + "," +
                               (float)m_sum/numSims + "," + Math.sqrt(m_cov/(numNodes-1)) + "," + 
                               Math.exp(-(float)m_sum/numSims) + "," +
                               (float)t_avrg_sum/numSims + "," + Math.sqrt(t_avrg_cov/(numNodes-1)) + "," + 
                               (float)t_last_sum/numSims + "," + Math.sqrt(t_last_cov/(numNodes-1)) + "," +
                               (float)t_avrg_sum/t_last_sum);
        }
        
        outputFile.close();
           
    }
}

