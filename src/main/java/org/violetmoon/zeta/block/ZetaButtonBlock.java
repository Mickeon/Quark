package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ButtonBlock;
import org.violetmoon.zeta.module.ZetaModule;

/**
 * @author WireSegal
 * Created at 9:14 PM on 10/8/19.
 */
public abstract class ZetaButtonBlock extends ButtonBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaButtonBlock(BlockSetType setType, int ticksToStayPressed, boolean arrowsCanPress, String regname, ZetaModule module, String creativeTab, Properties properties) {
		super(properties, setType, ticksToStayPressed, arrowsCanPress);
		this.module = module;

		module.zeta.registry.registerBlock(this, regname, true);
		module.zeta.registry.setCreativeTab(this, creativeTab);
	}

	@NotNull
	@Override
	protected abstract SoundEvent getSound(boolean powered);

	@Override
	public ZetaButtonBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

}