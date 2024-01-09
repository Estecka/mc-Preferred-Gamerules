package tk.estecka.preferredgamerules;

import tk.estecka.preferredgamerules.config.ConfigIO;
import tk.estecka.preferredgamerules.config.Preferences;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferredGamerules
{
	static public final Logger LOGGER = LoggerFactory.getLogger("preferred-gamerules");
	static public final ConfigIO io = new ConfigIO("preferred-gamerules.properties");
	static public final Preferences gamerules = new Preferences();

	static public void LoadConfig() {
		try {
			io.GetOrCreate(gamerules);
		}
		catch (IOException e){
			LOGGER.error("{}", e);
		}
	}
}
