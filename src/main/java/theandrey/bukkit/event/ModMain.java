package theandrey.bukkit.event;

import java.util.logging.Logger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import theandrey.bukkit.event.util.asm.ASMAccessor;
import theandrey.bukkit.event.util.asm.CraftBukkitAccessor;

/**
 * Главный класс мода
 * @author TheAndrey
 */
@Mod(modid = ModMain.MOD_ID, name = ModMain.MOD_NAME, version = ModMain.MOD_VERSION, acceptedMinecraftVersions = "1.4.7")
public final class ModMain {

	public static final String MOD_ID = "BukkitEventsLib";
	public static final String MOD_NAME = "Bukkit Events Lib";
	public static final String MOD_VERSION = "1.4-legacy";
	public static final Logger logger = Logger.getLogger("BukkitEventsLib");

	static {
		FMLRelaunchLog.makeLog(logger.getName()); // Set as parent
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		if(FMLCommonHandler.instance().getSide() != Side.SERVER) {
			throw new RuntimeException("This is server mod!");
		}
	}

	@Mod.Init
	public void init(FMLInitializationEvent event) {
		try {
			logger.info("Creating ASMAccessor class...");
			Class<? extends CraftBukkitAccessor> clazz = ASMAccessor.makeAccessorClass();
			BukkitEventUtils.craftBukkitAccessor = clazz.newInstance();
			logger.info("Class successfully created.");
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor class", ex);
		}
	}

}
