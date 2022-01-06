package mc.leaf.modules.weapons;

import mc.leaf.modules.weapons.data.entities.Ammunition;
import mc.leaf.modules.weapons.data.entities.Weapon;
import org.bukkit.Color;

public class WeaponGenerator {

    private static final String WEAPON_SOUND = "alfgard.gun";

    public static Weapon getTierOneRiffle() {
        Weapon weapon = new Weapon();

        weapon.setTier(1);
        weapon.setReach(60);
        weapon.setAmmo(12);
        weapon.setMaxAmmo(12);
        weapon.setBulletType("normal");
        weapon.setBulletPower(6);
        weapon.setBulletTrailColor(Color.RED);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Weapon getTierTwoRiffle() {
        Weapon weapon = new Weapon();

        weapon.setTier(2);
        weapon.setReach(60);
        weapon.setAmmo(16);
        weapon.setMaxAmmo(16);
        weapon.setBulletType("normal");
        weapon.setBulletPower(8);
        weapon.setBulletTrailColor(Color.AQUA);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Weapon getTierThreeRiffle() {
        Weapon weapon = new Weapon();

        weapon.setTier(3);
        weapon.setReach(60);
        weapon.setAmmo(28);
        weapon.setMaxAmmo(28);
        weapon.setBulletType("normal");
        weapon.setBulletPower(10);
        weapon.setBulletTrailColor(Color.WHITE);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Weapon getTierOneRocketLauncher() {
        Weapon weapon = new Weapon();

        weapon.setTier(1);
        weapon.setReach(40);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setBulletType("explosive");
        weapon.setBulletPower(2);
        weapon.setBulletTrailColor(Color.GREEN);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Weapon getTierTwoRocketLauncher() {
        Weapon weapon = new Weapon();

        weapon.setTier(2);
        weapon.setReach(50);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setBulletType("explosive");
        weapon.setBulletPower(3);
        weapon.setBulletTrailColor(Color.YELLOW);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Weapon getTierThreeRocketLauncher() {
        Weapon weapon = new Weapon();

        weapon.setTier(2);
        weapon.setReach(60);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setBulletType("explosive");
        weapon.setBulletPower(4);
        weapon.setBulletTrailColor(Color.YELLOW);
        weapon.setSoundCategory(WEAPON_SOUND);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) 250);
        weapon.setTimeReload((long) 2000);

        return weapon;
    }

    public static Ammunition getTierOneRiffleAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(1);
        ammunition.setType("normal");
        return ammunition;
    }

    public static Ammunition getTierTwoRiffleAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(2);
        ammunition.setType("normal");
        return ammunition;
    }

    public static Ammunition getTierThreeRiffleAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(3);
        ammunition.setType("normal");
        return ammunition;
    }

    public static Ammunition getTierOneRocketLauncherAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(1);
        ammunition.setType("explosive");
        return ammunition;
    }

    public static Ammunition getTierTwoRocketLauncherAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(2);
        ammunition.setType("explosive");
        return ammunition;
    }

    public static Ammunition getTierThreeRocketLauncherAmmo() {

        Ammunition ammunition = new Ammunition();
        ammunition.setTier(3);
        ammunition.setType("explosive");
        return ammunition;
    }


}
