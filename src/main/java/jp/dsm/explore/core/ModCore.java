package jp.dsm.dsm_explore_1_20_7;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModCore.MODID)
public class ModCore {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "dsm_explore_1_20_7";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ModCore(){
        LOGGER.info(" loaded : {}", MODID);
    }
}
