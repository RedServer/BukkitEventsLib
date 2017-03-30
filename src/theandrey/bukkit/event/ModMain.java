package theandrey.bukkit.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
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
	public static final String MOD_VERSION = "1.1";
	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) throw new RuntimeException("This is server mod!");
		logger = event.getModLog();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		try {
			logger.info("Creating ASMAccessor class...");
			Class<? extends CraftBukkitAccessor> clazz = ASMAccessor.makeAccessorClass();
			BukkitEventUtils.craftBukkitAccessor = clazz.newInstance();
			logger.info("Class succefully created.");
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor class", ex);
		}
	}

}
