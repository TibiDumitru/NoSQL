import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Tema2 {

  public static void main(String[] args) {
    try {
      DecimalFormat df = new DecimalFormat("#.##");
      String inputFile = args[0];
      String outputFile = inputFile + "_out";
      //citire din fisier
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      //scriere in fisier de output
      PrintStream fileStream = new PrintStream(outputFile);
      System.setOut(fileStream);
      DataBase DB = null;
      String line;
      DBOperations op = new DBOperations();
      //citire comenzi din fisier de input
      int i;
      while (true) {
        line = reader.readLine();
        if (line == null)
          break;
        String[] tokens = line.split(" ");
        if (tokens[0].equals("CREATEDB")) {
          String DBName = tokens[1];
          int noNodes = Integer.parseInt(tokens[2]);
          int maxCapacity = Integer.parseInt(tokens[3]);
          //creeaza baza de date
          DB = new DataBase(DBName, noNodes, maxCapacity);

        } else if (tokens[0].equals("CREATE")) {
          String entityName = tokens[1];
          int RF = Integer.parseInt(tokens[2]);
          //creeaza entitate
          Entity entity = new Entity(entityName, RF);
          int noAttributes = Integer.parseInt(tokens[3]);
          //adauga atributele entitatii
          for (i = 0; i < noAttributes; i++) {
            String attributeName = tokens[4 + 2 * i];
            String attributeType = tokens[4 + 2 * i + 1];
            Attribute attribute = new Attribute(attributeName, attributeType);
            entity.getAttributes().add(attribute);
          }
          op.getEntities().add(entity);

        } else if (tokens[0].equals("INSERT")) {
          String insertName = tokens[1];
          int k = 1;
          //construieste instanta entitatii
          Instance instance = new Instance(insertName);
          //cauta entitatea cu numele respectiv
          for (Entity e : op.getEntities()) {
            //daca s-a gasit entitatea cautata
            if (e.getName().equals(insertName)) {
              int replicationFactor = e.getReplicationFactor();
              for (Attribute a : e.getAttributes()) {
                String value = tokens[++k];
                instance.getNames().add(a.getName());
                //seteaza timestamp-ul instantei
                instance.setTimestamp(System.nanoTime());
                //ia fiecare atribut si il adauga convertit
                if (a.getType().equals("Integer"))
                  instance.getFieldsValues().add(Integer.parseInt(value));
                else if (a.getType().equals("Float")) {
                  Float valueF = Float.parseFloat(value);
                  instance.getFieldsValues().add(df.format(valueF));
                } else
                  instance.getFieldsValues().add(value);
              }
              //afla numarul de noduri in care se mai poate adauga
              int freeSlots = 0;
              for (Node n : DB.getNodes())
                if (DB.getMaxCapacity() - n.getInstances().size() > 0)
                  freeSlots++;
              //compara numarul de noduri in care se mai poate adauga
              //cu numarul necesar de noduri
              if (freeSlots - replicationFactor < 0) {
                int nr;
                //adauga cate noduri mai sunt nevoie
                for (nr = 0; nr < replicationFactor - freeSlots; nr++) {
                  DB.setNoNodes(DB.getNoNodes() + 1);
                  Node node = new Node("Nod" + DB.getNoNodes());
                  DB.getNodes().add(node);
                }
              }
              for (Node n : DB.getNodes()) {
                //daca mai este loc in nod
                if (n.getInstances().size() < DB.getMaxCapacity()) {
                  //adauga instanta si continua
                  n.getInstances().add(instance);
                  replicationFactor--;
                  if (replicationFactor == 0)
                    break;
                }
              }
              break;
            }
          }
          //sorteaza instantele dupa timestamp
          op.timestampSort(DB);

        } else if (tokens[0].equals("DELETE")) {
          String deleteName = tokens[1];
          String primaryKeyValue = tokens[2];
          ArrayList<Instance> instancesToRemove = new ArrayList<>();
          for (Node n : DB.getNodes())
            for (Instance inst : n.getInstances())
              if (inst.getName().equals(deleteName)
                      && primaryKeyValue.equals(inst.getPrimaryKey()))
                //construieste o lista cu elementele care trebuie eliminate
                instancesToRemove.add(inst);
          if (!instancesToRemove.isEmpty())
            op.delete(DB, instancesToRemove);
          else
            System.out.println("NO INSTANCE TO DELETE");

        } else if (tokens[0].equals("GET")) {
          String entityName = tokens[1];
          String primaryKeyValue = tokens[2];
          //afiseaza numele nodurilor in care apare si afla daca are instante
          boolean hasInstances = op.printNodes(DB, entityName, primaryKeyValue);
          boolean print = false;
          if (hasInstances) {
            System.out.print(entityName);
            for (Node n : DB.getNodes()) {
              for (Instance inst : n.getInstances()) {
                for (Object value : inst.getFieldsValues())
                  if (inst.getName().equals(entityName)
                          && primaryKeyValue.equals(inst.getPrimaryKey())) {
                    int index = inst.getFieldsValues().indexOf(value);
                    System.out.print(" " + inst.getNames().get(index) + ":" + value);
                    print = true;
                  }
              }
              if (print) {
                System.out.println();
                break;
              }
            }
          } else
            System.out.println("NO INSTANCE FOUND");

        } else if (tokens[0].equals("UPDATE")) {
          String entityName = tokens[1];
          String primaryKeyValue = tokens[2];
          String fieldName = tokens[3];
          String newValue = tokens[4];
          //afla tipul elementului care trebuie modificat
          String type = op.findType(entityName, fieldName);
          //parcurge nodurile
          for (Node n : DB.getNodes()) {
            //parcurge instantele
            for (Instance inst : n.getInstances()) {
              int nr = 0;
              while (nr < inst.getNames().size()) {
                //daca numele corespunde cu numele campului de modificat
                if (inst.getNames().get(nr).equals(fieldName)) {
                  //daca numele entitatii si cheia primara corespund
                  if (inst.getName().equals(entityName)
                          && primaryKeyValue.equals(inst.getPrimaryKey())) {
                    //modifica valoarea de la indicele nr cu newValue
                    if (type.equals("Integer")) {
                      Integer newValueI = Integer.parseInt(newValue);
                      inst.getFieldsValues().set(nr, newValueI);
                    } else if (type.equals("Float")) {
                      Float newValueF = Float.parseFloat(newValue);
                      inst.getFieldsValues().set(nr, df.format(newValueF));
                    } else
                      inst.getFieldsValues().set(nr, newValue);
                    //improspateaza timestamp-ul pentru instanta modificata
                    inst.setTimestamp(System.nanoTime());
                  }
                }
                nr++;
              }
            }
          }
          //sorteaza instantele dupa timestamp
          op.timestampSort(DB);

        } else if (tokens[0].equals("CLEANUP")) {
          long timestamp = Long.parseLong(tokens[2]);
          ArrayList<Instance> instancesToRemove = new ArrayList<>();
          for (Node n : DB.getNodes())
            for (Instance inst : n.getInstances())
              if (inst.getTimestamp() < timestamp)
                instancesToRemove.add(inst);
          op.delete(DB, instancesToRemove);

        } else if (tokens[0].equals("SNAPSHOTDB")) {
          op.snapshot(DB);
        }
      }
      reader.close();
      fileStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
