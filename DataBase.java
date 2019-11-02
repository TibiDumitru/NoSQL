import java.util.ArrayList;

class DataBase {
  private String name;
  private int noNodes;
  private int maxCapacity;
  private ArrayList<Node> nodes = new ArrayList<>(noNodes);

  public DataBase(String name, int noNodes, int maxCapacity) {
    this.name = name;
    this.noNodes = noNodes;
    this.maxCapacity = maxCapacity;
    //creeaza si adauga nodurile (goale) in baza de date
    for (int i = 1; i <= noNodes; i++) {
      String nodeName = "Nod" + i;
      Node n = new Node(nodeName);
      nodes.add(n);
    }
  }

  public ArrayList<Node> getNodes() {
    return nodes;
  }

  public int getMaxCapacity() {
    return maxCapacity;
  }

  public int getNoNodes() {
    return noNodes;
  }

  public void setNoNodes(int noNodes) {
    this.noNodes = noNodes;
  }
}
