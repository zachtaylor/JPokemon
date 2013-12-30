package org.jpokemon.interaction;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.interaction.requirements.EventRequirementFactory;
import org.jpokemon.interaction.requirements.PokedexRequirementFactory;

public class RequirementFactoryRegistry {
  private static Map<String, RequirementFactory> factories = new HashMap<String, RequirementFactory>();

  static {
    registerRequirementFactory("event", new EventRequirementFactory());
    registerRequirementFactory("pokedex", new PokedexRequirementFactory());
  }

  private RequirementFactoryRegistry() {
  }

  public static void registerRequirementFactory(String requirement, RequirementFactory builder) {
    factories.put(requirement, builder);
  }

  public static Requirement get(String action, String options) {
    RequirementFactory requirementFactory = factories.get(action);

    if (requirementFactory != null) { return requirementFactory.buildRequirement(options); }

    return null;
  }
}