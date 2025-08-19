package jp.dsm.explore.core;

import com.mojang.logging.LogUtils;
import jp.dsm.explore.block.ModBlocks;
import jp.dsm.explore.item.ModItems;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModCore.MODID)
public class ModCore {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "dsmexplore";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public ModCore(FMLJavaModLoadingContext ctx){
        BusGroup bus = ctx.getModBusGroup();
        LOGGER.info("[DSM]loaded : {}", MODID);
        ModItems.register(bus);
        ModBlocks.register(bus);
    }
}
