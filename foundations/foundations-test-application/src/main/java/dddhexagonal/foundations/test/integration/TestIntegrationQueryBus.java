package dddhexagonal.foundations.test.integration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dddhexagonal.foundations.integration.query.IntegrationQuery;
import dddhexagonal.foundations.integration.query.IntegrationQueryBus;
import dddhexagonal.foundations.integration.query.IntegrationQueryResult;

public class TestIntegrationQueryBus implements IntegrationQueryBus {

  @Getter
  private List<IntegrationQuery> queries = new ArrayList<>();


  @Override
  public <R extends IntegrationQueryResult> CompletableFuture<R> queryAsync(
      IntegrationQuery<R> query) {
    queries.add(query);
    return null;
  }


  public void clean() {
    queries = new ArrayList<>();
  }

  public boolean isClean() {
    return queries.isEmpty();
  }
}
