package tk.estecka.preferredgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Rule.class)
public interface IRuleMixin 
{
	@Invoker void	callDeserialize(String value);
	@Invoker String	callSerialize();
}
