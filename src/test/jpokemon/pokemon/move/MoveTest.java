package test.jpokemon.pokemon.move;

import java.util.HashMap;
import java.util.Map;

import jpkmn.Constants;
import jpkmn.game.pokemon.Type;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveInfo;
import jpkmn.game.pokemon.move.MoveStyle;
import junit.framework.TestCase;

import org.junit.Test;

public class MoveTest extends TestCase {
  private int number;
  private Move move;
  private MoveInfo answers;

  public void setUp() {
    number = 1 + (int) (Math.random() * Constants.MOVENUMBER);
    move = new Move(number);
    answers = MoveInfo.get(number);
  }

  @Test
  public void testNumber() {
    assertEquals(number, move.number());
  }

  public void testName() {
    assertEquals(answers.getName(), move.name());
  }

  public void testPpMax() {
    assertEquals(answers.getPp(), move.pp());
  }

  public void testType() {
    Type type = Type.valueOf(answers.getType());

    assertEquals(type, move.type());
  }

  public void testDisable() {
    assertTrue(move.enabled());
    move.enable(false);
    assertFalse(move.enabled());
  }

  public void testPpCount() {
    // Prevent random accuracy issues. #141 never misses
    number = 141;
    move = new Move(141);
    answers = MoveInfo.get(141);
    int random = (int) (Math.random() * answers.getPp());

    for (int i = 0; i < random; i++)
      move.use();

    assertEquals(random, answers.getPp() - move.pp());
  }

  public void testPpExhaustion() {
    // Prevent random accuracy issues. #141 never misses
    number = 141;
    move = new Move(141);
    answers = MoveInfo.get(141);

    assertTrue(move.enabled());

    for (int i = answers.getPp(); i > 0; i--)
      move.use();

    assertFalse(move.enabled());
  }

  public void testRestore() {
    // Prevent random accuracy issues. #141 never misses
    number = 141;
    move = new Move(141);
    answers = MoveInfo.get(141);

    assertTrue(move.enabled());

    for (int i = answers.getPp(); i > 0; i--)
      move.use();

    assertFalse(move.enabled());

    move.restore();

    assertEquals(answers.getPp(), move.pp());
  }

  public void testReps() {
    MoveStyle style = MoveStyle.valueOf(answers.getStyle());

    while (style != MoveStyle.REPEAT) {
      number = 1 + (int) (Math.random() * Constants.MOVENUMBER);
      move = new Move(number);
      answers = MoveInfo.get(number);
      style = MoveStyle.valueOf(answers.getStyle());
    }

    assertTrue(move.reps() != 1);

    while (style == MoveStyle.REPEAT) {
      number = 1 + (int) (Math.random() * Constants.MOVENUMBER);
      move = new Move(number);
      answers = MoveInfo.get(number);
      style = MoveStyle.valueOf(answers.getStyle());
    }

    assertTrue(move.reps() == 1);
  }

  public void testSTAB() {
    Type type = Type.valueOf(answers.getType());

    assertEquals(1.5, move.STAB(type, null));

    while (type == Type.valueOf(answers.getType()))
      type = Type.valueOf((int) (Math.random() * Type.values().length));

    assertEquals(1.0, move.STAB(type, null));
  }

  /*
   * NOTE: This test makes no assertions. Instead, it will print the
   * distribution of the calls made to reps()
   */
  public void testRepsDistribution() {
    int sampleSize = 200, result;
    Map<Integer, Integer> results = new HashMap<Integer, Integer>();

    MoveStyle style = MoveStyle.valueOf(answers.getStyle());

    while (style != MoveStyle.REPEAT) {
      number = 1 + (int) (Math.random() * Constants.MOVENUMBER);
      move = new Move(number);
      answers = MoveInfo.get(number);
      style = MoveStyle.valueOf(answers.getStyle());
    }

    for (int i = 0; i < sampleSize; i++) {
      result = move.reps();

      if (results.get(result) == null)
        results.put(result, 0);
      results.put(result, results.get(result) + 1);
    }

    System.out.println("\nDistribution of Reps");
    for (Map.Entry<Integer, Integer> entry : results.entrySet())
      System.out.println(entry.getKey() + ": " + entry.getValue());
    System.out.println();
  }
}