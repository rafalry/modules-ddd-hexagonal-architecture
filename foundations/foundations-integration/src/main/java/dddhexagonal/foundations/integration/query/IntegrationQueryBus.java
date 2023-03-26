package dddhexagonal.foundations.integration.query;

import java.util.concurrent.CompletableFuture;

public interface IntegrationQueryBus {

  <R extends IntegrationQueryResult> CompletableFuture<R> queryAsync(IntegrationQuery<R> query);
}
