package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCActionRequirement implements JPokemonConstants {
  private int area, number, set, reqset, type, data;

  public static List<NPCActionRequirement> get(int area, int number, int set) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionRequirement.class).where("area").eq(area).and("number").eq(number).and("set").eq(set).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<NPCActionRequirement>();
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getSet() {return set;} public void setSet(int s) {set = s;}
  public int getReqset() {return reqset;} public void setReqset(int r) {reqset = r;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  //@format
}