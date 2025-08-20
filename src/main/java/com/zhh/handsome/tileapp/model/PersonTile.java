package com.zhh.handsome.tileapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "person_tiles")
@Data
@IdClass(PersonTileId.class)
public class PersonTile {
    @Id
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "tile_id")
    private Tile tile;
}

class PersonTileId implements Serializable {
    private Long person;
    private Long tile;

    // 必须实现equals和hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonTileId that = (PersonTileId) o;
        return person.equals(that.person) && tile.equals(that.tile);
    }

    @Override
    public int hashCode() {
        return 31 * person.hashCode() + tile.hashCode();
    }
}