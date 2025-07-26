package jp.dsm.explore.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class RegisterHelper {
    public static class ItemRegister{
        public static RegistryObject<Item> registerItem(String id, ItemFactory factory){
            return ModItems.ITEMS.register(id, () -> factory.create(id));
        }

        @FunctionalInterface
        public interface ItemFactory{
            Item create(String id);
        }
    }
}
