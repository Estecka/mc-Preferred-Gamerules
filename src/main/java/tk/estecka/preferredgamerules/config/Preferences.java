package tk.estecka.preferredgamerules.config;

import java.util.HashMap;
import java.util.Map;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRules;
import tk.estecka.preferredgamerules.IRuleFactory;
import tk.estecka.preferredgamerules.PreferredGamerules;


public class Preferences
implements ConfigIO.ICodec
{
	static private final String PREFIX = "gamerule.";
	public final Map<String, String> rawValues = new HashMap<>();

	@Override
	public void Decode(Map<String, String> values){
		for (String key : values.keySet())
		if  (key.startsWith(PREFIX)) {
			rawValues.put(key.substring(PREFIX.length()), values.get(key));
		}
	}

	@Override
	public Map<String, String> Encode(){
		Map<String, String> values = new HashMap<>();
		for (var entry : this.rawValues.entrySet()){
			values.put(PREFIX+entry.getKey(), entry.getValue());
		}
	
		return values;
	}

	/**
	 * Update a single registered rule to match the preferences.
	 * @param ruleId
	 */
	public void ApplySingle(Identifier ruleId){
		GameRule<?> rule =  Registries.GAME_RULE.get(ruleId);
		String preferredValue = rawValues.get(ruleId.toString());

		DataResult<?> result = IRuleFactory.Of(rule).preferredgamerules$SetPreferred(preferredValue);
		result.ifError(err->PreferredGamerules.LOGGER.error(
			"Invalid value for gamerule {}: \"{}\"\n{}",
			ruleId, preferredValue, err.message()
		));
	}

	/**
	 * Changes all currently registered gamerules to match the preferences.
	 */
	public void ApplyAll(){
		Registries.GAME_RULE.streamKeys()
			.map(RegistryKey::getValue)
			.forEach(this::ApplySingle)
			;
	}

	/**
	 * Changes preferences and registered rules to match the given rules.
	 */
	public void	SetAllAsPreferred(final GameRules ruleValues){
		ruleValues.streamRules().forEach(type -> this.SetSingleAsPreferred(ruleValues, type));
	}

	public <T> void	SetSingleAsPreferred(GameRules values, GameRule<T> type){
		Identifier key = Registries.GAME_RULE.getId(type);
		T value = values.getValue(type);

		// Update preferences
		String rawValue = type.getValueName(value);
		if (rawValue.equals(IRuleFactory.Of(type).preferredgamerules$GetVanillaValue()))
			this.rawValues.remove(key.toString());
		else
			this.rawValues.put(key.toString(), rawValue);

		// Update registered
		this.ApplySingle(key);
	}
}
