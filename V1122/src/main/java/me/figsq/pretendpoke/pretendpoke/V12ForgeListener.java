package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public final class V12ForgeListener extends ForgeListener<
        Pokemon, EntityPixelmon,
        PokeballImpactEvent, BattleStartedEvent,
        Event
        > {

    public static final ForgeListener<Pokemon, EntityPixelmon, PokeballImpactEvent, BattleStartedEvent, Event> INSTANCE = new V12ForgeListener(PretendPokePlugin.getInstance());

    public V12ForgeListener(PretendPokePlugin<?, Pokemon, EntityPixelmon> plugin) {
        super(plugin);
    }

    @Override
    public Class<PokeballImpactEvent> getPokeballImpactEventClass() {
        return PokeballImpactEvent.class;
    }

    @Override
    public Class<BattleStartedEvent> getBattleStartEventClass() {
        return BattleStartedEvent.class;
    }

    @Override
    public Entity getBallHitEntity(PokeballImpactEvent event) {
        if (event.getEntityHit() == null) {
            return null;
        }
        net.minecraft.server.v1_12_R1.Entity hit = (net.minecraft.server.v1_12_R1.Entity) (Object) event.getEntityHit();
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), hit);
    }

    @Override
    public void cancelForgeEvent(Event event) {
        if (event instanceof BattleStartedEvent) {
            BattleStartedEvent e = (BattleStartedEvent) event;
            BattleControllerBase bc = e.bc;
            bc.endBattle(EnumBattleEndCause.FORCE);
            return;
        }
        event.setCanceled(true);
    }

    @Override
    public List<Entity> getParticipants(BattleStartedEvent battleStartedEvent) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (BattleParticipant participant : battleStartedEvent.bc.participants) {
            entities.add(V12PokeController.bukkitEntity(participant.getEntity()));
        }
        return entities;
    }

    @Override
    public Entity getThrower(PokeballImpactEvent event) {
        return V12PokeController.bukkitEntity(event.pokeball.field_70192_c);
    }
}
