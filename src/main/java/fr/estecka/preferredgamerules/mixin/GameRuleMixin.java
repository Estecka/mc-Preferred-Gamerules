package fr.estecka.preferredgamerules.mixin;

import net.minecraft.world.rule.GameRule;
import fr.estecka.preferredgamerules.IRuleFactory;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.serialization.DataResult;


@Mixin(GameRule.class)
public abstract class GameRuleMixin<T>
implements IRuleFactory<T>
{
	@Shadow private @Final T defaultValue;
	@Unique private Optional<T> preferredValue = null;

	@Shadow public abstract DataResult<T> deserialize(String value);


	public DataResult<T> preferredgamerules$SetPreferred(@Nullable String value){
		if (value == null){
			this.preferredValue = Optional.empty();
			return DataResult.success(this.defaultValue);
		}
		else {
			DataResult<T> result = this.deserialize(value);
			result.ifSuccess(r -> this.preferredValue = Optional.of(r));
			return result;
		}

	}

	public Optional<T> preferredgamerules$GetPreferredValue(){
		return this.preferredValue;
	}

	public T preferredgamerules$GetVanillaValue(){
		return this.defaultValue;
	}

	public T preferredgamerules$GetDefaultValue(){
		return this.preferredValue.orElse(this.defaultValue);
	}

	@Inject( method="getDefaultValue", at=@At("RETURN"))
	private void GetPreferredByDefault(CallbackInfoReturnable<T> info){
		info.setReturnValue(this.preferredgamerules$GetDefaultValue());
	}
}
