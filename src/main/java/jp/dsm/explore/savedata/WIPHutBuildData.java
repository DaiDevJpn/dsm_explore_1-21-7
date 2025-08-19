package jp.dsm.explore.savedata;

import com.mojang.serialization.Codec;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class WIPHutBuildData extends SavedData {
    private boolean built;
    public static final String DATA_NAME = "hut_built_data";

    public static final Codec<WIPHutBuildData> CODEC = Codec.BOOL.xmap(
            WIPHutBuildData::new,
            WIPHutBuildData::isBuilt
    );

    public static final SavedDataType<WIPHutBuildData> TYPE = new SavedDataType<>(
            DATA_NAME,
            ctx -> new WIPHutBuildData(false),
            ctx -> CODEC,
            DataFixTypes.LEVEL
    );

    public WIPHutBuildData(boolean flag){
        this.built = flag;
    }

    public boolean isBuilt(){return built;}
    public static WIPHutBuildData setBuilt(){return new WIPHutBuildData(true);}
}
