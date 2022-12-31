package me.gsqlin.pretendpoke;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PixelUtil{
    /*pixelmonMod版本*/
    public static String pixelVersion = Pixelmon.getVersion();
    /*获取版本*/
    public static String bukkitVersion = Bukkit.getBukkitVersion().contains("1.12.2")? "1.12.2" : "1.16.5";
    /*craftitem的class*/
    public static Class<?> craftItemStackClass;
    static {
        try {
            craftItemStackClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                    Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack"):
                    Class.forName("org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*craftentity的class*/
    public static Class<?> craftEntityClass;
    static {
        try {
            craftEntityClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                    Class.forName("org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity"):
                    Class.forName("org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*craftworld的class*/
    public static Class<?> craftWorldClass;
    static {
        try {
            craftWorldClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                    Class.forName("org.bukkit.craftbukkit.v1_12_R1.CraftWorld"):
                    Class.forName("org.bukkit.craftbukkit.v1_16_R3.CraftWorld");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*判断是否有指定class也是获取class*/
    public static Class<?> getClass(String c){
        try {
            return Class.forName(c);
        } catch (Exception ignored) {
            return null;
        }
    }
    /*注册不了事件发送提示并卸载插件*/
    public static void sendNotForgeEventError(Plugin plugin){
        plugin.getLogger().info("§c无法判断服务器的版本");
        plugin.getLogger().info("§c所以我决定卸载自己~请联系作者");
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
    /*注册事件*/
    public static void registerForgeEvent(Plugin plugin){
        if (PixelUtil.getClass("catserver.api.bukkit.event.ForgeEvent")!=null) {
            plugin.getServer().getPluginManager().registerEvents(new me.gsqlin.pretendpoke.forgeEvents.CatEventO(),plugin);
        } else if (PixelUtil.getClass("catserver.api.bukkit.ForgeEventV2")!=null) {
            plugin.getServer().getPluginManager().registerEvents(new me.gsqlin.pretendpoke.forgeEvents.CatEventM(),plugin);
        }else{
            PixelUtil.sendNotForgeEventError(plugin);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
    /*获取PlayerPartyStorage*/
    public static Object getPlayerPartyStorage(Object p) throws NoSuchFieldException, IllegalAccessException {
        if (bukkitVersion.equalsIgnoreCase("1.12.2")){
            return getPlayerPartyStorage((net.minecraft.entity.player.EntityPlayerMP)p);
        }else{
            return getPlayerPartyStorage((net.minecraft.entity.player.ServerPlayerEntity)p);
        }
    }
    //不要用导入,要用静态
    public static com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage getPlayerPartyStorage(net.minecraft.entity.player.ServerPlayerEntity p){
        return com.pixelmonmod.pixelmon.api.storage.StorageProxy.getParty(p);
    }
    public static com.pixelmonmod.pixelmon.storage.PlayerPartyStorage getPlayerPartyStorage(net.minecraft.entity.player.EntityPlayerMP p) throws NoSuchFieldException, IllegalAccessException {
        Class<?> pixelClass = Pixelmon.class;
        com.pixelmonmod.pixelmon.api.storage.IStorageManager storageM;
        storageM = (com.pixelmonmod.pixelmon.api.storage.IStorageManager) pixelClass.getField("storageManager").get(pixelClass);
        return storageM.getParty(p);
    }
    /*获取玩家*/
    public static Player getPlayer(net.minecraft.entity.player.ServerPlayerEntity serverPlayerEntity){
        return serverPlayerEntity.getBukkitEntity().getPlayer();
    }
    public static Player getPlayer(net.minecraft.entity.player.EntityPlayerMP entityPlayerMP){
        return entityPlayerMP.getBukkitEntity().getPlayer();
    }
    public static Object getPlayer(Player p) throws InvocationTargetException, IllegalAccessException {
        return getHandle(craftEntityClass.cast(p));
    }
    /*
    获取一个对象里边的变量,如果变量类似arraylist这类的你修改了,对象内的也一样,他们是同一个变量
    */
    public static Object getField(Class<?> c,Object o,String name) {
        Object ot = null;
        Field f = null;
        while (f == null){
            try {
                f = c.getDeclaredField(name);
            }catch (Exception e){
                if (c.getSuperclass() == null) break;
                c = c.getSuperclass();
            }
        }
        if (f != null){
            f.setAccessible(true);
            try {
                ot = f.get(o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return ot;
    }
    //获取一个对象中的方法(自动往父级上找到为止(最后没有则返回null))
    public static Method getMethod(Class<?> c,String name,Class<?>... a) {
        Method method = null;
        while (method == null){
            try {
                method = c.getDeclaredMethod(name, a);
            }catch (Exception e){
                if (c.getSuperclass() == null) break;
                c = c.getSuperclass();
            }
        }
        if (method != null) method.setAccessible(true);
        return method;
    }
    //对一个对象中修改里边的变量
    public static void setVariable(Class<?> c,Object o,String name,Object value) {
        Field f = null;
        while (f == null){
            try {
                f = c.getDeclaredField(name);
            }catch (Exception e){
                if (c.getSuperclass() == null) break;
                c = c.getSuperclass();
            }
        }
        if (f != null) {
            f.setAccessible(true);
            try {
                f.set(o,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //模仿CraftItemStack类中的方法asBukkitCopy
    public static ItemStack asBukkitCopy(net.minecraft.item.ItemStack itemStack) throws InvocationTargetException, IllegalAccessException {
        Method met = getMethod(craftItemStackClass,"asBukkitCopy",net.minecraft.item.ItemStack.class);
        return (ItemStack) met.invoke(craftItemStackClass, itemStack);
    }
    /*模仿上方同类中的asNMSCopy*/
    public static net.minecraft.item.ItemStack asNMSCopy(ItemStack itemStack) throws InvocationTargetException, IllegalAccessException {
        Method method = getMethod(craftItemStackClass,"asNMSCopy",ItemStack.class);
        return (net.minecraft.item.ItemStack) method.invoke(craftItemStackClass,itemStack);
    }
    /*获取minecraft中的world*/
    public static World getWorld(org.bukkit.World world) throws InvocationTargetException, IllegalAccessException {
        return (World) getHandle(craftWorldClass.cast(world));
    }
    /*所有继承了bukkitapi中的entity的对象都可以用*/
    public static Entity getBukkitEntity(Object entity) throws InvocationTargetException, IllegalAccessException {
        Method method = getMethod(entity.getClass(),"getBukkitEntity");
        return (Entity) method.invoke(entity);
    }
    /*获取构建器*/
    public static Constructor getConstructor(Class<?> c, Class<?>... a) throws NoSuchMethodException {
        return c.getDeclaredConstructor(a);
    }
    /*所有Craft...都可以用的getHandle*/
    public static Object getHandle(Object o) throws InvocationTargetException, IllegalAccessException {
        return getMethod(o.getClass(),"getHandle").invoke(o);
    }
    /*获取Minecraft中的Entity*/
    public static net.minecraft.entity.Entity getMinecraftEntity(org.bukkit.entity.Entity entity) throws InvocationTargetException, IllegalAccessException {
        Object o = craftEntityClass.cast(entity);
        return (net.minecraft.entity.Entity) getHandle(o);
    }
    /*弄只宝可梦,并没有被生成到世界*/
    public static Pokemon createPokemon(String... name) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> es = bukkitVersion.equalsIgnoreCase("1.12.2")?
                Class.forName("com.pixelmonmod.pixelmon.enums.EnumSpecies"):null;
        Class<?> c = bukkitVersion.equalsIgnoreCase("1.12.2")?
                Class.forName("com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory"):
                Class.forName("com.pixelmonmod.api.pokemon.PokemonSpecificationProxy");
        Method create = getMethod(c,"create",
                bukkitVersion.equalsIgnoreCase("1.12.2")?es:String[].class);
        Object o = create.invoke(bukkitVersion.equalsIgnoreCase("1.12.2")?
                        getField(Pixelmon.class,Pixelmon.class,"pokemonFactory"):c
                ,bukkitVersion.equalsIgnoreCase("1.12.2")?
                        getMethod(es,"valueOf",String.class).invoke(es,name[0]):name);
        if (bukkitVersion.equalsIgnoreCase("1.12.2")){
            return (Pokemon) o;
        }else{
            return (Pokemon) getMethod(o.getClass(),"create").invoke(o);
        }
    }
    /*生成宝可梦并获取宝可梦,类型是PixelmonEntity或者是EntityPixelmon,如果世界已经有这个宝可梦了则是直接获取*/
    public static Object getOrSpawnPixelmon(Pokemon pokemon,Player player) throws InvocationTargetException, IllegalAccessException {
        World world = getWorld(player.getWorld());
        Method method = getMethod(pokemon.getClass(),"getOrSpawnPixelmon",World.class,double.class,double.class,double.class);
        return method.invoke(pokemon,world,player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ());
    }
    /*从PixelmonEntity或者靠不说了,获取到Pokemon*/
    public static Pokemon getPokemonData(Object pixelmonEntity) throws InvocationTargetException, IllegalAccessException {
        return (Pokemon) getMethod(pixelmonEntity.getClass(),bukkitVersion.equalsIgnoreCase("1.12.2")?
                "getPokemonData":"getPokemon").invoke(pixelmonEntity);
    }
}