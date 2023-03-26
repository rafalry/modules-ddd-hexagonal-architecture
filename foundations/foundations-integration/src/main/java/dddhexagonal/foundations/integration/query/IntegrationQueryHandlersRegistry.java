package dddhexagonal.foundations.integration.query;

import java.util.function.Function;

public interface IntegrationQueryHandlersRegistry {

  <C extends IntegrationQuery<R>, R extends IntegrationQueryResult> void registerQueryHandler(
      Class<C> queryType,
      Function<C, R> handler);

}
