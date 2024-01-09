package tk.estecka.preferredgamerules.config;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Key;
import net.minecraft.world.GameRules.Rule;
import net.minecraft.world.GameRules.Type;
import net.minecraft.world.GameRules.Visitor;
import tk.estecka.preferredgamerules.IRuleFactory;
import tk.estecka.preferredgamerules.ITypeDuck;
import tk.estecka.preferredgamerules.PreferredGamerules;
import tk.estecka.preferredgamerules.mixin.IRuleMixin;

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

	public <T extends Rule<T>> void Apply(Key<T> key, Type<T> type){
		String preferred = rawValues.get(key.getName());

		if (preferred != null){
			IRuleMixin tester = (IRuleMixin)IRuleFactory.Of(type).preferredgamerules$CreateDefaultRule();

			String validated;
			tester.callDeserialize(preferred);
			validated = tester.callSerialize();

			if (!preferred.equals(validated)){
				PreferredGamerules.LOGGER.error("Invalid value for gamerule {}: \"{}\"", key.getName(), preferred);
				preferred = null;
			}
		}

		((ITypeDuck)type).preferredgamerules$SetPreferred(preferred);
	}

	/**
	 * Changes all currently registered gamerules to match the preferences.
	 */
	public void ApplyAll(){
		GameRules.accept(new Visitor() {
			@Override public <T extends Rule<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type){
				Apply(key, type);
			}
		});
	}

	/**
	 * Changes preferences to match the given rules.
	 */
	public void	SetAsPreferred(GameRules rules){
		GameRules.accept(new Visitor() {
			@Override public <T extends Rule<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type){
				String keyString = key.getName();
				String preferredvalue = rules.get(key).serialize();
				String defaultValue = IRuleFactory.Of(type).preferredgamerules$CreateDefaultRule().serialize();

				if (preferredvalue.equals(defaultValue))
					rawValues.remove(keyString);
				else
					rawValues.put(keyString, preferredvalue);

				Apply(key, type);
			}
		});
	}
}
