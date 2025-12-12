package fr.estecka.preferredgamerules;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.DataResult;
import net.minecraft.world.rule.GameRule;


public interface IRuleFactory<T>
{
	@SuppressWarnings("unchecked")
	static public <T> IRuleFactory<T> Of(GameRule<T> type){
		return (IRuleFactory<T>)(Object)type;
	}

	DataResult<T> preferredgamerules$SetPreferred(@Nullable String value);

	Optional<T> preferredgamerules$GetPreferredValue();
	T preferredgamerules$GetVanillaValue();
}
