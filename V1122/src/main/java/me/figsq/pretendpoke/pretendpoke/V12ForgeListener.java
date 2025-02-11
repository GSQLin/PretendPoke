package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public final class V12ForgeListener extends ForgeListener<
        Pokemon, EntityPixelmon,
        PokeballImpactEvent
        > {

    public static final ForgeListener<Pokemon, EntityPixelmon,?> INSTANCE = new V12ForgeListener(PretendPokePlugin.getInstance());

    public V12ForgeListener(PretendPokePlugin<?,Pokemon, EntityPixelmon> plugin) {
        super(plugin);
    }

    @Override
    public Class<PokeballImpactEvent> getPokeballImpactEventClass() {
        return PokeballImpactEvent.class;
    }

    @Override
    public Entity getBallHitEntity(PokeballImpactEvent event) {
        if (event.getEntityHit() == null) {
            return null;
        }
        net.minecraft.server.v1_12_R1.Entity hit = (net.minecraft.server.v1_12_R1.Entity) (Object) event.getEntityHit();
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()),hit);
    }
}
