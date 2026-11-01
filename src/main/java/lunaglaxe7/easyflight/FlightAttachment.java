package lunaglaxe7.easyflight;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FlightAttachment {
    public final static DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, EasyFlight.MODID);

    public final static Supplier<AttachmentType<Flight>> FLIGHT =
            ATTACHMENTS.register("ef.flight", ()->
                AttachmentType.builder(()-> new Flight(false, false))
                        .serialize(Flight.CODEC)
                        .sync(Flight.STREAM_CODEC)
                        .build()
            );

    public record Flight(boolean canFlight, boolean gliding){

        public static final Codec<Flight> CODEC =
                RecordCodecBuilder.create(
                        i -> i.group(
                                Codec.BOOL.fieldOf("canflight").forGetter(Flight::canFlight),
                                Codec.BOOL.fieldOf("gliding").forGetter(Flight::gliding)
                        ).apply(i, Flight::new)
                );
        public static final StreamCodec<FriendlyByteBuf, Flight> STREAM_CODEC =
                StreamCodec.of(((buf, v) -> {
                    buf.writeBoolean(v.canFlight());
                    buf.writeBoolean(v.gliding());
                        }),
                        buf->new Flight(buf.readBoolean(), buf.readBoolean()));

        /**
         * @return gliding is always false when changing canFlight
         */
        public Flight withFlight(boolean canFlight){
            return new Flight(canFlight, false);
        }

        public Flight withGliding(boolean gliding){
            return new Flight(this.canFlight, gliding);
        }
    }
}
