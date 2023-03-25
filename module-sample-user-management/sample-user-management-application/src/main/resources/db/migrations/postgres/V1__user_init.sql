CREATE TABLE user_management.users (
  id UUID NOT NULL,
  active BOOLEAN,
  email VARCHAR(256),
  _entity_version BIGINT,
  PRIMARY KEY (id)
);

CREATE TABLE user_management.projection_onboarding_documents (
  user_id UUID NOT NULL,
  document_id UUID NOT NULL,
  document_name VARCHAR(256),
  PRIMARY KEY (user_id)
);
