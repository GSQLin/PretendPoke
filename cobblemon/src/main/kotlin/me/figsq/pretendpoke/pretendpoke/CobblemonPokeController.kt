package me.figsq.pretendpoke.pretendpoke

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import me.figsq.pretendpoke.pretendpoke.api.PokeController
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

object CobblemonPokeController: PokeController<Species, Pokemon, PokemonEntity>() {
    override fun getPokeEntity(pokemon: Pokemon): PokemonEntity? {
        return pokemon.entity
    }

    override fun getOrSpawnPokeEntity(pokemon: Pokemon, parent: Entity): PokemonEntity {
        var pokeEntity = this.getPokeEntity(pokemon)
        if (pokeEntity == null) {
            val location = parent.location
            pokeEntity = pokemon.sendOut(location.world!!.level() as ServerLevel, Vec3(location.x, location.y, location.z), null)
        }
        return pokeEntity!!
    }

    override fun getPokemon(pokeEntity: PokemonEntity): Pokemon {
        return pokeEntity.pokemon
    }

    override fun asBukkitEntity(pokeEntity: PokemonEntity): Entity {
        return pokeEntity.bukkitEntity()
    }

    override fun asPokeEntity(entity: Entity): PokemonEntity {
        return entity.getHandle() as? PokemonEntity ?: throw IllegalArgumentException("Entity is not a PokemonEntity")
    }

    override fun hideEntityForPlayer(entity: Entity, target: Player) {
        target.hideEntity(PretendPokePlugin.getInstance(),entity)
        target.uniqueId.getPlayer()!!.connection.send(ClientboundRemoveEntitiesPacket(target.entityId))
    }

    override fun isPokeEntity(entity: Entity): Boolean {
        return entity.getHandle() is PokemonEntity
    }

    override fun getSpecies(speciesName: String): Species? {
        return try {
            PokemonSpecies.getByName(speciesName.lowercase())
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllSpecies(): MutableList<Species> {
        return PokemonSpecies.implemented
    }

    override fun getOwner(pokemon: Pokemon): OfflinePlayer? {
        return pokemon.getOwnerPlayer()?.let {
            Bukkit.getOfflinePlayer(it.uuid)
        }
    }

    override fun getSpeciesName(species: Species): String {
        return species.name
    }

    override fun getUUID(pokemon: Pokemon): UUID {
        return pokemon.uuid
    }

    override fun removeAI(pokeEntity: PokemonEntity) {
        (pokeEntity.bukkitEntity() as LivingEntity).setAI(false)
    }

    override fun createPokemon(species: Species): Pokemon {
        return species.create()
    }
}