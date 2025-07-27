package de.boniel.apps.restaurantlocator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Entity
@Table(name = "locations")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(length = 500)
    private String image;

    // PostGIS Point type with SRID 0 Euclidean coordinates)
    @Column(columnDefinition = "geometry(Point, 0)", nullable = false)
    private Point coords;

    @Column(nullable = false)
    private double radius;

    @Column(length = 50)
    private String openingHours;
}
