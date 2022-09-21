package mc.leaf.modules.armory.targets;

import mc.leaf.modules.armory.generics.ShotTarget;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class EntityShotTarget implements ShotTarget {

    private final LivingEntity entity;
    private       Location     lastKnownLocation;

    public EntityShotTarget(LivingEntity entity) {

        this.entity = entity;
    }

    @Override
    public Location getTargetLocation() {

        if (this.getEntity().isDead()) {
            return null;
        }
        this.lastKnownLocation = this.getEntity().getEyeLocation();
        return this.getEntity().getEyeLocation();
    }

    @Override
    public Location getLastKnownLocation() {

        return this.lastKnownLocation;
    }

    @Override
    public Location getEffectiveLocation() {

        if (!this.getEntity().isDead()) {
            return this.getTargetLocation();
        }
        return this.getLastKnownLocation();
    }

    @Override
    public boolean isAvailable() {

        return !this.getEntity().isDead();
    }

    public LivingEntity getEntity() {

        return this.entity;
    }

}
