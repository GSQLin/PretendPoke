package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import static me.figsq.pretendpoke.pretendpoke.V20PokeController.bukkitEntity;

public final class V20ForgeListener extends ForgeListener<
        Pokemon, PixelmonEntity,
        PokeBallImpactEvent, BattleStartedEvent,
        Event
        > {
    public static final V20ForgeListener INSTANCE = new V20ForgeListener(PretendPokePlugin.getInstance());

    public V20ForgeListener(PretendPokePlugin<?, Pokemon, PixelmonEntity> plugin) {
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
        return bukkitEntity(e.getEntityHit().orElse(null));
    }

    @Override
    public void cancelForgeEvent(Event event) {
        if (event instanceof BattleStartedEvent) {
            ((BattleStartedEvent) event).getBattleController().endBattle(BattleEndCause.BATTLE_ERROR);
        }
        event.setCanceled(true);
    }

    @Override
    public List<Entity> getParticipants(BattleStartedEvent e) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (BattleParticipant participant : e.getBattleController().participants) {
            entities.add(bukkitEntity(participant.getEntity()));
        }
        return entities;
    }

    @Override
    public Entity getThrower(PokeBallImpactEvent e) {
        return bukkitEntity(e.getPokeBall().getThrower());
    }
}
