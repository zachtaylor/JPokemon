package org.jpokemon.interaction;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.interaction.actions.*;

public class ActionFactoryRegistry {
  private static Map<String, ActionFactory> factories = new HashMap<String, ActionFactory>();

  static {
    registerActionFactory("event", new EventActionFactory());
    registerActionFactory("heal", new HealActionFactory());
    registerActionFactory("item", new ItemActionFactory());
    registerActionFactory("store", new StoreActionFactory());
    registerActionFactory("upgrade", new UpgradeActionFactory());
    registerActionFactory("map", new MapActionFactory());
    registerActionFactory("grass", new GrassActionFactory());
  }

  private ActionFactoryRegistry() {
  }

  public static void registerActionFactory(String action, ActionFactory factory) {
    factories.put(action, factory);
  }

  public static Action get(String action, String options) {
    ActionFactory actionFactory = factories.get(action);

    if (actionFactory != null) {
      return actionFactory.buildAction(options);
    }

    return null;
  }
}