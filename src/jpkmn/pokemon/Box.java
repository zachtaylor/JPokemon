package jpkmn.pokemon;

import java.util.ArrayList;
import java.util.Iterator;

public class Box implements Iterable<Pokemon> {
  public Box() {
    storage = new ArrayList<Pokemon>();
  }
  
  public void add(Pokemon p) {
    storage.add(p);
  }
  
  public void remove(Pokemon p) {
    storage.remove(p);
  }
  
  @Override
  public Iterator<Pokemon> iterator() {
    return storage.iterator();
  }
  
  private ArrayList<Pokemon> storage;
}
