import java.util.ArrayList;
import java.util.Collections;

class DBOperations {
  //creeaza lista de entitati
  ArrayList<Entity> entities = new ArrayList<>();

  public ArrayList<Entity> getEntities() {
    return entities;
  }

  //parcurge toate nodurile si le sorteaza descrescator dupa timestamp
  public void timestampSort(DataBase DB) {
    int nr;
    TimestampComparator TC = new TimestampComparator();
    for (nr = 0; nr < DB.getNoNodes(); nr++) {
      //sortare instante dupa timestamp
      Collections.sort(DB.getNodes().get(nr).getInstances(), TC);
      Collections.reverse(DB.getNodes().get(nr).getInstances());
    }
  }

  //sterge din noduri toate instantele din lista instancesToRemove
  public void delete(DataBase DB, ArrayList<Instance> instancesToRemove) {
    int nr;
    for (Instance inst : instancesToRemove)
      for (nr = 0; nr < DB.getNoNodes(); nr++)
        //sterge pe rand fiecare element
        DB.getNodes().get(nr).getInstances().remove(inst);
  }

  //gaseste tipul atributului cu numele fieldName din entityName
  public String findType(String entityName, String fieldName) {
    for (Entity en : entities)
      if (en.getName().equals(entityName))
        for (Attribute att : en.getAttributes())
          if (att.getName().equals(fieldName))
            return att.getType();
    return null;
  }

  //afiseaza nodurile care contin enitatea entityName cu cheia primara prKeyVal
  public boolean printNodes(DataBase DB, String entityName, String prKeyVal) {
    boolean hasInstances = false;
    for (Node n : DB.getNodes()) {
      for (Instance inst : n.getInstances()) {
        if (inst.getName().equals(entityName)
                && prKeyVal.equals(inst.getPrimaryKey())) {
          hasInstances = true;
          System.out.print(n.getName() + " ");
        }
      }
    }
    return hasInstances;
  }

  //afiseaza baza de date
  public void snapshot(DataBase DB) {
    boolean isEmpty = true;
    //parcurge nodurile din baza de date pentru afisare
    for (Node n : DB.getNodes()) {
      //afiseaza continutul nodului doar daca nu este gol
      if (!n.getInstances().isEmpty()) {
        isEmpty = false;
        System.out.println(n.getName());
        for (Instance inst : n.getInstances()) {
          System.out.print(inst.getName());
          int nr = 0;
          for (Object value : inst.getFieldsValues()) {
            System.out.print(" " + inst.getNames().get(nr++) + ":" + value);
          }
          System.out.println();
        }
      }
    }
    if (isEmpty)
      System.out.println("EMPTY DB");
  }
}
