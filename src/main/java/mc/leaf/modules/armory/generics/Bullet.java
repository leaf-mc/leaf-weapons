package mc.leaf.modules.armory.generics;

import mc.leaf.modules.armory.entities.Weapon;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class Bullet {

    private final Weapon     weapon;
    private final Location   origin;
    private final Player     player;
    private final ShotTarget target;
    private       Location   currentLocation;
    private       Vector     direction;
    private       float      speed;
    private       boolean    alive = true;

    public Bullet(Weapon weapon, Player player, ShotTarget target) {

        this(weapon, player.getEyeLocation(), player, target);
    }

    public Bullet(Weapon weapon, Location origin, Player player, ShotTarget target) {

        this.weapon = weapon;
        this.player = player;
        this.target = target;

        // Cloning just in case
        this.origin          = origin.clone();
        this.currentLocation = origin.clone();

        this.speed = 0.2f;
        this.redirect();
    }

    public void redirect() {

        this.direction = this.getTarget()
                             .getEffectiveLocation()
                             .clone()
                             .subtract(this.getCurrentLocation())
                             .toVector()
                             .normalize()
                             .multiply(this.getSpeed());
    }

    public Weapon getWeapon() {

        return this.weapon;
    }

    public Location getOrigin() {

        return this.origin;
    }

    public Player getPlayer() {

        return this.player;
    }

    public ShotTarget getTarget() {

        return this.target;
    }

    public Vector getDirection() {

        return this.direction;
    }

    public void setDirection(Vector direction) {

        this.direction = direction;
    }

    public float getSpeed() {

        return this.speed;
    }

    public void setSpeed(float speed) {

        this.speed = speed;
    }

    public Location getCurrentLocation() {

        return this.currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {

        this.currentLocation = currentLocation;
    }

    public double getDistanceFromTarget() {

        return this.getCurrentLocation().distance(this.getTarget().getEffectiveLocation());
    }

    public boolean isAlive() {

        return this.alive;
    }

    public void setAlive(boolean alive) {

        this.alive = alive;
    }

    public abstract boolean needTick();

    public abstract void tick();

}
