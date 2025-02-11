package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;

public class Main extends PretendPokePlugin<EnumSpecies,Pokemon, EntityPixelmon>{
    public Main(){
        super();
        this.getLogger().info("§a插件套壳版本: 1.12.2");
        this.getLogger().info("§a模组版本: Pixelmon-1.12.2-8.4.3");
    }

    @Override
    public @NonNull PokeController<EnumSpecies,Pokemon, EntityPixelmon> getPokeController() {
        return V12PokeController.INSTANCE;
    }

    @Override
    public @NonNull ForgeListener<Pokemon, EntityPixelmon, ?> getForgeListener() {
        return V12ForgeListener.INSTANCE;
    }
}
