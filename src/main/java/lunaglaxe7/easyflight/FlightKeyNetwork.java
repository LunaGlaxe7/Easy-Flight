package lunaglaxe7.easyflight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record FlightKeyNetwork(boolean canFlight, boolean gliding) implements CustomPacketPayload {

    public static final Type<FlightKeyNetwork> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(EasyFlight.MODID, "flight_toggle"));

    public static final StreamCodec<FriendlyByteBuf, FlightKeyNetwork> CODEC =
            StreamCodec.of((buf,p)->{
                buf.writeBoolean(p.canFlight());
                buf.writeBoolean(p.gliding());},
                    buf->new FlightKeyNetwork(buf.readBoolean(),buf.readBoolean()));

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void sendFlight(){
        var player = Minecraft.getInstance().player;
        if (player != null){
            var flight = player.getData(FlightAttachment.FLIGHT);
            boolean canFlight = !flight.canFlight();

            player.setData(FlightAttachment.FLIGHT, flight.withFlight(canFlight));
            PacketDistributor.sendToServer(new FlightKeyNetwork(canFlight, false));
        }
    }

    public static void sendGlide(){
        var player = Minecraft.getInstance().player;
        if (player != null){
            var flight = player.getData(FlightAttachment.FLIGHT);
            if (flight.canFlight()){
                boolean gliding = !flight.gliding();
                player.setData(FlightAttachment.FLIGHT, flight.withGliding(gliding));
                PacketDistributor.sendToServer(new FlightKeyNetwork(true, gliding));
            }
        }
    }

    public static void handle(FlightKeyNetwork payload, IPayloadContext ctx){
        ctx.enqueueWork(()->{
            var player = ctx.player();
            player.setData(FlightAttachment.FLIGHT,
                    new FlightAttachment.Flight(payload.canFlight(), payload.gliding()));

            onFlightChanging(player, payload.canFlight());
            var flight = player.getData(FlightAttachment.FLIGHT);
            if (flight.canFlight()){
                if (flight.gliding()){
                    info("gliding enabled");
                }else {
                    info("gliding disabled");
                }
            }else {
                info("flight disabled");
            }
        });
    }

    private static void onFlightChanging(Player player, boolean canFlight){
        var fly = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (canFlight && !player.mayFly()){
            if (fly != null){
                fly.setBaseValue(1);
            }
        }
        if (!canFlight) {
            if (fly != null) {
                fly.setBaseValue(0);
            }
        }
    }

    private static volatile int bound = Config.GlidingSpeedBound.getAsInt();

    public static void playerFlight(LocalPlayer player){
        if (player.input.hasForwardImpulse()) {
            var vv = FlightVec.from(player.getXRot(), player.getYRot());
            var speedo = player.getDeltaMovement().length();
            var vvr = new Vec3(vv.vx(), vv.vy(), vv.vz());

            if (speedo < 0.0005f){
                player.addDeltaMovement(vvr.scale(0.5));
            }else {
                vvr.scale(speedo);
                vvr.add(vvr.scale(0.05));
                player.setDeltaMovement(vvr);
            }

            var speed = player.getDeltaMovement().length();
            if (speed > getBound()){
                player.setDeltaMovement(player.getDeltaMovement().scale(bound / speed));
            }
        }else {
            player.setDeltaMovement(player.getDeltaMovement().scale(0.8D));
        }

        player.move(MoverType.SELF, player.getDeltaMovement());
        player.resetFallDistance();
    }

    public static void reloadBound(){
        bound = Config.GlidingSpeedBound.getAsInt();
    }

    private static int getBound(){
        return bound;
    }

    private static void info(String format, Object... args){
        if (args.length > 0) {
            EasyFlight.LOGGER.info(format, args);
        }else {
            EasyFlight.LOGGER.info(format);
        }
    }
}
