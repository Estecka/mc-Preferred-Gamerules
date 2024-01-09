package tk.estecka.preferredgamerules.mixin;

import net.minecraft.world.GameRules.Rule;
import net.minecraft.world.GameRules.Type;
import tk.estecka.preferredgamerules.IRuleFactory;
import tk.estecka.preferredgamerules.ITypeDuck;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Type.class)
public abstract class TypeMixin<T extends Rule<T>>
implements IRuleFactory<T>, ITypeDuck
{
	@Shadow private @Mutable Function<Type<T>,T> ruleFactory;

	@Unique
	private @Nullable String preferredValue = null;

	public void	preferredgamerules$SetPreferred(String value){
		this.preferredValue = value;
	}

	public Rule<T> preferredgamerules$CreatePreferredRule(){
		Rule<T> rule = this.preferredgamerules$CreateDefaultRule();

		if (preferredValue != null)
			((IRuleMixin)rule).callDeserialize(preferredValue);

		return rule;
	}

	@SuppressWarnings("unchecked")
	public Rule<T> preferredgamerules$CreateDefaultRule(){
		return this.ruleFactory.apply((Type<T>)(Object)this);
	}

	@Inject( method="createRule", at=@At("RETURN"))
	private void createPreferredByDefault(CallbackInfoReturnable<T> info){
		if (this.preferredValue != null)
			((IRuleMixin)info.getReturnValue()).callDeserialize(this.preferredValue);
	}
}
