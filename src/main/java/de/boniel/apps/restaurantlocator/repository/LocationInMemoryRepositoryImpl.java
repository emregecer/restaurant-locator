package de.boniel.apps.restaurantlocator.repository;

import de.boniel.apps.restaurantlocator.model.Location;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LocationInMemoryRepositoryImpl implements LocationRepository {

    private final Map<UUID, Location> storage = new ConcurrentHashMap<>();

    @Override
    public List<Location> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Location save(Location location) {
        if (location.getId() == null) {
            location = location.toBuilder()
                    .id(UUID.randomUUID())
                    .build();
        }
        storage.put(location.getId(), location);

        return location;
    }

    @Override
    public int count() {
        return storage.size();
    }
}
