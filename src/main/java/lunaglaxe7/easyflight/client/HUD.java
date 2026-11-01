package lunaglaxe7.easyflight.client;

import lunaglaxe7.easyflight.EasyFlight;
import lunaglaxe7.easyflight.FlightAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = EasyFlight.MODID, value = Dist.CLIENT)
public class HUD {

    private static final Identifier flight_icon = Identifier.fromNamespaceAndPath(
            EasyFlight.MODID,"textures/hud/flight.png"
    );

    private static final Identifier glide_icon = Identifier.fromNamespaceAndPath(
            EasyFlight.MODID,"textures/hud/glide.png"
    );

    @SubscribeEvent
    public static void renderFlightHUD(RenderGuiEvent.Post event){
        var mc = Minecraft.getInstance();

        if (mc.player == null || !mc.player.isLocalPlayer()) return;

        var window = mc.getWindow();

        var player = mc.player;
        var data = player.getData(FlightAttachment.FLIGHT);
        boolean flight = data.canFlight();
        boolean glide = data.gliding();
        if (flight){
            var gui = event.getGuiGraphics();
            int height = 22;
            int size = 8;
            int left = (window.getGuiScaledWidth() - 9 * 18) / 2 - size - 8 - 2;
            int top = window.getGuiScaledHeight() - height;

            drawScaled32At(gui, flight_icon, left, top, 0.25f);
            if (glide){
                top += size;
                drawScaled32At(gui, glide_icon, left, top, 0.25f);
            }
        }

    }

    private static void drawScaled32At(GuiGraphicsExtractor gui, Identifier icon, int x, int y, float scale){
        gui.pose().pushMatrix();
        gui.pose().translate(x, y);
        gui.pose().scale(scale, scale);
        gui.blit(icon, 0, 0, 32, 32, 0 ,1, 0, 1);
        gui.pose().popMatrix();
    }
}
