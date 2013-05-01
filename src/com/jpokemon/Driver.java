package com.jpokemon;

import com.jpokemon.launcher.Launcher;
import com.jpokemon.mapeditor.MapEditWindow;

public class Driver {
  public static void main(String[] args) {
    if (args.length > 0) {
      if (args[0].equals("-mapedit")) {
        new MapEditWindow();
      }
    }
    else {
      new Launcher();
    }
  }
}