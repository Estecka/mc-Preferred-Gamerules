package fr.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fr.estecka.preferredgamerules.PreferredGamerules;


@Mixin(GameRulesMixin.class)
public class GameRulesMixin
{
	@Inject( method="<clinit>", at=@At("HEAD") )
	static private void	entryPoint(CallbackInfo info){
		PreferredGamerules.LoadConfig();
		PreferredGamerules.preferences.ApplyAll();
	}
}
