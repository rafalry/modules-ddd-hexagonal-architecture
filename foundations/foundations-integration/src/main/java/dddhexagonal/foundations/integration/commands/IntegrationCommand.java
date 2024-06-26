package dddhexagonal.foundations.integration.commands;

import lombok.ToString;

import dddhexagonal.foundations.integration.message.IntegrationMessage;

@ToString(callSuper = true)
public abstract class IntegrationCommand<R extends IntegrationCommandResult> extends IntegrationMessage {

}
