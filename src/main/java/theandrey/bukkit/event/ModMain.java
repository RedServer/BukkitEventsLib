package theandrey.bukkit.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theandrey.bukkit.event.listener.WorldEventHooks;
import theandrey.bukkit.event.util.asm.ASMAccessor;
import theandrey.bukkit.event.util.asm.CraftBukkitAccessor;

/**
 * Главный класс мода
 * @author TheAndrey
 */
@Mod(modid = ModMain.MOD_ID, name = ModMain.MOD_NAME, version = ModMain.MOD_VERSION, acceptableRemoteVersions = "*")
public final class ModMain {

	public static final String MOD_ID = "BukkitEventsLib";
	public static final String MOD_NAME = "Bukkit Events Lib";
	public static final String MOD_VERSION = "1.6";
	public static final Logger logger = LogManager.getLogger("BukkitEventsLib");

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide() != Side.SERVER) {
			throw new RuntimeException("This is server mod!");
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		try {
			logger.info("Creating ASMAccessor class...");
			Class<? extends CraftBukkitAccessor> clazz = ASMAccessor.makeAccessorClass();
			BukkitEventUtils.craftBukkitAccessor = clazz.newInstance();
			logger.info("Class successfully created.");
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor class", ex);
		}

		MinecraftForge.EVENT_BUS.register(new WorldEventHooks());
	}

}
