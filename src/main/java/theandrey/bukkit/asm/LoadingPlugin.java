package theandrey.bukkit.asm;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("theandrey.bukkit.asm.")
public final class LoadingPlugin implements IFMLLoadingPlugin {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"theandrey.bukkit.asm.BukkitInventoryTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// NO-OP
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
