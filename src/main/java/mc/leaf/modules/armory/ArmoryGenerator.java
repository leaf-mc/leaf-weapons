package mc.leaf.modules.armory;

import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.entities.Ammunition;
import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.enums.AmmoType;
import mc.leaf.modules.armory.enums.WeaponType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ArmoryGenerator {

    private ArmoryGenerator() {}

    private static ItemStack getWeapon(ILeafModule module, Weapon weapon) {

        ItemStack stack = new ItemStack(Material.CROSSBOW, 1);
        ItemMeta  meta  = stack.getItemMeta();
        Weapon.persist(module, meta, weapon);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getTierOneRiffle(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(1);
        weapon.setReach(60);
        weapon.setAmmo(12);
        weapon.setMaxAmmo(12);
        weapon.setType(WeaponType.RIFLE);
        weapon.setPower(6);
        weapon.setTrailColor(Color.RED);
        weapon.setTimeShot(5);
        weapon.setTimeReload(40);

        return getWeapon(module, weapon);
    }

    public static ItemStack getTierTwoRiffle(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(2);
        weapon.setReach(60);
        weapon.setAmmo(16);
        weapon.setMaxAmmo(16);
        weapon.setType(WeaponType.RIFLE);
        weapon.setPower(8);
        weapon.setTrailColor(Color.AQUA);
        weapon.setTimeShot(5);
        weapon.setTimeReload(40);

        return getWeapon(module, weapon);
    }

    public static ItemStack getTierThreeRiffle(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(3);
        weapon.setReach(60);
        weapon.setAmmo(28);
        weapon.setMaxAmmo(28);
        weapon.setType(WeaponType.RIFLE);
        weapon.setPower(10);
        weapon.setTrailColor(Color.WHITE);
        weapon.setTimeShot(5);
        weapon.setTimeReload(40);

        return getWeapon(module, weapon);
    }

    public static ItemStack getTierOneRocketLauncher(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(1);
        weapon.setReach(40);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setType(WeaponType.ROCKET_LAUNCHER);
        weapon.setPower(2);
        weapon.setTrailColor(Color.GREEN);
        weapon.setTimeShot(20);
        weapon.setTimeReload(60);

        return getWeapon(module, weapon);
    }

    public static ItemStack getTierTwoRocketLauncher(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(2);
        weapon.setReach(50);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setType(WeaponType.ROCKET_LAUNCHER);
        weapon.setPower(3);
        weapon.setTrailColor(Color.YELLOW);
        weapon.setTimeShot(20);
        weapon.setTimeReload(60);

        return getWeapon(module, weapon);
    }

    public static ItemStack getTierThreeRocketLauncher(ILeafModule module) {

        Weapon weapon = new Weapon(module);

        weapon.setTier(2);
        weapon.setReach(60);
        weapon.setAmmo(4);
        weapon.setMaxAmmo(4);
        weapon.setType(WeaponType.ROCKET_LAUNCHER);
        weapon.setPower(4);
        weapon.setTrailColor(Color.YELLOW);
        weapon.setTimeShot(20);
        weapon.setTimeReload(60);

        return getWeapon(module, weapon);
    }

    private static ItemStack getAmmo(ILeafModule module, AmmoType type, int tier) {

        ItemStack stack = new ItemStack(type.getDefaultMaterial(), 1);
        ItemMeta  meta  = stack.getItemMeta();

        Ammunition ammunition = new Ammunition(module);
        ammunition.setTier(tier);
        ammunition.setType(type);

        Ammunition.persist(module, meta, ammunition);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getRiffleAmmo(ILeafModule module, int tier) {

        return getAmmo(module, AmmoType.RIFLE, tier);
    }

    public static ItemStack getRocketLauncherAmmo(ILeafModule module, int tier) {

        return getAmmo(module, AmmoType.EXPLOSIVE, tier);
    }

}
