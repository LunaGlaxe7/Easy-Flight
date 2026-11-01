package lunaglaxe7.easyflight;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(EasyFlight.MODID)
public class EasyFlight {
    public static final String MODID = "easyflight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EasyFlight(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayLoad);
        modEventBus.addListener(this::reloadConfig);

        FlightAttachment.ATTACHMENTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void reloadConfig(ModConfigEvent.Reloading event){
        FlightKeyNetwork.reloadBound();
    }

    private void registerPayLoad(RegisterPayloadHandlersEvent event){
        var re = event.registrar("1");
        re.playToServer(FlightKeyNetwork.TYPE, FlightKeyNetwork.CODEC, FlightKeyNetwork::handle);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
