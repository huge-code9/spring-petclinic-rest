CREATE TABLE pets (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(30) NOT NULL,
    birth_date DATE NOT NULL,
    owner_id VARCHAR(36) NOT NULL,
    registered_at DATE NOT NULL,
    CONSTRAINT fk_pets_owner FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

CREATE INDEX idx_pets_owner_id ON pets(owner_id);
CREATE INDEX idx_pets_type ON pets(type);
