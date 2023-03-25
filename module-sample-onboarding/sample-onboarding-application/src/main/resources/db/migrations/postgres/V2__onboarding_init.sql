CREATE TABLE onboarding.templates (
  id UUID NOT NULL,
  name VARCHAR(256) NOT NULL,
  active BOOLEAN NOT NULL,
  archived BOOLEAN NOT NULL,
  _entity_version BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT uc_name UNIQUE (name)
);


CREATE TABLE onboarding.template_documents (
  id UUID NOT NULL,
  template_id UUID NOT NULL,
  name VARCHAR(256) NOT NULL,
  _entity_version BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES onboarding.templates (id),
  CONSTRAINT uc_name1 UNIQUE (name)
);


CREATE TABLE onboarding.operations (
  id UUID NOT NULL,
  user_id UUID NOT NULL,
  template_id UUID NOT NULL,
  progress_status VARCHAR(32),
  email VARCHAR(256),
  documents JSONB,
  _entity_version BIGINT,
  PRIMARY KEY (id)
);


CREATE TABLE onboarding.operationdocuments (
  id UUID NOT NULL,
  operation_id UUID NOT NULL,
  name VARCHAR(256),
  status VARCHAR(256),
  _entity_version BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT fk_operation FOREIGN KEY (operation_id) REFERENCES onboarding.operations (id)
);
