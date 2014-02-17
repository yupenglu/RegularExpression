package automata;

/**
 * Edge class stores source node, destination node and the label set.
 * CAUTION: Though if one needs to attach a label set and a colabel set to
 * the same pair of source node and destination node, one needs to create
 * at least two Edge instances.
 */
public class Edge {
  private Integer src, dst;
  public Labels labels;

  public Edge(int src, int dst, Labels labels) {
    this.src = src;
    this.dst = dst;
    this.labels = labels;
  }

  public void labels(Labels abstractLabels) {
    this.labels = abstractLabels;
  }

  public Labels getLabels() {
    return labels;
  }

  public Integer getSrc() {
    return src;
  }

  public Integer getDst() {
    return dst;
  }
}
