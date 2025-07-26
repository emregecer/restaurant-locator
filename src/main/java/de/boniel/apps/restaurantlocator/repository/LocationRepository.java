package de.boniel.apps.restaurantlocator.repository;

import de.boniel.apps.restaurantlocator.model.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {

    List<Location> findAll();

    Optional<Location> findById(UUID id);

    Location save(Location location);

    int count();

}
