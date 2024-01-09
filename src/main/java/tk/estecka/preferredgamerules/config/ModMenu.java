package tk.estecka.preferredgamerules.config;

import java.io.IOException;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.world.GameRules;
import tk.estecka.preferredgamerules.PreferredGamerules;

public class ModMenu
implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		return this::CreateScreen;
	}

	public Screen CreateScreen(Screen parent){
		return new EditGameRulesScreen(
			new GameRules(), 
			optRules -> {
				if (optRules.isPresent()){
					PreferredGamerules.gamerules.SetAsPreferred(optRules.get());
					try {
						PreferredGamerules.io.Write(PreferredGamerules.gamerules);
					}
					catch (IOException e){
						PreferredGamerules.LOGGER.error("Unable to save config: {}", e);
					}
				}
				MinecraftClient.getInstance().setScreen(parent);
			}
		);
	}
}
