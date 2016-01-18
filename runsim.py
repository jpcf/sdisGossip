import os
import threading
import time
import csv
import numpy as np
import matplotlib.pyplot as plt

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

# Plots the results
plt.figure(1)
plt.plot(range(1, maxK+1, 1), s_blind[:,0], 'o', label='Blind Removal')
plt.plot(range(1, maxK+1, 1), s_counter[:,0], 'o', label='Feedback & Counters')
plt.plot(range(1, maxK+1, 1), s_gossip[:,0], 'o', label='Naive Gossiping')
plt.legend(loc=1)
plt.show()
