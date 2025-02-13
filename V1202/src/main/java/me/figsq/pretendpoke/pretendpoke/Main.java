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
        return V20ForgeListener.INSTANCE;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getLogger().info("§aShell version: 1.20.2");
        this.getLogger().info("§aMod version: Pixelmon-1.20.2-9.2.7[Development and usage version]");
    }
}
