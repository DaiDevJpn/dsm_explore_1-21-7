package jp.dsm.explore.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends Item implements IdCreator{

    public static final Properties PROP = new Properties()
            .tool(ToolMaterial.STONE, BlockTags.MINEABLE_WITH_PICKAXE, 5.0F, -3.0F, 1.5F)
            .overrideDescription("TEST DESC");

    public HammerItem(String id){
        super(IdCreator.validateId(PROP, id));
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState state) {
        Tool tool = itemStack.get(DataComponents.TOOL);
        float mSpeed = tool != null ? tool.getMiningSpeed(state) : 1.0F;
        return (1.0F - (float)itemStack.getDamageValue() / (float) itemStack.getMaxDamage()) * mSpeed;
    }
}
