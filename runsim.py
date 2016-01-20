import os
import threading
import time
import csv
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import rc

# The Class that extends the Thread, and launches the simulaitons

class singleSimThread (threading.Thread):
    def __init__(self, numNodes, maxk, simType, numSims, verbosity):
        threading.Thread.__init__(self)
        self.cmd = "java -cp ./src sdisgossip.SdisGossip {} {} {} {} {}".format(numNodes, maxk, simType, numSims, verbosity)
    def run(self):
         os.system(self.cmd)

###########################  THE MAIN SCRIPT

# Simulation Parameters
maxK = 5

# Compiles the project
os.system("javac src/sdisgossip/*.java");

# Now, the program runs a simulation for each of the test variants
threadG  = singleSimThread(1000, maxK, "push_gossip",  50, 0)
threadB  = singleSimThread(1000, maxK, "push_blind",   50, 0)
threadC  = singleSimThread(1000, maxK, "push_counter", 50, 0)

print("***** SIMULATION STARTED! *****")
startTime = time.time()    # Keeps track of the start time

# Starts the threads corresponding to each algorithm type
threadG.start()
threadB.start()
threadC.start()
threadG.join()
threadB.join()
threadC.join()

stopTime = time.time()    # Keeps track of when the simulation ended
print("***** SIMULATION ENDED! Elapsed Time: {}".format(stopTime - startTime))

# Reads the csv files and parses the values for plotting

s_counter = np.zeros((maxK,2))
m_counter = np.zeros((maxK,2))
e_counter = np.zeros((maxK,1))
tA_counter = np.zeros((maxK,2))
tL_counter = np.zeros((maxK,2))
tQ_counter = np.zeros((maxK,1))

with open('counterGossip_Push.csv', 'rt') as file:
    k = 0;
    lines = csv.reader(file)
    for line in lines:
        if k != 0:
            s_counter[k-1,0], s_counter[k-1,1] = float(line[1]), float(line[2])
            m_counter[k-1,0], m_counter[k-1,1] = float(line[3]), float(line[4])
            e_counter[k-1,0] = float(line[5])
            tA_counter[k-1,0], tA_counter[k-1,1] = float(line[6]), float(line[7])
            tL_counter[k-1,0], tA_counter[k-1,1] = float(line[8]), float(line[9])
            tQ_counter[k-1,0] = float(line[10])
        k += 1

s_gossip = np.zeros((maxK,2))
m_gossip = np.zeros((maxK,2))
e_gossip = np.zeros((maxK,1))
tA_gossip = np.zeros((maxK,2))
tL_gossip = np.zeros((maxK,2))
tQ_gossip = np.zeros((maxK,1))

with open('pureFeedback_Push.csv', 'rt') as file:
    k = 0;
    lines = csv.reader(file)
    for line in lines:
        if k != 0:
            s_gossip[k-1,0], s_gossip[k-1,1] = float(line[1]), float(line[2])
            m_gossip[k-1,0], m_gossip[k-1,1] = float(line[3]), float(line[4])
            e_gossip[k-1,0] = float(line[5])
            tA_gossip[k-1,0], tA_gossip[k-1,1] = float(line[6]), float(line[7])
            tL_gossip[k-1,0], tA_gossip[k-1,1] = float(line[8]), float(line[9])
            tQ_gossip[k-1,0] = float(line[10])
        k += 1


s_blind = np.zeros((maxK,2))
m_blind = np.zeros((maxK,2))
e_blind = np.zeros((maxK,1))
tA_blind = np.zeros((maxK,2))
tL_blind = np.zeros((maxK,2))
tQ_blind = np.zeros((maxK,1))

with open('blindRemoval_Push.csv', 'rt') as file:
    k = 0;
    lines = csv.reader(file)
    for line in lines:
        if k != 0:
            s_blind[k-1,0], s_blind[k-1,1] = float(line[1]), float(line[2])
            m_blind[k-1,0], m_blind[k-1,1] = float(line[3]), float(line[4])
            e_blind[k-1,0] = float(line[5])
            tA_blind[k-1,0], tA_blind[k-1,1] = float(line[6]), float(line[7])
            tL_blind[k-1,0], tA_blind[k-1,1] = float(line[8]), float(line[9])
            tQ_blind[k-1,0] = float(line[10])
        k += 1
# Prints a summary
print("\nFraction of Susceptible Nodes After no Infective Nodes Left-- Push")

# Plots the results
## Some configurations for the output font and LaTeX rendering
rc('text', usetex=True);
rc('font', family='serif', serif='Times');
plt.figure(1)       # The fraction of susceptible nodes
plt.title("Fraction of Susceptible Nodes After no Infective Nodes Left-- Push")
plt.axis([0, 6, 0, 1])
plt.xlabel(r"k")
plt.ylabel(r"s")
plt.plot(range(1, maxK+1, 1), s_blind[:,0], 'o', label='Blind Removal')
plt.plot(range(1, maxK+1, 1), s_counter[:,0], 'o', label='Feedback & Counters')
plt.plot(range(1, maxK+1, 1), s_gossip[:,0], 'o', label='Naive Gossiping')
plt.legend(loc=1)

plt.figure(2)       # The average traffic per node
plt.title("Average Update Traffic per Node -- Push")
plt.axis([0, 6, 0, 7])
plt.xlabel(r"k")
plt.ylabel(r"m")
plt.plot(range(1, maxK+1, 1), m_blind[:,0], 'o', label='Blind Removal')
plt.plot(range(1, maxK+1, 1), m_counter[:,0], 'o', label='Feedback & Counters')
plt.plot(range(1, maxK+1, 1), m_gossip[:,0], 'o', label='Naive Gossiping')
plt.legend(loc=4)

plt.figure(3)       # The average traffic per node
plt.title("Average Node Update Time -- Push")
plt.axis([0, 6, 8, 13])
plt.ylabel(r"$t_{avg}$ (s)")
plt.xlabel(r"k")
plt.plot(range(1, maxK+1, 1), tA_blind[:,0], 'o', label='Blind Removal')
plt.plot(range(1, maxK+1, 1), tA_counter[:,0], 'o', label='Feedback & Counters')
plt.plot(range(1, maxK+1, 1), tA_gossip[:,0], 'o', label='Naive Gossiping')
plt.legend(loc=4)

plt.figure(4)       # The average traffic per node
plt.title("Last Update Time -- Push")
plt.axis([0, 6, 0, 50])
plt.ylabel(r"$t_{last}$ (s)")
plt.xlabel(r"k")
plt.plot(range(1, maxK+1, 1), tL_blind[:,0], 'o', label='Blind Removal')
plt.plot(range(1, maxK+1, 1), tL_counter[:,0], 'o', label='Feedback & Counters')
plt.plot(range(1, maxK+1, 1), tL_gossip[:,0], 'o', label='Naive Gossiping')
plt.legend(loc=4)
plt.show()
