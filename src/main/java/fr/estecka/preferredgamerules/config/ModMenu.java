package fr.estecka.preferredgamerules.config;

import java.io.IOException;
import java.util.Optional;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
// import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationGameRulesScreen;
// import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
// import tk.estecka.clothgamerules.api.ClothGamerulesScreenBuilder;
import fr.estecka.preferredgamerules.IRuleFactory;
import fr.estecka.preferredgamerules.PrefRulesMod;


public class ModMenu
implements ModMenuApi
{
	// static private final Component TITLE = Component.translatable("preferred-gamerules.editTitle");
	static public final FeatureFlagSet ALL_FEATURES = FeatureFlags.REGISTRY.allFlags();

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		return this::CreateScreen;
	}

	public Screen CreateScreen(Screen parent){
		GameRules preferred = new GameRules(ALL_FEATURES);
		// GameRules vanilla = GetVanillaRules();

		// if (!FabricLoader.getInstance().isModLoaded("cloth-gamerules"))
			return new WorldCreationGameRulesScreen( preferred, r -> {SaveConsummer(r); Minecraft.getInstance().setScreen(parent);} );
		// else {
		// 	return new ClothGamerulesScreenBuilder()
		// 		.Parent(parent)
		// 		.Title(TITLE)
		// 		.ActiveValues(preferred)
		// 		.ResetValues(vanilla)
		// 		.DisplayValues("editGamerule.default", null)
		// 		.DisplayValues("editGamerule.vanilla", vanilla)
		// 		.OnClosed(r -> SaveConsummer(r))
		// 		.Build()
		// 		;
		// }
	}

	static private void SaveConsummer(Optional<GameRules> result){
		if (result.isPresent()){
			PrefRulesMod.preferences.SetAllAsPreferred(result.get());
			try {
				PrefRulesMod.io.Write(PrefRulesMod.preferences);
			}
			catch (IOException e){
				PrefRulesMod.LOGGER.error("Unable to save config: {}", e);
			}
		}
	}

	static private GameRules GetVanillaRules(){
		final GameRules result = new GameRules(ALL_FEATURES);
		result.availableRules().forEach(rule -> SetVanillaSingle(result, rule));
		return result;
	}

	static private <T> void SetVanillaSingle(GameRules values, GameRule<T> type){
		values.set(type, IRuleFactory.Of(type).preferredgamerules$GetVanillaValue(), null);
	}
}
