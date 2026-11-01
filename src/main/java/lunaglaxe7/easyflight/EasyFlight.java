package lunaglaxe7.easyflight;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(EasyFlight.MODID)
public class EasyFlight {
    public static final String MODID = "easyflight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EasyFlight(IEventBus modEventBus, ModContainer modContainer) {
    }
}
