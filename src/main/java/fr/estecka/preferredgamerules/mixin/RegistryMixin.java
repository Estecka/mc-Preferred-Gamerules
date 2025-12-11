package fr.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import fr.estecka.preferredgamerules.PrefRulesMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.rule.GameRule;

@Mixin(Registry.class)
public interface RegistryMixin
{

	@Inject(
		require = 2,
		method = {
			"registerReference(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;",
			"register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;"
		},
		at = @At("HEAD")
	)
	static private void OnRegister(
		Registry<?> registry,
		RegistryKey<GameRule<?>> key,
		Object entry,
		CallbackInfoReturnable<?> ci
	){
		if (registry == Registries.GAME_RULE && entry instanceof GameRule<?> rule){
			OnGameruleRegistered(key, rule);
		}
	}

	@Unique
	static private void OnGameruleRegistered(RegistryKey<GameRule<?>> key, GameRule<?> rule){
		PrefRulesMod.preferences.ApplySingle(key.getValue(), rule);
	}
}
