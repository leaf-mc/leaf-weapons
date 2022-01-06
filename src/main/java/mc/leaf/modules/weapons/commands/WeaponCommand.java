package mc.leaf.modules.weapons.commands;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.api.command.annotations.Param;
import mc.leaf.core.api.command.annotations.Runnable;
import mc.leaf.core.api.command.annotations.Sender;
import mc.leaf.modules.weapons.LeafWeapons;
import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.data.entities.Ammunition;
import mc.leaf.modules.weapons.data.entities.Licence;
import mc.leaf.modules.weapons.data.entities.Weapon;
import mc.leaf.modules.weapons.data.tags.LicenceTag;
import mc.leaf.modules.weapons.data.tags.WeaponTag;
import mc.leaf.modules.weapons.lib.converters.bukkit.PlayerConverter;
import mc.leaf.modules.weapons.lib.converters.types.IntegerConverter;
import mc.leaf.modules.weapons.utils.WeaponUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;

public class WeaponCommand extends PluginCommandImpl {

    private final LeafWeaponsModule module;

    public WeaponCommand(LeafWeaponsModule module) {

        super(module.getCore());
        this.module = module;
    }

    @Runnable(value = "licence give {player} [level]", opOnly = true, allowCommandBlock = true, allowConsole = true)
    public void setPlayerLicence(@Sender CommandSender sender, @Param(converter = PlayerConverter.class) Player player, @Param(converter = IntegerConverter.class) Integer level) {

        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        Licence                 licence                 = persistentDataContainer.get(this.module.getLicenceNamespace(), new LicenceTag(this.module));

        if (licence == null) {
            licence = new Licence();
        }

        licence.setLevel(level);
        persistentDataContainer.set(this.module.getLicenceNamespace(), new LicenceTag(this.module), licence);

        sender.sendMessage(LeafWeapons.PREFIX + " §aThe player received the licence.");
        player.sendMessage(LeafWeapons.PREFIX + " §aYou received a licence rank " + WeaponUtils.toRoman(level));
    }

    @Runnable(value = "licence take {player}", opOnly = true, allowConsole = true, allowCommandBlock = true)
    public void removePlayerLicence(@Sender CommandSender sender, @Param(converter = PlayerConverter.class) Player player) {

        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        persistentDataContainer.remove(this.module.getLicenceNamespace());

        sender.sendMessage(LeafWeapons.PREFIX + " §aThe player's licence has been revoked.");
        player.sendMessage(LeafWeapons.PREFIX + " §aYour licence has been revoked.");
    }

    @Runnable(value = "how-to-generate", opOnly = true, allowCommandBlock = true, allowConsole = true)
    public void sendHelp(@Sender CommandSender sender) {

        sender.sendMessage(LeafWeapons.PREFIX + " §3/weapon generate <tier> <reach> <maxAmmo> <type> <power> <red> <green> <blue> <sounds> <shotTime> <reloadTime>");
    }

    @Runnable(value = "generate [tier] [reach] [maxAmmo] [type] [power] [red] [green] [blue] [sounds] [shotTime] [reloadTime]", opOnly = true)
    public void createWeapon(
            @Sender Player player,
            @Param(converter = IntegerConverter.class) Integer tier,
            @Param(converter = IntegerConverter.class) Integer reach,
            @Param(converter = IntegerConverter.class) Integer maxAmmo,
            @Param String type,
            @Param(converter = IntegerConverter.class) Integer power,
            @Param(converter = IntegerConverter.class) Integer red,
            @Param(converter = IntegerConverter.class) Integer green,
            @Param(converter = IntegerConverter.class) Integer blue,
            @Param String sounds,
            @Param(converter = IntegerConverter.class) Integer shotTime,
            @Param(converter = IntegerConverter.class) Integer reloadTime
    ) {

        ItemStack stack = player.getInventory().getItemInMainHand();

        if (stack.getMaxStackSize() > 1) {
            player.sendMessage(LeafWeapons.PREFIX + " §cStackable items are not supported.");
            return;
        }

        Weapon weapon = new Weapon();

        weapon.setTier(tier);
        weapon.setReach(reach);
        weapon.setAmmo(maxAmmo);
        weapon.setMaxAmmo(maxAmmo);
        weapon.setBulletType(type);
        weapon.setBulletPower(power);
        weapon.setBulletTrailColor(Color.fromRGB(red, green, blue));
        weapon.setSoundCategory(sounds);
        weapon.setNextShot(0L);
        weapon.setTimeShot((long) shotTime);
        weapon.setTimeReload((long) reloadTime);

        ItemMeta                meta                    = stack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        persistentDataContainer.set(this.module.getWeaponNamespace(), new WeaponTag(this.module), weapon);

        String loreTier = "Tier " + WeaponUtils.toRoman(weapon.getTier());
        String loreAmmo = "Ammo " + weapon.getAmmo() + " / " + weapon.getMaxAmmo();
        String loreType = "Type " + weapon.getBulletType();

        meta.lore(Arrays.asList(
                Component.text(loreTier, Style.style(TextColor.fromCSSHexString("#FFAA00"), TextDecoration.BOLD)),
                Component.empty(),
                Component.text(loreAmmo, Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)),
                Component.text(loreType, Style.style(TextColor.fromCSSHexString("#AAAAAA"), TextDecoration.BOLD))
        ));

        stack.setItemMeta(meta);

        player.sendMessage(LeafWeapons.PREFIX + " §aThe weapon has been created.");
    }

    @Runnable(value = "create ammo [count] [type] [tier]", opOnly = true)
    public void createAmmo(@Sender Player player, @Param(converter = IntegerConverter.class) Integer count, @Param String type, @Param(converter = IntegerConverter.class) Integer tier) {

        ItemStack stack = new ItemStack(type.equals("normal") ? Material.IRON_NUGGET : Material.GOLD_NUGGET, count);

        Ammunition ammunition = new Ammunition();
        ammunition.setType(type);
        ammunition.setTier(tier);

        WeaponUtils.saveAmmo(this.module, stack, ammunition);

        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.displayName(Component.text("Munitions", Style.style(TextColor.fromCSSHexString("#55FFFF"), TextDecoration.BOLD)));
        stack.setItemMeta(itemMeta);

        player.getInventory().addItem(stack);
    }

    @Runnable(value = "create weapon {weaponType} [reach] [ammo] [power]", opOnly = true)
    public void createWeapon(@Sender Player player, @Param String weaponType, @Param(converter = IntegerConverter.class) Integer reach, @Param(converter = IntegerConverter.class) Integer ammo, @Param(converter = IntegerConverter.class) Integer power) {

        ItemStack stack = new ItemStack(Material.CROSSBOW, 1);

        Weapon weapon = new Weapon();

        // Defaults
        weapon.setTimeReload(3000L);
        weapon.setTimeShot(500L);
        weapon.setBulletTrailColor(Color.RED);
        weapon.setSoundCategory("alfgard.gun");

        weapon.setReach(reach);
        weapon.setAmmo(ammo);
        weapon.setMaxAmmo(ammo);
        weapon.setBulletType(weaponType);
        weapon.setBulletPower(power);
        weapon.setNextShot(0L);

        WeaponUtils.saveWeapon(this.module, stack, weapon, true);
        player.getInventory().addItem(stack);
    }

}
