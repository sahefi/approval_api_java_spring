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



CREATE TABLE IF NOT EXISTS "vehicles"   
(
    id               UUID,
    name             VARCHAR(100) NOT NULL,
    fuel_consumption DECIMAL(10, 2) NOT NULL,
    service_schedule TIMESTAMP NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('BOOKED', 'AVAILABLE')),
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
    start_book DATE NOT NULL,
    end_book DATE NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('PENDING', 'APPROVED','REJECTED')),
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_vehicle_booking FOREIGN KEY (vehicle_id) REFERENCES "vehicles" (id),
    CONSTRAINT fk_user_booking FOREIGN KEY (approver_id) REFERENCES "users" (id)
);