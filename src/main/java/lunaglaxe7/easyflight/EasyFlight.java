package lunaglaxe7.easyflight;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(EasyFlight.MODID)
public class EasyFlight {
    public static final String MODID = "easyflight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EasyFlight(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::reloadConfig);
        modEventBus.addListener(this::registerPayload);
        FlightAttachment.ATTACHMENTS.register(modEventBus);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event){

    }

    private void reloadConfig(ModConfigEvent.Reloading event){
        FlightKeyNetwork.reloadBound();
    }

    private void registerPayload(RegisterPayloadHandlersEvent event){
        var re = event.registrar("1");
        re.playToServer(FlightKeyNetwork.TYPE, FlightKeyNetwork.CODEC, FlightKeyNetwork::handle);
    }


}
