package jp.dsm.explore.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TestItem extends BaseItem{

    public static final Properties PROP = new Properties().food(new FoodProperties(1, 1, true));

    public TestItem(String id) {
        super(PROP, id);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, @Nullable EquipmentSlot slot, int slotIndex) {
        super.inventoryTick(stack, level, entity, slot, slotIndex);
        if (!(entity instanceof Player player))return;
        if (slot == EquipmentSlot.MAINHAND){
            // 1) 泳ぎポーズ
            player.setPose(Pose.SWIMMING);
            player.setSwimming(true);

            // 2) 入力取得
            float forward = player.zza;   // W/S
            float strafe  = player.xxa;   // A/D
            boolean up    = player.isJumping();          // Space
            boolean down  = player.isShiftKeyDown(); // Shift

            // 3) ベース速度
            double accel   = 0.12; // 水中より少し速い
            double gravity = 0.05; // 落下抑制

            // 4) 現在速度
            Vec3 motion = player.getDeltaMovement();
            Vec3 look = player.getLookAngle().normalize();

            // 5) 前後左右入力で加速
            if (forward != 0 || strafe != 0) {

                // 横移動を含めるために Y=0 の水平成分
                Vec3 dir = new Vec3(look.x, 0, look.z).normalize()
                        .scale(forward)
                        .add(new Vec3(-look.z, 0, look.x).normalize().scale(strafe));
                motion = motion.add(dir.normalize().scale(accel));
            }

            // 6) 上下方向への推進
            double brake = 0.2; // 視線による上昇下降の強さ
            Vec3 vUpDown = new Vec3(0, look.y, 0).normalize().scale(forward * brake);

            // 明示的な上下操作
            if (up)   vUpDown = vUpDown.add(0,  accel, 0);
            if (down) vUpDown = vUpDown.add(0, -accel, 0);

            motion = motion.add(vUpDown);


            // 7) 疑似浮力：落下をゆるく
            if (motion.y < 0) motion = motion.add(0, gravity, 0);

            // 8) 適度な減速（空中水の抵抗）
            motion = motion.scale(0.90);

            player.setDeltaMovement(motion);
            player.resetFallDistance();
        }
    }
}
