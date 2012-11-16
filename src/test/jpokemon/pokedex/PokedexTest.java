package test.jpokemon.pokedex;

import java.util.HashMap;
import java.util.Map;

import jpkmn.exceptions.LoadException;
import junit.framework.TestCase;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokedex.PokedexStatus;

public class PokedexTest extends TestCase implements JPokemonConstants {
  public static int power = 25, defaultSize = POKEMONNUMBER;

  public static Pokedex dex;

  protected void setUp() {
    dex = new Pokedex();
  }

  public void testSaw() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize + 1);

      dex.saw(cur);

      assertTrue(PokedexStatus.SAW == dex.status(cur));
    }
  }

  public void testOwn() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize + 1);

      dex.own(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testOwnOverridesSaw() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize + 1);

      dex.saw(cur);
      dex.own(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testSawDoesNotOverrideOwn() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize + 1);

      dex.own(cur);
      dex.saw(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testBoundsLower() {
    try {
      dex.status(0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testBoundsUpper() {
    try {
      dex.status(defaultSize + 1);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testSaveEmpty() {
    String expected = "DEX: \n";

    assertEquals(expected, dex.save());
  }

  public void testSaveSome() {
    Map<Integer, PokedexStatus> data = new HashMap<Integer, PokedexStatus>();

    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize);
      dex.saw(cur + 1);
      data.put(cur, PokedexStatus.SAW);
    }

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * defaultSize);
      dex.own(cur + 1);
      data.put(cur, PokedexStatus.OWN);
    }

    String expected = "DEX: ";
    for (int i = 0; i < defaultSize; i++) {
      if (data.get(i) != null)
        expected += i + "-" + data.get(i).ordinal() + " ";
    }
    expected += "\n";

    assertEquals(expected, dex.save());
  }

  public void testLoad() {
    Map<Integer, PokedexStatus> data = new HashMap<Integer, PokedexStatus>();

    for (int i = 0; i < power; i++)
      data.put((int) (Math.random() * defaultSize), PokedexStatus.SAW);

    for (int i = 0; i < power; i++)
      data.put((int) (Math.random() * defaultSize), PokedexStatus.OWN);

    String loadData = "DEX: ";

    for (int i = 0; i < defaultSize; i++) {
      if (data.get(i) != null)
        loadData += i + "-" + data.get(i).ordinal() + " ";
    }

    loadData += "\n";

    try {
      dex.load(loadData);
    } catch (LoadException e) {
      assertTrue(e.getMessage(), false);
      e.printStackTrace();
    }

    for (int i = 0; i < defaultSize; i++)
      if (data.get(i) != null)
        assertEquals(data.get(i), dex.status(i + 1));
  }

  public void testLoadNonsense() {
    try {
      dex.load("foo bar baz\n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }
  }

  public void testLoadRangeError() {
    try {
      dex.load("DEX: 151-1 \n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }
  }
}