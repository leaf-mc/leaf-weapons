package mc.leaf.modules.armory;

import mc.leaf.core.interfaces.ILeafCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeafArmory extends JavaPlugin {

    public static final String PREFIX = "§l[§aLeaf§bWeapons§r§l]§r";

    @Override
    public void onEnable() {

        Plugin plugin = Bukkit.getPluginManager().getPlugin("LeafCore");
        if (plugin instanceof ILeafCore core) {
            new ArmoryModule(this, core);
        } else {
            this.getLogger().severe("Unable to find LeafCore instance.");
        }
    }

}
