package mc.leaf.modules.armory.targets;

import mc.leaf.modules.armory.generics.ShotTarget;
import org.bukkit.Location;

public record LocationShotTarget(Location location) implements ShotTarget {

    @Override
    public Location getTargetLocation() {

        return this.location;
    }

    @Override
    public Location getLastKnownLocation() {

        return this.location;
    }

    @Override
    public Location getEffectiveLocation() {

        return this.location;
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

}
