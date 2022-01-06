package mc.leaf.modules.weapons.utils;

import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.data.entities.Ammunition;
import mc.leaf.modules.weapons.data.entities.Licence;
import mc.leaf.modules.weapons.data.entities.Weapon;
import mc.leaf.modules.weapons.data.tags.AmmunitionTag;
import mc.leaf.modules.weapons.data.tags.LicenceTag;
import mc.leaf.modules.weapons.data.tags.WeaponTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

public class WeaponUtils {

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {

        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static Integer getNaturalWeaponTier(Weapon weapon) {

        float maxAmmo = weapon.getMaxAmmo();
        float power   = weapon.getBulletPower();
        float mode    = 0;

        if (weapon.getBulletType().equals("normal")) {
            mode = 1;
        } else if (weapon.getBulletType().equals("explosive")) {
            mode = 3;
        }

        return Math.round(maxAmmo * (power / 100f) * mode);
    }

    public static Optional<Weapon> extractWeapon(LeafWeaponsModule module, ItemStack stack) {

        ItemMeta                itemMeta  = stack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return Optional.ofNullable(container.get(module.getWeaponNamespace(), new WeaponTag(module)));
    }

    public static Optional<Ammunition> extractAmmunition(LeafWeaponsModule module, ItemStack stack) {

        ItemMeta                itemMeta  = stack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return Optional.ofNullable(container.get(module.getAmmunitionNamespace(), new AmmunitionTag(module)));
    }


    public static Optional<Licence> extractLicence(LeafWeaponsModule module, Player player) {

        return Optional.ofNullable(player.getPersistentDataContainer()
                .get(module.getLicenceNamespace(), new LicenceTag(module)));
    }

    public static void saveWeapon(LeafWeaponsModule module, ItemStack stack, Weapon data, boolean refreshTier) {

        ItemMeta                itemMeta  = stack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (data == null) {
            container.remove(module.getWeaponNamespace());
            itemMeta.lore(Collections.emptyList());
        } else {
            if (refreshTier) {
                data.setTier(getNaturalWeaponTier(data));
            }

            container.set(module.getWeaponNamespace(), new WeaponTag(module), data);

            switch (data.getBulletType()) {
                case "normal" -> itemMeta.displayName(Component.text("Fusil", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
                case "explosive" -> itemMeta.displayName(Component.text("Lance-Roquette", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
            }

            String loreTier = "Tier " + toRoman(data.getTier());
            String loreAmmo = "Ammo " + data.getAmmo() + " / " + data.getMaxAmmo();
            String loreType = "Type " + data.getBulletType();

            itemMeta.lore(Arrays.asList(
                    Component.text(loreTier, Style.style(TextColor.fromCSSHexString("#FFAA00"), TextDecoration.BOLD)),
                    Component.empty(),
                    Component.text(loreAmmo, Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)),
                    Component.text(loreType, Style.style(TextColor.fromCSSHexString("#AAAAAA"), TextDecoration.BOLD))
            ));
        }

        stack.setItemMeta(itemMeta);
    }

    public static void saveLicence(LeafWeaponsModule module, Player player, Licence licence) {

        PersistentDataContainer container = player.getPersistentDataContainer();

        if (licence == null) {
            container.remove(module.getLicenceNamespace());
        } else {
            container.set(module.getLicenceNamespace(), new LicenceTag(module), licence);
        }
    }

    public static void saveAmmo(LeafWeaponsModule module, ItemStack stack, Ammunition ammunition) {

        ItemMeta                itemMeta  = stack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (ammunition == null) {
            container.remove(module.getAmmunitionNamespace());
            itemMeta.lore(Collections.emptyList());
        } else {
            container.set(module.getAmmunitionNamespace(), new AmmunitionTag(module), ammunition);

            String loreTier = "Tier " + toRoman(ammunition.getTier());
            String loreType = "Type " + ammunition.getType();

            itemMeta.lore(Arrays.asList(
                    Component.text(loreTier, Style.style(TextColor.fromCSSHexString("#FFAA00"), TextDecoration.BOLD)),
                    Component.empty(),
                    Component.text(loreType, Style.style(TextColor.fromCSSHexString("#AAAAAA"), TextDecoration.BOLD))
            ));

            itemMeta.displayName(Component.text("Munition", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
        }

        stack.setItemMeta(itemMeta);
    }

}
