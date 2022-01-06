package mc.leaf.modules.weapons.data.entities;

import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.utils.WeaponUtils;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

public class Weapon {

    private Integer tier;
    private Integer reach;
    private Integer ammo;
    private Integer maxAmmo;
    private String  bulletType;
    private Integer bulletPower;
    private Color   bulletTrailColor;
    private String  soundCategory;
    private Long    nextShot;
    private Long    timeShot;
    private Long    timeReload;

    public Integer getTier() {

        return tier;
    }

    public void setTier(Integer tier) {

        this.tier = tier;
    }

    public Integer getReach() {

        return reach;
    }

    public void setReach(Integer reach) {

        this.reach = reach;
    }

    public Integer getAmmo() {

        return ammo;
    }

    public void setAmmo(Integer ammo) {

        this.ammo = ammo;
    }

    public Integer getMaxAmmo() {

        return maxAmmo;
    }

    public void setMaxAmmo(Integer maxAmmo) {

        this.maxAmmo = maxAmmo;
    }

    public String getBulletType() {

        return bulletType;
    }

    public void setBulletType(String bulletType) {

        this.bulletType = bulletType;
    }

    public Integer getBulletPower() {

        return bulletPower;
    }

    public void setBulletPower(Integer bulletPower) {

        this.bulletPower = bulletPower;
    }

    public Color getBulletTrailColor() {

        return bulletTrailColor;
    }

    public void setBulletTrailColor(Color bulletTrailColor) {

        this.bulletTrailColor = bulletTrailColor;
    }

    public String getSoundCategory() {

        return soundCategory;
    }

    public void setSoundCategory(String soundCategory) {

        this.soundCategory = soundCategory;
    }

    public Long getNextShot() {

        return nextShot;
    }

    public void setNextShot(Long nextShot) {

        this.nextShot = nextShot;
    }

    public Long getTimeShot() {

        return timeShot;
    }

    public void setTimeShot(Long timeShot) {

        this.timeShot = timeShot;
    }

    public Long getTimeReload() {

        return timeReload;
    }

    public void setTimeReload(Long timeReload) {

        this.timeReload = timeReload;
    }


    public boolean reload(LeafWeaponsModule module, Player player, ItemStack stack) {

        boolean needAmmo = player.getGameMode() != GameMode.CREATIVE && stack.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 0;

        if (player.getGameMode() == GameMode.CREATIVE || stack.getEnchantmentLevel(Enchantment.ARROW_INFINITE) > 0) {
            this.setAmmo(this.getMaxAmmo());
            return true;
        }

        int missingAmmo     = this.getMaxAmmo() - this.getAmmo();
        int canReloadAmount = 0;

        // Scan the player inventory for ammunition.
        HashMap<Integer, ? extends ItemStack> queryResult = new HashMap<>(player.getInventory()
                .all(this.getBulletType().equals("normal") ? Material.IRON_NUGGET : Material.GOLD_NUGGET));
        HashMap<Integer, ItemStack> valid = new HashMap<>();

        queryResult.forEach((index, inventoryStack) -> {
            Optional<Ammunition> optionalAmmunition = WeaponUtils.extractAmmunition(module, inventoryStack);
            if (optionalAmmunition.isPresent()) {
                Ammunition ammunition = optionalAmmunition.get();
                if (ammunition.isCompatible(this)) {
                    valid.put(index, inventoryStack);
                }
            }
        });

        if (valid.size() == 0) {
            return false;
        }

        for (Integer integer : valid.keySet()) {
            ItemStack itemStack = valid.get(integer);
            if (canReloadAmount < missingAmmo) {
                // We still need to reload.
                if (itemStack.getAmount() >= (missingAmmo - canReloadAmount)) {
                    itemStack.setAmount(itemStack.getAmount() - (missingAmmo - canReloadAmount));
                    canReloadAmount = missingAmmo;
                } else {
                    canReloadAmount += itemStack.getAmount();
                    itemStack.setAmount(0);
                }
            }
            player.getInventory().setItem(integer, itemStack);
        }

        this.setAmmo(this.getAmmo() + canReloadAmount);
        return true;
    }

    public ItemStack asItem(LeafWeaponsModule module) {

        ItemStack stack = new ItemStack(Material.CROSSBOW);
        stack.setAmount(1);
        WeaponUtils.saveWeapon(module, stack, this, false);
        return stack;
    }

}
