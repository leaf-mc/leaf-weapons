package mc.leaf.modules.armory.entities.weapons;

import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.entities.bullets.RocketLauncherBullet;
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

public class RocketLauncherWeapon extends WeaponWrapper {

    public RocketLauncherWeapon(Player player, Weapon weapon) {

        super(player, weapon);
    }

    @Override
    public Bullet shotAt(ShotTarget target) {

        return new RocketLauncherBullet(this.getWeapon(), this.getPlayer(), target);
    }

    @Override
    public void doShotFeedback(Bullet bullet) {

        Player player = bullet.getPlayer();
        World  world  = player.getWorld();

        world.playSound(player, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 2, 2);
    }

    @Override
    public List<ShotTarget> findTargets() {

        List<ShotTarget> targets = new ArrayList<>();

        Location startingSpot = this.getPlayer().getLocation().clone();
        Location seekingPoint = this.getPlayer().getLocation().clone();
        double   distance     = startingSpot.distance(seekingPoint);

        while (distance < this.getWeapon().getReach() && targets.isEmpty()) {
            seekingPoint.add(this.getDirection());

            distance = startingSpot.distance(seekingPoint);

            Optional<LivingEntity> any = seekingPoint.getNearbyEntities(1, 1, 1).stream()
                                                     .filter(entity -> entity instanceof LivingEntity)
                                                     .filter(entity -> !(entity instanceof ArmorStand))
                                                     .map(entity -> (LivingEntity) entity)
                                                     .filter(entity -> !entity.equals(this.getPlayer()))
                                                     .filter(livingEntity -> !livingEntity.isDead())
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
