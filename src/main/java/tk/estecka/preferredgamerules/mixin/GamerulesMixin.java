package tk.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
import net.minecraft.world.GameRules.Rule;
import net.minecraft.world.GameRules.Type;
import tk.estecka.preferredgamerules.PreferredGamerules;


@Mixin(GameRules.class)
public class GamerulesMixin 
{
	@Inject( method="<clinit>", at=@At("HEAD") )
	static private void	entryPoint(CallbackInfo info){
		PreferredGamerules.LoadConfig();
	}

	/*
	 * Applies preferences to rules that  are registered _after_ the config file
	 * was loaded.
	 */
	@Inject( method="register", at=@At( value="INVOKE", shift=Shift.AFTER, target="java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;" ) )
	static private <T extends Rule<T>> void	registerPreferred(String name, Category category, Type<T> type, CallbackInfoReturnable<?> ci, @Local Key<T> key){
		PreferredGamerules.gamerules.Apply(key, type);
	}
}
