package net.zekk051.securecrops.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(HoeItem.class)
public class HoeItemMixin {
    @Inject(method = "createTillAction", at = @At("HEAD"), cancellable = true)
    private static void createTillAction(BlockState result, CallbackInfoReturnable<Consumer<ItemUsageContext>> cir) {
        if (result.isOf(Blocks.FARMLAND)) {
            BlockState modifiedResult = result.with(FarmlandBlock.MOISTURE, 7);
            cir.setReturnValue((context) -> {
                (context).getWorld().setBlockState(context.getBlockPos(), modifiedResult, 11);
                (context).getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), modifiedResult));
            });
            cir.cancel();
        }
    }
}
