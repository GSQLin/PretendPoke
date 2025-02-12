package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;

public class Main extends PretendPokePlugin<Species, Pokemon, PixelmonEntity>{
    @Override
    public @NonNull PokeController<Species, Pokemon, PixelmonEntity> getPokeController() {
        return V20PokeController.INSTANCE;
    }

    @Override
    public ForgeListener<Pokemon, PixelmonEntity, ?, ?, ?> getForgeListener() {
        return null;
    }
}
