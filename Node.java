import java.util.ArrayList;

class Node {
  private String name;
  private ArrayList<Instance> instances;

  public Node(String name) {
    this.name = name;
    instances = new ArrayList<>();
  }

  public ArrayList<Instance> getInstances() {
    return instances;
  }

  public String getName() {
    return name;
  }
}
