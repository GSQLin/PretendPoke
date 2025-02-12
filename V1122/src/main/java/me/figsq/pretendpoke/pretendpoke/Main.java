package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import net.minecraftforge.fml.common.eventhandler.Event;

public class Main extends PretendPokePlugin<EnumSpecies,Pokemon, EntityPixelmon>{

    @Override
    public @NonNull PokeController<EnumSpecies,Pokemon, EntityPixelmon> getPokeController() {
        return V12PokeController.INSTANCE;
    }

    @Override
    public @NonNull ForgeListener<Pokemon, EntityPixelmon, PokeballImpactEvent, BattleStartedEvent, Event> getForgeListener() {
        return V12ForgeListener.INSTANCE;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getLogger().info("§aShell version: 1.12.2");
        this.getLogger().info("§aMod version: Pixelmon-1.12.2-8.4.3");
    }
}
