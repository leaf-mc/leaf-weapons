package mc.leaf.modules.armory.entities.weapons;

import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.generics.ShotTarget;
import mc.leaf.modules.armory.targets.EntityShotTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SpecialRocketLauncherWeapon extends RocketLauncherWeapon {

    public SpecialRocketLauncherWeapon(Player player, Weapon weapon) {

        super(player, weapon);
    }

    @Override
    public List<ShotTarget> findTargets() {

        List<ShotTarget> targets = super.findTargets();

        if (targets.size() == 1) {
            ShotTarget target = targets.get(0);
            if (target instanceof EntityShotTarget entityTarget) {

                LivingEntity entity = entityTarget.getEntity();
                EntityType   type   = entity.getType();
                int          reach  = this.getWeapon().getReach();

                return this.getPlayer().getNearbyEntities(reach, reach, reach).stream()
                           .filter(en -> en instanceof LivingEntity)
                           .map(en -> (LivingEntity) en)
                           .filter(en -> en.getType() == type)
                           .map(EntityShotTarget::new)
                           .collect(Collectors.toList());
            }
        }
        return targets;
    }

}
