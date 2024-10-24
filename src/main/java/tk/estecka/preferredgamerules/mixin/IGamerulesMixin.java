package tk.estecka.preferredgamerules.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.*;

@Mixin(GameRules.class)
public interface IGamerulesMixin
{
	@Accessor("RULE_TYPES")
	static Map<Key<?>, Type<?>> GetAllRules(){ throw new AssertionError(); }
}
