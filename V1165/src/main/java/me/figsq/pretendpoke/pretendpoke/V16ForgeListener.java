package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class V16ForgeListener extends ForgeListener<
        Pokemon, PixelmonEntity,
        PokeBallImpactEvent, BattleStartedEvent,
        Event
        > {
    public static final V16ForgeListener INSTANCE = new V16ForgeListener(PretendPokePlugin.getInstance());

    public V16ForgeListener(PretendPokePlugin<?, Pokemon, PixelmonEntity> plugin) {
        super(plugin);
    }

    @Override
    public Class<PokeBallImpactEvent> getPokeballImpactEventClass() {
        return PokeBallImpactEvent.class;
    }

    @Override
    public Class<BattleStartedEvent> getBattleStartEventClass() {
        return BattleStartedEvent.class;
    }

    @Override
    public Entity getBallHitEntity(PokeBallImpactEvent e) {
        return  V16PokeController.INSTANCE.bukkitEntity(e.getEntityHit().orElse(null));
    }

    @Override
    public void cancelForgeEvent(Event event) {
        if (event instanceof BattleStartedEvent) {
            BattleStartedEvent e = (BattleStartedEvent) event;
            e.getBattleController().endBattle(BattleEndCause.FORCE);
            return;
        }
        event.setCanceled(true);
    }

    @Override
    public List<Entity> getParticipants(BattleStartedEvent e) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (BattleParticipant participant : e.getBattleController().participants) {
            entities.add(V16PokeController.INSTANCE.bukkitEntity(participant.getEntity()));
        }
        return entities;
    }

    @Override
    public Entity getThrower(PokeBallImpactEvent e) {
        return Bukkit.getPlayer(e.getPokeBall().getOwnerId());
    }
}
