package mc.leaf.modules.armory.generics;

import org.bukkit.Location;

public interface ShotTarget {

    Location getTargetLocation();

    Location getLastKnownLocation();

    Location getEffectiveLocation();

    boolean isAvailable();

}
