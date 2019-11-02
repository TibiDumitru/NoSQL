import java.util.Comparator;

class TimestampComparator implements Comparator<Instance> {
  @Override
  //compara doua instante dupa timestamp
  public int compare(Instance i1, Instance i2) {
    return Long.compare(i1.getTimestamp(), i2.getTimestamp());
  }
}
