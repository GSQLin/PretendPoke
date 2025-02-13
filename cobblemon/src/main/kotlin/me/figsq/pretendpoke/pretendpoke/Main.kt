package me.figsq.pretendpoke.pretendpoke

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import me.figsq.pretendpoke.pretendpoke.api.PokeController
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener
import me.fullidle.ficore.ficore.common.SomeMethod

class Main: PretendPokePlugin<Species, Pokemon, PokemonEntity>() {
    override fun getPokeController(): PokeController<Species, Pokemon, PokemonEntity> {
        return CobblemonPokeController
    }

    override fun getForgeListener(): ForgeListener<Pokemon, PokemonEntity, *, *, *>? {
        return null
    }

    override fun onEnable() {
        super.onEnable()
        logger.info("§aShell version: ${SomeMethod.getMinecraftVersion()}")
        logger.info("§aMod version: Cobblemon-1.6.0 [Development and usage version]")
    }
}