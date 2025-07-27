-- Enable PostGIS extension (needed for geometry & spatial indexes)
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE locations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    image VARCHAR(500),
    coords geometry(Point, 0) NOT NULL,
    radius DOUBLE PRECISION NOT NULL,
    opening_hours VARCHAR(50),
);

CREATE INDEX idx_locations_coords
    ON locations USING GIST (coords);