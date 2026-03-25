package fr.estecka.preferredgamerules.mixin;

import fr.estecka.preferredgamerules.IRuleFactory;
import java.util.Optional;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
			result.ifSuccess(r -> this.preferredValue = Optional.of(r))
			      .ifError(err -> this.preferredValue = Optional.empty())
			      ;
			return result;
		}

	}

	public Optional<T> preferredgamerules$GetPreferredValue(){
		return this.preferredValue;
	}

	public T preferredgamerules$GetVanillaValue(){
		return this.defaultValue;
	}

	@Overwrite
	public T defaultValue(){
		return this.preferredValue.orElse(this.defaultValue);
	}
}
