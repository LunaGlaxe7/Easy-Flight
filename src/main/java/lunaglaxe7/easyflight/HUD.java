package lunaglaxe7.easyflight;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = EasyFlight.MODID, value = Dist.CLIENT)
public class HUD {

    private static final ResourceLocation flight_icon = ResourceLocation.fromNamespaceAndPath(
            EasyFlight.MODID,"textures/hud/flight.png"
    );

    private static final ResourceLocation glide_icon = ResourceLocation.fromNamespaceAndPath(
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

    private static void drawScaled32At(GuiGraphics gui,ResourceLocation icon, int x, int y, float scale){
        gui.pose().pushPose();
        gui.pose().translate(x, y,0);
        gui.pose().scale(scale, scale, 1);
        gui.blit(icon, 0, 0, 0, 0, 32 ,32, 32, 32);
        gui.pose().popPose();
    }
}
