package jp.dsm.explore.util;

import jp.dsm.explore.block.ModBlocks;
import jp.dsm.explore.core.ModCore;
import jp.dsm.explore.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class RegisterHelper {
    public static class ItemRegister{
        public static RegistryObject<Item> registerItem(String id, ItemFactory factory){
            return ModItems.ITEMS.register(id, () -> factory.create(id));
        }

        public static void registerBlockItem(String id, RegistryObject<Block> block){
            ModItems.ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModCore.MODID, id)))));
        }

        @FunctionalInterface
        public interface ItemFactory{
            Item create(String id);
        }
    }

    public static class BlockRegister{
        public static RegistryObject<Block> registerBlock(String id, BlockFactory factory){
            final var block = ModBlocks.BLOCKS.register(id, () -> factory.create(id));
            ItemRegister.registerBlockItem(id, block);
            return block;
        }

        @FunctionalInterface
        public interface BlockFactory{
            Block create(String id);
        }
    }
}
