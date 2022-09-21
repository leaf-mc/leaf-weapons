package mc.leaf.modules.armory.entities.bullets;

import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.generics.Bullet;
import mc.leaf.modules.armory.generics.ShotTarget;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class RifleBullet extends Bullet {

    private static final double           PARTICLES_SUMMON_DISTANCE = 2.0;
    private final        Collection<UUID> hitEntities               = new HashSet<>();

    public RifleBullet(Weapon weapon, Player player, ShotTarget target) {

        super(weapon, player, target);
    }

    public RifleBullet(Weapon weapon, Location origin, Player player, ShotTarget target) {

        super(weapon, origin, player, target);
    }

    @Override
    public void tick() {

        if (!this.getTarget().isAvailable() && this.getTarget().getLastKnownLocation() == null) {
            // Fail-safe. Rifle should be instant, but we never know.
            this.setAlive(false);
            return;
        }

        World  world    = this.getTarget().getTargetLocation().getWorld();
        double distance = this.getOrigin().distance(this.getCurrentLocation());

        while (distance < this.getWeapon().getReach()) {
            distance = this.getOrigin().distance(this.getCurrentLocation());
            this.setCurrentLocation(this.getCurrentLocation().add(this.getDirection()));

            this.getCurrentLocation().getNearbyEntities(1, 1, 1).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> !(entity instanceof ArmorStand))
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> !entity.equals(this.getPlayer()))
                .filter(entity -> !this.hitEntities.contains(entity.getUniqueId()))
                .filter(livingEntity -> !livingEntity.isDead())
                .filter(entity -> entity.getBoundingBox().contains(this.getCurrentLocation().toVector()))
                .forEach(entity -> {
                    // After the bounding box check, we should only have one entity,
                    // but maybe people are having fun stacking mobs...
                    entity.damage(this.getWeapon().getPower(), this.getPlayer());
                    entity.attack(this.getPlayer());

                    // Avoid the entity to be hit multiple times
                    this.hitEntities.add(entity.getUniqueId());
                });

            // Spawn the particle
            if (distance > PARTICLES_SUMMON_DISTANCE) {
                // The distance check is here to avoid spawn particle directly in front of the player.
                Particle.DustOptions options = new Particle.DustOptions(this.getWeapon().getTrailColor(), 1);
                world.spawnParticle(Particle.REDSTONE, this.getCurrentLocation(), 1, 0, 0, 0, 0, options);
            }
        }

        if (!this.hitEntities.isEmpty()) {
            this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }

        this.setAlive(false);
    }

    @Override
    public boolean needTick() {

        return false;
    }

}
