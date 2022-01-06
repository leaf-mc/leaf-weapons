package mc.leaf.modules.weapons.lib;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityShotTarget implements ShotTarget {

    private final LivingEntity entity;
    private       Location     location;

    public EntityShotTarget(LivingEntity entity) {

        this.entity   = entity;
        this.location = entity.getLocation();
    }

    public LivingEntity getEntity() {

        return entity;
    }

    @Override
    public Location getTargetLocation() {
        // Refresh entity location.
        Entity entity = this.location.getWorld().getEntity(this.entity.getUniqueId());

        if (entity != null) {
            this.location = entity.getLocation();
        }

        return this.location;
    }

}
