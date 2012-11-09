package org.jpokemon.exception;

/**
 * An error in configuration which is breaking to the system
 */
public class ConfigurationException extends RuntimeException {
  public ConfigurationException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 1L;
}