package fr.estecka.preferredgamerules.config;

import java.io.IOException;
import java.util.Optional;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.world.rule.GameRules;
import tk.estecka.clothgamerules.api.ClothGamerulesScreenBuilder;
import fr.estecka.preferredgamerules.PreferredGamerules;


public class ModMenu
implements ModMenuApi
{
	static private final Text TITLE = Text.translatable("preferred-gamerules.editTitle");
	static public final FeatureSet ALL_FEATURES = FeatureFlags.FEATURE_MANAGER.getFeatureSet();

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		return this::CreateScreen;
	}

	public Screen CreateScreen(Screen parent){
		GameRules preferred = new GameRules(ALL_FEATURES);
		GameRules vanilla = GetVanillaRules();

		if (!FabricLoader.getInstance().isModLoaded("cloth-gamerules"))
			return new EditGameRulesScreen( preferred, r -> {SaveConsummer(r); MinecraftClient.getInstance().setScreen(parent);} );
		else {
			return new ClothGamerulesScreenBuilder()
				.Parent(parent)
				.Title(TITLE)
				.ActiveValues(preferred)
				.ResetValues(vanilla)
				.DisplayValues("editGamerule.default", null)
				.DisplayValues("editGamerule.vanilla", vanilla)
				.OnClosed(r -> SaveConsummer(r))
				.Build()
				;
		}
	}

	static private void SaveConsummer(Optional<GameRules> result){
		if (result.isPresent()){
			PreferredGamerules.preferences.SetAllAsPreferred(result.get());
			try {
				PreferredGamerules.io.Write(PreferredGamerules.preferences);
			}
			catch (IOException e){
				PreferredGamerules.LOGGER.error("Unable to save config: {}", e);
			}
		}
	}

	static private GameRules GetVanillaRules(){
		GameRules result = new GameRules(ALL_FEATURES);

		// FIXME
		// result.accept(new GameRules.Visitor(){
		// 	@Override public <T extends Rule<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type){
		// 		@SuppressWarnings("unchecked")
		// 		T vanilla = (T)IRuleFactory.<T>Of(type).preferredgamerules$GetVanillaValue();
		// 		result.get(key).setValue(vanilla, null);
		// 	}
		// });

		return result;
	}
}
