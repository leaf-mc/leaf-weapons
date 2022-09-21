package mc.leaf.modules.armory.generics;

import mc.leaf.modules.armory.entities.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class WeaponWrapper {

    private final Player player;
    private final Weapon weapon;
    private final Vector direction;

    public WeaponWrapper(Player player, Weapon weapon) {

        this.player    = player;
        this.weapon    = weapon;
        this.direction = player.getEyeLocation().getDirection().normalize().multiply(0.2);
    }

    public Weapon getWeapon() {

        return this.weapon;
    }

    public Player getPlayer() {

        return this.player;
    }

    public Vector getDirection() {

        return this.direction;
    }

    public abstract List<ShotTarget> findTargets();

    public abstract Bullet shotAt(ShotTarget target);

    public abstract void doShotFeedback(Bullet bullet);

}
