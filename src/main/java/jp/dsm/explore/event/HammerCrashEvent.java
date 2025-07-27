package jp.dsm.explore.event;

import jp.dsm.explore.core.ModCore;
import jp.dsm.explore.item.HammerItem;
import jp.dsm.explore.recipe.common.DropEntry;
import jp.dsm.explore.recipe.common.ReloadListenerRegister;
import jp.dsm.explore.recipe.crash_recipe.CrashRecipe;
import jp.dsm.explore.recipe.crash_recipe.CrashRecipeManager;
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

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModCore.MODID)
public class HammerCrashEvent {

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getPlayer();
        ItemStack held = player.getMainHandItem();

        if (!(held.getItem() instanceof HammerItem hammer)) return;

        BlockState state = event.getState();
        Block block = state.getBlock();

        CrashRecipeManager manager = ReloadListenerRegister.getManager();
        CrashRecipe recipe = manager.find(hammer, block);
        if (recipe == null) return;

        event.setResult(Result.DENY);
        level.removeBlock(event.getPos(), false);

        if (!player.isCreative() && held.isDamageableItem()) {
            held.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

        List<ItemEntity> drops = new ArrayList<>();

        ItemStack dropB = recipe.baseOutput().createStack(level.getRandom());
        drops.add(new ItemEntity(level, event.getPos().getCenter().x, event.getPos().getCenter().y, event.getPos().getCenter().z, dropB));

        for (DropEntry d : recipe.additionalOutputs()) {
            ItemStack dropA = d.createStack(level.getRandom());
            drops.add(new ItemEntity(level, event.getPos().getCenter().x, event.getPos().getCenter().y, event.getPos().getCenter().z, dropA));
        }

        drops.forEach(level::addFreshEntity);

    }
}
