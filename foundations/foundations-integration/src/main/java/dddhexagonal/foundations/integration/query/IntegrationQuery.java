package dddhexagonal.foundations.integration.query;

import lombok.ToString;

import dddhexagonal.foundations.integration.message.IntegrationMessage;

@ToString(callSuper = true)
public abstract class IntegrationQuery<Q extends IntegrationQueryResult> extends IntegrationMessage {

}
