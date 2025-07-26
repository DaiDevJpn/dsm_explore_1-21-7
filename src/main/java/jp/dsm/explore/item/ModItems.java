package jp.dsm.explore.item;

import jp.dsm.explore.core.ModCore;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static jp.dsm.explore.item.RegisterHelper.ItemRegister.registerItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModCore.MODID);

    public static RegistryObject<Item> TEST_ITEM = registerItem("test_item", BaseItem::new);
    public static RegistryObject<Item> TEST_ITEM2 = registerItem("test_item2", TestItem::new);

    public static void register(BusGroup bus){
        ITEMS.register(bus);  // まず最初にバスに登録
    }
}

