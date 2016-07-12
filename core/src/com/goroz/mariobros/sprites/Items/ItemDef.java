package com.goroz.mariobros.sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by HC Lim on 12/7/2016.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
