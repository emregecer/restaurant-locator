package de.boniel.apps.restaurantlocator.repository;

import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.model.LocationWithDistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query(
            value = """
                            SELECT id, name,
                            ST_X(coords) as x,
                            ST_Y(coords) as y,
                            ST_Distance(coords, ST_SetSRID(ST_MakePoint(:x, :y), 0)) as distance
                            FROM locations
                            WHERE ST_DWithin(coords, ST_SetSRID(ST_MakePoint(:x, :y), 0), radius)
                            ORDER BY distance ASC
                    """,
            nativeQuery = true
    )
    List<LocationWithDistance> findLocationsWithinRadiusWithDistance(@Param("x") double x,
                                                                     @Param("y") double y);
}
