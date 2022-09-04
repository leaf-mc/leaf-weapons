package mc.leaf.modules.weapons.entities;

import mc.leaf.modules.weapons.event.ShotEvent;
import mc.leaf.modules.weapons.lib.ShotTarget;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.Optional;

public class BulletEntity {

    private final ShotTarget target;
    private final ShotEvent  event;
    private       double     lifetime;

    private Location bulletLocation;


    public BulletEntity(ShotEvent event) {

        this.target = event.getTarget();
        this.event  = event;

        this.lifetime       = event.getWeapon().getReach();
        this.bulletLocation = this.event.getShotOrigin().clone();
    }

    public void tick() {

        this.bulletLocation.getWorld().playSound(this.bulletLocation, Sound.ENTITY_CREEPER_HURT, 2, 0);

        Vector   movementDirection = this.target.getTargetLocation().clone().subtract(this.bulletLocation).toVector()
                .normalize().multiply(0.4);
        Location newLocation       = this.bulletLocation.add(movementDirection);
        this.lifetime -= this.bulletLocation.distance(newLocation);
        this.bulletLocation = newLocation;

        if (this.bulletLocation.distance(this.event.getShotOrigin()) > 2) {
            Particle.DustOptions dust = new Particle.DustOptions(this.event.getWeapon().getBulletTrailColor(), 3);
            this.bulletLocation.getWorld().spawnParticle(Particle.REDSTONE, this.bulletLocation, 1, 0, 0, 0, 0, dust);
        }


        Optional<LivingEntity> optionalEntity = this.bulletLocation.getNearbyEntities(1, 1, 1) // Retrieve entities around the actual location
                .stream() // Make this Entity list a stream of Entity
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(livingEntity -> !livingEntity.getUniqueId().equals(this.event.getShooter().getUniqueId()))
                .filter(livingEntity -> livingEntity.getBoundingBox().contains(this.bulletLocation.toVector()))
                .filter(livingEntity -> !livingEntity.isDead())
                .findFirst();

        Block block = this.bulletLocation.getBlock();

        if (optionalEntity.isPresent()) {
            this.makeBulletGoKaboom();
        } else if (!block.isPassable() && block.getBoundingBox().contains(this.bulletLocation.toVector())) {
            this.makeBulletGoKaboom();
        } else if (this.lifetime == 0 || this.bulletLocation.distance(this.target.getTargetLocation()) < 1) {
            this.makeBulletGoKaboom();
        }
    }

    private void makeBulletGoKaboom() {

        TNTPrimed tnt = this.bulletLocation.getWorld().spawn(this.bulletLocation, TNTPrimed.class);
        Integer   bulletPower = this.event.getWeapon().getBulletPower();
        int       delta = bulletPower / 6;
        tnt.setYield(bulletPower);
        tnt.setFuseTicks(0);
        this.lifetime = 0;

        World world = this.bulletLocation.getWorld();
        world.spawnParticle(Particle.EXPLOSION_NORMAL, this.bulletLocation, bulletPower * 10, delta, delta, delta);
        world.spawnParticle(Particle.EXPLOSION_LARGE, this.bulletLocation, bulletPower * 10, delta, delta, delta);
    }

    public boolean isValid() {

        return this.lifetime > 0;
    }

}
