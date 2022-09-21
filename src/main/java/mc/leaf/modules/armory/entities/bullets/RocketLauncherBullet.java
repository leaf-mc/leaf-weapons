package mc.leaf.modules.armory.entities.bullets;

import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.generics.Bullet;
import mc.leaf.modules.armory.generics.ShotTarget;
import mc.leaf.modules.armory.targets.EntityShotTarget;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RocketLauncherBullet extends Bullet {

    private static final double PARTICLES_SUMMON_DISTANCE = 2.0;
    private              double ttl;

    public RocketLauncherBullet(Weapon weapon, Player player, ShotTarget target) {

        super(weapon, player, target);
        this.ttl = weapon.getReach();
    }

    public RocketLauncherBullet(Weapon weapon, Location origin, Player player, ShotTarget target) {

        super(weapon, origin, player, target);
        this.ttl = weapon.getReach();
    }

    @Override
    public void tick() {

        if (!this.getTarget().isAvailable() && this.getTarget().getLastKnownLocation() == null) {
            this.setAlive(false, false);
            return;
        }

        this.redirect();
        World    world          = this.getPlayer().getWorld();
        Location futureLocation = this.getCurrentLocation().clone().add(this.getDirection());
        this.ttl -= this.getCurrentLocation().distance(futureLocation);
        this.setCurrentLocation(futureLocation);

        if (this.ttl <= 0 || (this.getTarget() instanceof EntityShotTarget target && !target.isAvailable())) {
            this.setAlive(false, false);
            return;
        }

        double distance = this.getOrigin().distance(this.getCurrentLocation());

        this.getCurrentLocation().getNearbyEntities(1, 1, 1).stream()
            .filter(entity -> entity instanceof LivingEntity)
            .filter(entity -> !(entity instanceof ArmorStand))
            .map(entity -> (LivingEntity) entity)
            .filter(entity -> !entity.equals(this.getPlayer()))
            .filter(livingEntity -> !livingEntity.isDead())
            .filter(entity -> entity.getBoundingBox().contains(this.getCurrentLocation().toVector()))
            .findAny().ifPresent(entity -> this.setAlive(false, true));

        // Audio & Visual feedback
        world.playSound(this.getCurrentLocation(), Sound.ENTITY_CREEPER_HURT, 0.5f, 0);

        // Spawn the particle
        if (distance > PARTICLES_SUMMON_DISTANCE) {
            // The distance check is here to avoid spawn particle directly in front of the player.

            Particle.DustOptions options = new Particle.DustOptions(this.getWeapon().getTrailColor(), 1);
            world.spawnParticle(Particle.REDSTONE, this.getCurrentLocation(), 1, 0, 0, 0, 0, options);
        }

        // Do we hit a block ?
        Block block = this.getCurrentLocation().getBlock();

        if (!block.isPassable() && block.getBoundingBox().contains(this.getCurrentLocation().toVector())) {
            this.setAlive(false, false);
        }

    }

    public void setAlive(boolean alive, boolean ding) {

        this.setAlive(false);

        // Hook for the explosion !
        if (!alive) {
            Player player = this.getPlayer();
            World  world  = this.getCurrentLocation().getWorld();
            int    power  = this.getWeapon().getPower();
            int    delta  = power / 6;

            world.createExplosion(player, this.getCurrentLocation(), power, false);
            world.spawnParticle(Particle.EXPLOSION_NORMAL, this.getCurrentLocation(), power * 10, delta, delta, delta);
            world.spawnParticle(Particle.EXPLOSION_LARGE, this.getCurrentLocation(), power * 10, delta, delta, delta);

            if (ding) {
                this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            }
        }
    }

    @Override
    public boolean needTick() {

        return true;
    }

}
