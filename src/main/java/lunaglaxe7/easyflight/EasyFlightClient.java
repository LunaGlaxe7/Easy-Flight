package lunaglaxe7.easyflight;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

@Mod(value = EasyFlight.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = EasyFlight.MODID, value = Dist.CLIENT)
public class EasyFlightClient {

    public static final String FLIGHT_KEY_NAME = "key.easyflight.toggle";
    public static final String GLIDE_KEY_NAME = "key.easyflight.glide_toggle";
    public static final KeyMapping FLIGHT_KEY = new KeyMapping(
            FLIGHT_KEY_NAME, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            KeyMapping.CATEGORY_MISC
    );
    public static final KeyMapping GLIDE_KEY = new KeyMapping(
            GLIDE_KEY_NAME, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,KeyMapping.CATEGORY_MISC
    );

    @SubscribeEvent
    public static void registerKeyMapping(RegisterKeyMappingsEvent event){
        event.register(FLIGHT_KEY);
        event.register(GLIDE_KEY);
    }

    public EasyFlightClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void checkFlightKey(ClientTickEvent.Post event){
        if (FLIGHT_KEY.consumeClick()){
            FlightKeyNetwork.sendFlight();
        }
        if (GLIDE_KEY.consumeClick()){
            FlightKeyNetwork.sendGlide();
        }
    }

}
