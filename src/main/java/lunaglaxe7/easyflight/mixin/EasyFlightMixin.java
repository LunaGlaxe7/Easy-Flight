package lunaglaxe7.easyflight.mixin;

import lunaglaxe7.easyflight.FlightAttachment;
import lunaglaxe7.easyflight.FlightKeyNetwork;
import lunaglaxe7.easyflight.FlightVec;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EasyFlightMixin {

    @Inject(method = "travel",
        at = @At("HEAD"), cancellable = true
    )
    public void easyFlight$handleFlight(Vec3 v, CallbackInfo c){
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof LocalPlayer player){
            var flight = player.getData(FlightAttachment.FLIGHT);
            if (flight.canFlight() && flight.gliding()){
                FlightKeyNetwork.playerFlight(player);
                player.calculateEntityAnimation(true);
                c.cancel();
            }
        }
    }
}
