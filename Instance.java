import java.util.ArrayList;

class Instance {
  private String name;
  private long timestamp;
  //lista cu valorile atributelor
  //pot fi de tip Integer, Float sau String
  private ArrayList<Object> fieldsValues;
  //lista cu numele atributelor
  private ArrayList<String> names;

  public Instance(String name) {
    this.name = name;
    fieldsValues = new ArrayList<>();
    names = new ArrayList<>();
  }

  public ArrayList<Object> getFieldsValues() {
    return fieldsValues;
  }

  public ArrayList<String> getNames() {
    return names;
  }

  public String getName() {
    return name;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  //extrage cheia primara
  public String getPrimaryKey() {
    return String.valueOf(fieldsValues.get(0));
  }
}
