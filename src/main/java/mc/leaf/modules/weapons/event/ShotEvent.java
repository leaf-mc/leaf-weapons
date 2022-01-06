package mc.leaf.modules.weapons.event;

import mc.leaf.modules.weapons.data.entities.Weapon;
import mc.leaf.modules.weapons.entities.BulletEntity;
import mc.leaf.modules.weapons.lib.BlockShotTarget;
import mc.leaf.modules.weapons.lib.EntityShotTarget;
import mc.leaf.modules.weapons.lib.ShotTarget;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ShotEvent extends Event implements Cancellable {

    private static final HandlerList  handlers = new HandlerList();
    private final        LivingEntity shooter;
    private final        Weapon       weapon;
    private final        Location     shotOrigin;
    private              ShotTarget   target;
    private              boolean      cancelled;

    public ShotEvent(LivingEntity shooter, Weapon weapon) {

        this.shooter    = shooter;
        this.weapon     = weapon;
        this.shotOrigin = this.shooter.getEyeLocation().clone();

        this.findShotTarget(weapon.getBulletType().equals("normal"));
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    public void findShotTarget(boolean sensitive) {

        Vector   movement           = this.shotOrigin.clone().getDirection().normalize().multiply(0.2);
        Location fakeBulletLocation = this.shotOrigin.clone();

        while (this.target == null && fakeBulletLocation.distance(shotOrigin) < this.weapon.getReach()) {
            fakeBulletLocation.add(movement);
            fakeBulletLocation
                    .getNearbyEntities(1, 1, 1) // Retrieve entities around the actual location
                    .stream() // Make this Entity list a stream of Entity
                    .filter(entity -> entity instanceof LivingEntity)
                    .map(entity -> (LivingEntity) entity)
                    .filter(livingEntity -> !livingEntity.getUniqueId().equals(this.shooter.getUniqueId()))
                    .filter(livingEntity -> {
                        if (sensitive) {
                            return livingEntity.getBoundingBox().contains(fakeBulletLocation.toVector());
                        }
                        return true;
                    })
                    .filter(livingEntity -> !livingEntity.isDead())
                    .findFirst()
                    .ifPresent(livingEntity -> this.target = new EntityShotTarget(livingEntity));

            if (this.target == null) {
                Block block = fakeBulletLocation.getBlock();
                if (block.getBoundingBox().contains(fakeBulletLocation.toVector()) && !block.isPassable()) {
                    this.target = new BlockShotTarget(block);
                }
            }
        }

        if (this.target == null) {
            // If we didn't hit anything after reaching the maximum distance of the weapon.
            this.target = new BlockShotTarget(fakeBulletLocation.getBlock());
        }
    }

    public void shot() {

        Vector   movement         = this.shotOrigin.clone().getDirection().normalize().multiply(0.1);
        double   distanceToTarget = this.getShotOrigin().distance(this.getTarget().getTargetLocation());
        Location bulletLocation   = this.shotOrigin.clone();
        double   distanceTraveled = 0;

        while (distanceTraveled < distanceToTarget) {
            bulletLocation.add(movement);
            distanceTraveled = this.getShotOrigin().distance(bulletLocation);

            if (distanceTraveled > 2) {
                Particle.DustOptions options = new Particle.DustOptions(weapon.getBulletTrailColor(), 1);
                bulletLocation.getWorld().spawnParticle(Particle.REDSTONE, bulletLocation, 1, 0, 0, 0, 0, options);
            }
        }

        if (this.getTarget() instanceof EntityShotTarget) {
            LivingEntity target = ((EntityShotTarget) this.getTarget()).getEntity();
            target.damage(weapon.getBulletPower(), this.getShooter());
        }
    }

    public ShotTarget getTarget() {

        return target;
    }

    public BulletEntity getBullet() {

        return new BulletEntity(this);
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins.
     *
     * @param cancel
     *         true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {

        cancelled = cancel;
    }

    public LivingEntity getShooter() {

        return shooter;
    }

    public Weapon getWeapon() {

        return weapon;
    }

    public Location getShotOrigin() {

        return shotOrigin;
    }

}
