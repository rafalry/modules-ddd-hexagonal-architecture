/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.events;

public interface IntegrationEventBus {

  void publishAsync(IntegrationEvent event);
}
