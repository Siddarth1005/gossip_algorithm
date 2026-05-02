import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

class Node{

    int id;
    int fanoutClusters;
    Node[] nodes;
    Set<UUID> messagesSet = ConcurrentHashMap.newKeySet();
    int noOfRounds;
    Random rand = new Random();



    Node(Node[] nodes, int fanoutClusters, int noOfRounds, int id){
        this.id = id;
        this.fanoutClusters = fanoutClusters;
        this.nodes = nodes;
        this.noOfRounds = noOfRounds;
    }


    // Updated send() — gossips k nodes per round, for noOfRounds rounds
    public void send(UUID messageID) {
        for (int round = 0; round < noOfRounds; round++) {
            int numberOfNodesSent = 0;
            while (numberOfNodesSent < fanoutClusters) {
                int index = rand.nextInt(nodes.length);
                if (nodes[index].id != this.id) {
                    nodes[index].receive(messageID);
                    numberOfNodesSent++;
                }
            }
        }
    }

    public boolean receive(UUID messageID){
        if(!messagesSet.contains(messageID)){
//            perform any broadcast algorithm

            messagesSet.add(messageID);
            CompletableFuture.runAsync(() ->{
                send(messageID);
            });
        }
        return  true;
    }


}

//fifo
//casual
//total
//total + fifo