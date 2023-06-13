package vazkii.quark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.quark.content.automation.module.PistonsMoveTileEntitiesModule;
import vazkii.quark.content.experimental.module.GameNerfsModule;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private static boolean tick(Level world, BlockPos pos, BlockState newState, int flags) {
		return PistonsMoveTileEntitiesModule.setPistonBlock(world, pos, newState, flags);
	}

	@Redirect(method = "finalTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean finalTick(Level world, BlockPos pos, BlockState newState, int flags) {
		return PistonsMoveTileEntitiesModule.setPistonBlock(world, pos, newState, flags);
	}

	@ModifyConstant(method = "tick", constant = @Constant(intValue = 84))
	private static int forceNotifyBlockUpdate(int flag) {
		return GameNerfsModule.stopPistonPhysicsExploits() ? (flag | 2) : flag; // paper impl comment: Paper - force notify (flag 2), it's possible the set type by the piston block (which doesn't notify) set this block to air
	}
}
