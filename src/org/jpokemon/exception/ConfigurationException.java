package org.jpokemon.exception;

import com.kremerk.Sqlite.DataConnectionException;

/**
 * An error in configuration which is breaking to the system
 */
public class ConfigurationException extends RuntimeException {
  public ConfigurationException(String message) {
    super(message);
  }
  
  public ConfigurationException(DataConnectionException e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;
}