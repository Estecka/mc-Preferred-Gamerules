package tk.estecka.preferredgamerules;

import net.minecraft.world.GameRules.Rule;
import net.minecraft.world.GameRules.Type;

public interface IRuleFactory<T extends Rule<T>>
{
	@SuppressWarnings("unchecked")
	static public <T extends Rule<T>> IRuleFactory<T> Of(Type<T> type){
		return (IRuleFactory<T>)type;
	}

	Rule<T> preferredgamerules$CreatePreferredRule();
	Rule<T> preferredgamerules$CreateDefaultRule();
}
