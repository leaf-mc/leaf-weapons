package mc.leaf.modules.weapons.lib;

import org.bukkit.Location;
import org.bukkit.block.Block;

public record BlockShotTarget(Block block) implements ShotTarget {

    @Override
    public Location getTargetLocation() {

        return this.block.getLocation();
    }

}
