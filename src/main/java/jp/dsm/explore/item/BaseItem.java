package jp.dsm.explore.item;

import jp.dsm.explore.core.ModCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class BaseItem extends Item {
    public BaseItem(Properties properties, String id) {
        super(properties.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModCore.MODID, id))));
    }

    public BaseItem(String id){
        this(createProp(), id);
    }

    protected static Properties createProp(){
        return new Properties();
    }
}
