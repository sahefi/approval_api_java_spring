CREATE TABLE IF NOT EXISTS "roles"   
(
    id       UUID,
    name     VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "users"   
(
    id       UUID,
    name     VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id  UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE(username),
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES roles (id)
);


CREATE TYPE vehicle_type AS ENUM ('stuff', 'people');

CREATE TABLE IF NOT EXISTS "vehicles"   
(
    id               UUID,
    name             VARCHAR(100) NOT NULL,
    type             vehicle_type NOT NULL,
    fuel_consumption DECIMAL(10, 2) NOT NULL,
    service_schedule TIMESTAMP NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);




CREATE TABLE IF NOT EXISTS "bookings"   
(
    id       UUID,
    driver     VARCHAR(100) NOT NULL,
    applicant   VARCHAR(100) NOT NULL,
    vehicle_id UUID,
    approver_id UUID,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    need_approval BOOLEAN NOT NULL DEFAULT TRUE,
    start_book TIMESTAMP NOT NULL,
    end_book TIMESTAMP NOT NULL,
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_vehicle_booking FOREIGN KEY (vehicle_id) REFERENCES "vehicles" (id),
    CONSTRAINT fk_user_booking FOREIGN KEY (approver_id) REFERENCES "users" (id)
);