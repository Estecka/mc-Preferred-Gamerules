package fr.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import fr.estecka.preferredgamerules.PrefRulesMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gamerules.GameRule;

@Mixin(Registry.class)
public interface RegistryMixin
{

	@Inject(
		require = 2,
		method = {
			"registerForHolder(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;)Lnet/minecraft/core/Holder$Reference;",
			"register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;)Ljava/lang/Object;"
		},
		at = @At("HEAD")
	)
	static private void OnRegister(
		Registry<?> registry,
		ResourceKey<GameRule<?>> key,
		Object entry,
		CallbackInfoReturnable<?> ci
	){
		if (registry == BuiltInRegistries.GAME_RULE && entry instanceof GameRule<?> rule){
			OnGameruleRegistered(key, rule);
		}
	}

	@Unique
	static private void OnGameruleRegistered(ResourceKey<GameRule<?>> key, GameRule<?> rule){
		PrefRulesMod.preferences.ApplySingle(key.identifier(), rule);
	}
}
