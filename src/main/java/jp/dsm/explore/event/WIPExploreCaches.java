package jp.dsm.explore.event;

import jp.dsm.explore.core.ModCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;

//@Mod.EventBusSubscriber(modid = ModCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WIPExploreCaches {
    private static final String TAG = "[Explore:Cache]";
    private static final String NBT_LAST = "explore:lastCache";
    private static final long  COOLDOWN_TICKS = 20 * 30; // 30秒
    private static final double SPAWN_CHANCE = 1;     // 35%

    //@SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        //if (e.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
        if (e.player.level().isClientSide()) return;
        if (!(e.player instanceof ServerPlayer sp)) return;

        ServerLevel level = sp.level();
        var nbt = sp.getPersistentData();
        long now = level.getGameTime();
        long last = nbt.getLong(NBT_LAST).orElse(0L);
        if (now - last < COOLDOWN_TICKS) return;
        nbt.putLong(NBT_LAST, now); // まず刻む（多重発火防止）

        var rand = level.random;
        if (rand.nextDouble() >= SPAWN_CHANCE) return;

        // プレイヤー周辺の地表に“少し遠め”で
        int dist = 80 + rand.nextInt(41); // 80..120
        double ang = rand.nextDouble() * Math.PI * 2;
        int x = Mth.floor(sp.getX() + Math.cos(ang) * dist);
        int z = Mth.floor(sp.getZ() + Math.sin(ang) * dist);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        var pos = new BlockPos(x, y, z);

        // 水上や空中は避ける
        if (!level.getFluidState(pos).isEmpty()) return;
        if (!level.getBlockState(pos.below()).isSolid()) return;

        // 小さな“目印”＋バレル
        placeCache(level, pos);
        sp.sendSystemMessage(Component.literal("地平線の向こうに妙な積み荷が見える…").withStyle(net.minecraft.ChatFormatting.GOLD));
        ModCore.LOGGER.info("{} placed cache at {}", TAG, pos);
    }

    private static void placeCache(ServerLevel level, BlockPos pos) {
        // 目印（十字の丸石）
        for (var off : new BlockPos[]{
                pos.north(), pos.south(), pos.east(), pos.west()}) {
            level.setBlock(off, Blocks.COBBLESTONE.defaultBlockState(), 2);
        }
        // バレル本体
        level.setBlock(pos, Blocks.BARREL.defaultBlockState(), 3);

        // LootTableを紐づけ（data/<modid>/loot_tables/chests/explore_cache.json を用意）

        var be = level.getBlockEntity(pos);
        if (be instanceof RandomizableContainerBlockEntity chest) {
            chest.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ModCore.MODID, "chests/explore_cache")),
                               level.random.nextLong());
        }
        // ちょっとした演出
        level.levelEvent(2005, pos, 0); // ボーン!のパーティクル
    }
}
