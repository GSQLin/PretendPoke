package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;

public class Main extends PretendPokePlugin<Pokemon, EntityPixelmon>{
    @Override
    public @NonNull PokeController<Pokemon, EntityPixelmon> getPokeController() {
        return V12PokeController.INSTANCE;
    }

    @Override
    public @NonNull ForgeListener<Pokemon, EntityPixelmon, ?> getForgeListener() {
        return V12ForgeListener.INSTANCE;
    }
}
