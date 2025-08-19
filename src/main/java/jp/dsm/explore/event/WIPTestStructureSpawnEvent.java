package jp.dsm.explore.event;

import jp.dsm.explore.core.ModCore;
import jp.dsm.explore.savedata.WIPHutBuildData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Optional;

//@Mod.EventBusSubscriber(modid = ModCore.MODID)
public class WIPTestStructureSpawnEvent {

    //@SubscribeEvent
    public static void onWorldLoad(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity().level().isClientSide())return;
        ServerLevel level = (ServerLevel) event.getEntity().level();

        var dataStorage = level.getDataStorage();
        WIPHutBuildData data = dataStorage.computeIfAbsent(WIPHutBuildData.TYPE);

        level.getStructureManager().listTemplates().forEach(id -> {
            if (!id.toString().startsWith("minecraft:"))ModCore.LOGGER.info("Available template: {}", id);
        });

        if (!data.isBuilt()) {
            BlockPos pos = event.getEntity().getOnPos();
            //placeStructure(level, pos, ResourceLocation.fromNamespaceAndPath(ModCore.MODID, "test_hut"));
            //dataStorage.set(WIPHutBuildData.TYPE, WIPHutBuildData.setBuilt());
        }
    }

    private static void buildTestHut(ServerLevel level, BlockPos center) {
        for (int x = -2; x <= 2; x++){
            for (int z = -2; z <= 2; z++){
                for (int y = 0; y <= 4; y++){

                    BlockPos pos = center.offset(x, y, z);
                    boolean isWall = (x == -2 || x == 2 || z == -2 || z == 2);
                    if (y == 0){
                        level.setBlock(pos, Blocks.PALE_OAK_PLANKS.defaultBlockState(), 3);
                    }else if (y == 4 || isWall){
                        level.setBlock(pos, Blocks.STRIPPED_PALE_OAK_LOG.defaultBlockState(), 3);
                    } else if (y == 3 && x == 0 && z == 0) {
                        level.setBlock(pos, Blocks.LANTERN.defaultBlockState(), 3);
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    public static void placeStructure(ServerLevel level, BlockPos pos, ResourceLocation structureId) {
        StructureTemplateManager manager = level.getStructureManager();
        Optional<StructureTemplate> template = manager.get(structureId);

        if (template.isEmpty()) {
            ModCore.LOGGER.warn("Structure not found: {}", structureId);
            return;
        }

        Vec3i size = template.get().getSize();
        BlockPos adjust = pos.offset(
                -size.getX() / 2,
                0,
                -size.getZ() / 2
        );

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE)
                .setIgnoreEntities(false);

        template.get().placeInWorld(level, adjust, adjust, settings, level.random, 2);
    }
}
