package mc.leaf.modules.weapons.data.entities;

import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.utils.WeaponUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Ammunition {

    private Integer tier;
    private String  type;

    public Integer getTier() {

        return tier;
    }

    public void setTier(Integer tier) {

        this.tier = tier;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public boolean isCompatible(Weapon weapon) {

        return weapon.getBulletType().equals(this.getType()) && this.getTier().equals(weapon.getTier());
    }

    public ItemStack asItem(LeafWeaponsModule module) {

        ItemStack stack = new ItemStack(this.getType().equals("normal") ? Material.IRON_NUGGET : Material.GOLD_NUGGET);
        stack.setAmount(1);
        WeaponUtils.saveAmmo(module, stack, this);
        return stack;
    }

}
