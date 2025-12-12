package fr.estecka.preferredgamerules;

import fr.estecka.preferredgamerules.config.ConfigIO;
import fr.estecka.preferredgamerules.config.Preferences;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrefRulesMod
{
	static public final String MODID = "preferred-gamerules";
	static public final Logger LOGGER = LoggerFactory.getLogger(MODID);
	static public final ConfigIO io = new ConfigIO(MODID+".properties");
	static public final Preferences preferences = new Preferences();

	static public void LoadConfig() {
		try {
			io.GetOrCreate(preferences);
		}
		catch (IOException e){
			LOGGER.error("{}", e);
		}
	}
}
