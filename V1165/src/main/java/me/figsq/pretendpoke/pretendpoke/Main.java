package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import net.minecraftforge.eventbus.api.Event;

public class Main extends PretendPokePlugin<Species, Pokemon, PixelmonEntity>{

    @Override
    public @NonNull PokeController<Species, Pokemon, PixelmonEntity> getPokeController() {
        return V16PokeController.INSTANCE;
    }

    @Override
    public ForgeListener<Pokemon, PixelmonEntity, PokeBallImpactEvent, BattleStartedEvent, Event> getForgeListener() {
        return V16ForgeListener.INSTANCE;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getLogger().info("§aShell version: 1.16.5");
        this.getLogger().info("§aMod version: Pixelmon-1.16.5-9.1.9[Development and usage version]");
    }
}
