package dddhexagonal.foundations.domain.rule;

import java.util.Map;

import static java.util.Collections.emptyMap;

public interface BusinessRule {

  boolean isRespected();

  default Map<String, String> getDetails() {
    return emptyMap();
  }
}
