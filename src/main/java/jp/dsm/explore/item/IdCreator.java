package jp.dsm.explore.item;

import jp.dsm.explore.core.ModCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface IdCreator {
    static Item.Properties validateId(Item.Properties properties, String id) {
        return properties.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModCore.MODID, id)));
    }
}
