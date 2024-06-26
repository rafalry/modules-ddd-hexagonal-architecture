package dddhexagonal.foundations.domain.repository;

import java.io.Serializable;

public class InvalidAggregateIdException extends RuntimeException {

  public InvalidAggregateIdException(Serializable id) {
    super("Aggregate with id " + id + " not found in the database.");
  }

}
