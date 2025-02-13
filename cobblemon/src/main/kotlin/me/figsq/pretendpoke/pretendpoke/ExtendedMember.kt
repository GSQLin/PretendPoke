package me.figsq.pretendpoke.pretendpoke

import me.fullidle.ficore.ficore.common.SomeMethod
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import org.bukkit.Bukkit
import org.bukkit.World
import java.lang.reflect.Method

val GET_ENTITY_METHOD: Method by lazy {
    Class.forName("org.bukkit.craftbukkit.${SomeMethod.getNmsVersion()}.entity.CraftEntity")
        .getDeclaredMethod("getEntity", Bukkit.getServer().javaClass, Entity::class.java).apply {
            this.isAccessible = true
        }
}

fun Entity.bukkitEntity(): org.bukkit.entity.Entity {
    return GET_ENTITY_METHOD.invoke(null, Bukkit.getServer(), this) as org.bukkit.entity.Entity
}

fun World.level(): Level {
    return Class.forName("org.bukkit.craftbukkit.${SomeMethod.getNmsVersion()}.CraftWorld")
        .getDeclaredMethod("getHandle").invoke(this) as Level
}

fun org.bukkit.entity.Entity.getHandle(): Any {
    return this.javaClass.getDeclaredMethod("getHandle").invoke(this)
}
