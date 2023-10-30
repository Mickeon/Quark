package vazkii.quark.content.tweaks.module;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.util.Hint;
import vazkii.quark.content.tweaks.recipe.ElytraDuplicationRecipe;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;

@LoadModule(category = "tweaks", hasSubscriptions = true)
public class DragonScalesModule extends ZetaModule {

	@Hint public static Item dragon_scale;

	@LoadEvent
	public final void register(ZRegister event) {
		event.getRegistry().register(ElytraDuplicationRecipe.SERIALIZER, "elytra_duplication", Registry.RECIPE_SERIALIZER_REGISTRY);

		dragon_scale = new QuarkItem("dragon_scale", this, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
	}

	@SubscribeEvent
	public void onEntityTick(LivingTickEvent event) {
		if(event.getEntity() instanceof EnderDragon dragon && !event.getEntity().getCommandSenderWorld().isClientSide) {
			if(dragon.getDragonFight() != null && dragon.getDragonFight().hasPreviouslyKilledDragon() && dragon.dragonDeathTime == 100) {
				Vec3 pos = dragon.position();
				ItemEntity item = new ItemEntity(dragon.level, pos.x, pos.y, pos.z, new ItemStack(dragon_scale, 1));
				dragon.level.addFreshEntity(item);
			}
		}
	}

}
