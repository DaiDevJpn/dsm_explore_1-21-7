package jp.dsm.explore.block;

import jp.dsm.explore.core.ModCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static jp.dsm.explore.util.RegisterHelper.BlockRegister.registerBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModCore.MODID);

    public static RegistryObject<Block> TEST_BLOCK = registerBlock("test_block", (id) -> new Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ModCore.MODID,id)))));

    public static void register(BusGroup bus){
        BLOCKS.register(bus);
    }
}
