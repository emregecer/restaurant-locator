package de.boniel.apps.restaurantlocator.model;

import java.util.UUID;

/**
 * Projection class for locations with distance information
 */
public interface LocationWithDistance {

    UUID getId();

    String getName();

    Double getX();

    Double getY();

    double getDistance();
}
