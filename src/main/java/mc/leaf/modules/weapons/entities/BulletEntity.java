package mc.leaf.modules.weapons.entities;

import mc.leaf.modules.weapons.event.ShotEvent;
import mc.leaf.modules.weapons.lib.ShotTarget;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

        bulletLocation.getWorld().playSound(bulletLocation, Sound.ENTITY_CREEPER_HURT, 2, 0);

        Vector   movementDirection = this.target.getTargetLocation().clone().subtract(this.bulletLocation).toVector()
                .normalize().multiply(0.4);
        Location newLocation       = bulletLocation.add(movementDirection);
        this.lifetime -= bulletLocation.distance(newLocation);
        bulletLocation = newLocation;

        if (bulletLocation.distance(event.getShotOrigin()) > 2) {
            Particle.DustOptions dust = new Particle.DustOptions(this.event.getWeapon().getBulletTrailColor(), 3);
            bulletLocation.getWorld().spawnParticle(Particle.REDSTONE, bulletLocation, 1, 0, 0, 0, 0, dust);
        }


        Optional<LivingEntity> optionalEntity = bulletLocation.getNearbyEntities(1, 1, 1) // Retrieve entities around the actual location
                .stream() // Make this Entity list a stream of Entity
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(livingEntity -> !livingEntity.getUniqueId().equals(this.event.getShooter().getUniqueId()))
                .filter(livingEntity -> livingEntity.getBoundingBox().contains(bulletLocation.toVector()))
                .filter(livingEntity -> !livingEntity.isDead())
                .findFirst();

        Block block = bulletLocation.getBlock();

        if (optionalEntity.isPresent()) {
            this.makeBulletGoKaboom();
        } else if (!block.isPassable() && block.getBoundingBox().contains(bulletLocation.toVector())) {
            this.makeBulletGoKaboom();
        } else if (lifetime == 0 || bulletLocation.distance(this.target.getTargetLocation()) < 1) {
            this.makeBulletGoKaboom();
        }
    }

    private void makeBulletGoKaboom() {

        TNTPrimed tnt = bulletLocation.getWorld().spawn(bulletLocation, TNTPrimed.class);
        tnt.setYield(this.event.getWeapon().getBulletPower());
        tnt.setFuseTicks(0);
        lifetime = 0;
    }

    public boolean isValid() {

        return this.lifetime > 0;
    }

}
