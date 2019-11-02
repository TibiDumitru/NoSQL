import java.util.ArrayList;

class Entity {
  private String name;
  private int replicationFactor;
  private ArrayList<Attribute> attributes;

  public Entity(String name, int replicationFactor) {
    this.name = name;
    this.replicationFactor = replicationFactor;
    attributes = new ArrayList<>();
  }

  public ArrayList<Attribute> getAttributes() {
    return attributes;
  }

  public String getName() {
    return name;
  }

  public int getReplicationFactor() {
    return replicationFactor;
  }
}
