package vazkii.zetaimplforge.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.ForgeConfigSpec;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.IZetaConfigInternals;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;

public class ForgeBackedConfig implements IZetaConfigInternals {
	private final Map<ValueDefinition<?>, ForgeConfigSpec.ConfigValue<?>> definitionsToValues = new HashMap<>();

	public ForgeBackedConfig(SectionDefinition rootSection, ForgeConfigSpec.Builder forgeBuilder) {
		walkSection(rootSection, forgeBuilder, true);
	}

	private void walkSection(SectionDefinition sect, ForgeConfigSpec.Builder builder, boolean root) {
		if(!root) {
			builder.comment(sect.commentToArray());
			builder.push(sect.name);
		}

		for(ValueDefinition<?> value : sect.getValues())
			addValue(value, builder);

		for(SectionDefinition subsection : sect.getSubsections())
			walkSection(subsection, builder, false);

		if(!root)
			builder.pop();
	}

	private <T> void addValue(ValueDefinition<T> val, ForgeConfigSpec.Builder builder) {
		builder.comment(val.commentToArray());

		ForgeConfigSpec.ConfigValue<?> forge;
		if(val.defaultValue instanceof List<?> list)
			forge = builder.defineList(val.name, list, val::validate);
		else
			forge = builder.define(List.of(val.name), () -> val.defaultValue, val::validate, val.defaultValue.getClass()); //forge is weird

		definitionsToValues.put(val, forge);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(ValueDefinition<T> definition) {
		ForgeConfigSpec.ConfigValue<T> forge = (ForgeConfigSpec.ConfigValue<T>) definitionsToValues.get(definition);
		return forge.get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void set(ValueDefinition<T> definition, T value) {
		ForgeConfigSpec.ConfigValue<T> forge = (ForgeConfigSpec.ConfigValue<T>) definitionsToValues.get(definition);
		forge.set(value);
	}

	//Not needed on Forge since the config auto refreshes
	//TODO maybe not needed at all
	@Override
	public void refresh(Definition thing) {
		// florp
	}
}
