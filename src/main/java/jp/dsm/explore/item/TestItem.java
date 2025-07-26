package jp.dsm.explore.item;

import net.minecraft.world.food.FoodProperties;

public class TestItem extends BaseItem{

    public static final Properties PROP = new Properties().food(new FoodProperties(1, 1, true));

    public TestItem(String id) {
        super(PROP, id);
    }
}
