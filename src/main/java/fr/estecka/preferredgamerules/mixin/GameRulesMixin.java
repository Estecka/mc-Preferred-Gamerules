package fr.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fr.estecka.preferredgamerules.PrefRulesMod;
import net.minecraft.world.level.gamerules.GameRules;


@Mixin(GameRules.class)
public class GameRulesMixin
{
	@Inject( method="<clinit>", at=@At("HEAD") )
	static private void	LoadConfig(CallbackInfo info){
		PrefRulesMod.LoadConfig();
		PrefRulesMod.preferences.ApplyAll();
	}
}
