package de.boniel.apps.restaurantlocator.repository;

import de.boniel.apps.restaurantlocator.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationInMemoryRepositoryTest {

    @InjectMocks
    private LocationInMemoryRepositoryImpl repository;

    @Test
    void shouldSaveLocationWithGivenId() {
        UUID id = UUID.randomUUID();
        Location location = sampleLocation(id);

        repository.save(location);

        Optional<Location> found = repository.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Test Place", found.get().getName());
        assertEquals(id, found.get().getId());
    }

    @Test
    void shouldGenerateIdWhenSavingLocationWithoutId() {
        Location location = sampleLocation(null);

        repository.save(location);

        List<Location> all = repository.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getId(), "Generated id should not be null");
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Optional<Location> result = repository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllSavedLocations() {
        Location loc1 = sampleLocation(UUID.randomUUID());
        Location loc2 = sampleLocation(UUID.randomUUID());

        repository.save(loc1);
        repository.save(loc2);

        List<Location> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(loc1));
        assertTrue(all.contains(loc2));
    }

    @Test
    void shouldUpdateLocationWithSameId() {
        UUID id = UUID.randomUUID();
        Location original = sampleLocation(id);
        repository.save(original);

        Location updated = original.toBuilder()
                .name("Updated Place")
                .build();

        repository.save(updated);

        Optional<Location> found = repository.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Updated Place", found.get().getName());
    }

    @Test
    void shouldReturnCountOfLocations() {
        Location loc1 = sampleLocation(UUID.randomUUID());
        Location loc2 = sampleLocation(UUID.randomUUID());

        repository.save(loc1);
        repository.save(loc2);

        int count = repository.count();

        assertEquals(2, count);
    }

    private Location sampleLocation(UUID id) {
        return Location.builder()
                .id(id)
                .name("Test Place")
                .type("Restaurant")
                .xCoordinate(10)
                .yCoordinate(20)
                .radius(5)
                .openingHours("10-22")
                .image("http://img")
                .build();
    }
}
