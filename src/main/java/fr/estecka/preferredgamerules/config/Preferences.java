package fr.estecka.preferredgamerules.config;

import java.util.HashMap;
import java.util.Map;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRules;
import fr.estecka.preferredgamerules.IRuleFactory;
import fr.estecka.preferredgamerules.PrefRulesMod;


public class Preferences
implements ConfigIO.ICodec
{
	static private final String PREFIX = "gamerule.";
	public final Map<Identifier, String> rawValues = new HashMap<>();
	public final Map<String, String> badIds = new HashMap<>();

	@Override
	public void Decode(Map<String, String> values){
		for (String key : values.keySet())
		if  (key.startsWith(PREFIX)) {
			String value = values.get(key);
			key = key.substring(PREFIX.length());

			Identifier id = Identifier.tryParse(key);
			if (id != null)
				rawValues.put(id, value);
			else {
				badIds.put(key, value);
				PrefRulesMod.LOGGER.error("Invalid gamerule ID: {}", key);
			}
		}
	}

	@Override
	public Map<String, String> Encode(){
		Map<String, String> values = new HashMap<>();
		for (var entry : this.rawValues.entrySet()){
			values.put(PREFIX+entry.getKey(), entry.getValue());
		}
		for (var entry : this.badIds.entrySet()){
			values.put(PREFIX+entry.getKey(), entry.getValue());
		}
	
		return values;
	}

	/**
	 * Update a single registered rule to match the preferences.
	 * @param ruleId
	 */
	public void ApplySingle(Identifier ruleId){
		this.ApplySingle(ruleId, Registries.GAME_RULE.get(ruleId));
	}

	public void ApplySingle(Identifier ruleId, GameRule<?> rule){
		String preferredValue = rawValues.get(ruleId);

		DataResult<?> result = IRuleFactory.Of(rule).preferredgamerules$SetPreferred(preferredValue);
		result.ifError(err->PrefRulesMod.LOGGER.error(
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

		// Update preferences
		String rawValue = type.getValueName(values.getValue(type));
		String vanilla  = type.getValueName(IRuleFactory.Of(type).preferredgamerules$GetVanillaValue());
		if (rawValue.equals(vanilla))
			this.rawValues.remove(key);
		else
			this.rawValues.put(key, rawValue);

		// Update registered
		this.ApplySingle(key);
	}
}
