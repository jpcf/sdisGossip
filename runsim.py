import os
import threading
import time

class singleSimThread (threading.Thread):
    def __init__(self, numNodes, maxk, simType, numSims, verbosity):
        threading.Thread.__init__(self)
        self.cmd = "java -cp ./src sdisgossip.SdisGossip {} {} {} {} {}".format(numNodes, maxk, simType, numSims, verbosity)
    def run(self):
         os.system(self.cmd)


###########################  THE MAIN SCRIPT

# Compiles the project
os.system("javac src/sdisgossip/*.java");

# Now, the program runs a simulation for each of the test variants
threadG  = singleSimThread(1000, 5, "push_gossip",  1000, 0)
threadB  = singleSimThread(1000, 5, "push_blind",   1000, 0)
threadC  = singleSimThread(1000, 5, "push_counter", 1000, 0)

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


