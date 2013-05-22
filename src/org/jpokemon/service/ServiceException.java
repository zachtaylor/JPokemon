package org.jpokemon.service;

public class ServiceException extends Exception {
  public ServiceException(LoadException e) {
    super(e);
  }

  public ServiceException(String message) {
    _message = message;
  }

  public String getMessage() {
    return _message;
  }

  private String _message;
  private static final long serialVersionUID = 1L;
}