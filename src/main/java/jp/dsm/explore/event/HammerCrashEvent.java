package jp.dsm.explore.event;

import jp.dsm.explore.core.ModCore;
import jp.dsm.explore.item.HammerItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModCore.MODID)
public class HammerCrashEvent {

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event){
        Level level = (Level) event.getLevel();
        if (level.isClientSide())return;

        Player player = event.getPlayer();
        ItemStack held = player.getMainHandItem();

        if (!(held.getItem() instanceof HammerItem))return;

        ModCore.LOGGER.info("HCE CALLED");

        //↓--TestCode--↓

        BlockState state = event.getState();
        Block block =  state.getBlock();

        if (!(block == Blocks.COBBLESTONE || block == Blocks.STONE))return;

        event.setResult(Result.DENY);
        level.removeBlock(event.getPos(), false);

        if (!player.isCreative() && held.isDamageableItem()){
            held.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

        ItemStack drop = new ItemStack(Blocks.GRAVEL);
        ItemEntity dropE = new ItemEntity(level, event.getPos().getCenter().x, event.getPos().getCenter().y, event.getPos().getCenter().z, drop);

        level.addFreshEntity(dropE);
    }
}
