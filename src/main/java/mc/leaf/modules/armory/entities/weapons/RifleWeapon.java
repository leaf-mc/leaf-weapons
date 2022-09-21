package mc.leaf.modules.armory.entities.weapons;

import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.entities.bullets.RifleBullet;
import mc.leaf.modules.armory.generics.Bullet;
import mc.leaf.modules.armory.generics.ShotTarget;
import mc.leaf.modules.armory.generics.WeaponWrapper;
import mc.leaf.modules.armory.targets.EntityShotTarget;
import mc.leaf.modules.armory.targets.LocationShotTarget;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RifleWeapon extends WeaponWrapper {

    public RifleWeapon(Player player, Weapon weapon) {

        super(player, weapon);
    }

    @Override
    public Bullet shotAt(ShotTarget target) {

        return new RifleBullet(this.getWeapon(), this.getPlayer(), target);
    }

    @Override
    public void doShotFeedback(Bullet bullet) {

        Player player = bullet.getPlayer();
        World  world  = player.getWorld();

        world.playSound(player, Sound.ENTITY_BLAZE_SHOOT, 10, 1);
        world.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 2, 2);
    }

    @Override
    public List<ShotTarget> findTargets() {

        List<ShotTarget> targets = new ArrayList<>();

        Location startingSpot = this.getPlayer().getEyeLocation().clone();
        Location seekingPoint = this.getPlayer().getEyeLocation().clone();
        double   distance     = seekingPoint.distance(startingSpot);

        while (distance < this.getWeapon().getReach() && targets.isEmpty()) {
            seekingPoint.add(this.getDirection());

            distance = seekingPoint.distance(startingSpot);

            Optional<LivingEntity> any = seekingPoint.getNearbyEntities(1, 1, 1).stream()
                                                     .filter(entity -> entity instanceof LivingEntity)
                                                     .filter(entity -> !(entity instanceof ArmorStand))
                                                     .map(entity -> (LivingEntity) entity)
                                                     .filter(entity -> !entity.equals(this.getPlayer()))
                                                     .filter(livingEntity -> !livingEntity.isDead())
                                                     .filter(entity -> entity.getBoundingBox()
                                                                             .contains(seekingPoint.toVector()))
                                                     .findAny();

            any.ifPresent(entity -> {
                ShotTarget target = new EntityShotTarget(entity);
                targets.add(target);
            });
        }

        if (targets.isEmpty()) {
            targets.add(new LocationShotTarget(seekingPoint));
        }

        return targets;
    }

}
