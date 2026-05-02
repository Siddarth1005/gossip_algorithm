import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    int totalNodes = 10;
    int fanout = 3;
    int rounds = 2;

    // Step 1: create nodes array first (empty), then wire them up
    Node[] nodes = new Node[totalNodes];
    for (int i = 0; i < totalNodes; i++) {
      nodes[i] = new Node(nodes, fanout, rounds, i);
    }

    // Step 2: node 0 originates a message
    UUID messageId = UUID.randomUUID();
    System.out.println("Node 0 starting gossip for message: " + messageId);
    nodes[0].receive(messageId);

    // Step 3: wait for gossip to spread
    TimeUnit.SECONDS.sleep(3);

    // Step 4: print which nodes received the message
    System.out.println("\n--- Results ---");
    int count = 0;
    for (Node node : nodes) {
      boolean received = node.messagesSet.contains(messageId);
      System.out.println("Node " + node.id + ": " + (received ? "✓ received" : "✗ missed"));
      if (received) count++;
    }
    System.out.println("\n" + count + "/" + totalNodes + " nodes received the message");
  }
}