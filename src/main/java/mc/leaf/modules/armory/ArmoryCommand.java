package mc.leaf.modules.armory;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.api.command.annotations.MinecraftCommand;
import mc.leaf.core.api.command.annotations.Param;
import mc.leaf.core.api.command.annotations.Sender;
import mc.leaf.core.utils.MinecraftColors;
import mc.leaf.modules.armory.entities.Ammunition;
import mc.leaf.modules.armory.entities.License;
import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.enums.AmmoType;
import mc.leaf.modules.armory.enums.WeaponType;
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

import java.util.Optional;
import java.util.function.Consumer;

public class ArmoryCommand extends PluginCommandImpl {

    private final ArmoryModule module;

    public ArmoryCommand(ArmoryModule module) {

        super(module.getCore());
        this.module = module;
    }

    // <editor-fold desc="Messages">
    private static Component propValue(String prop, String value) {

        return Component.empty()
                        .append(Component.text(prop + ":").style(Style.style(TextDecoration.BOLD)))
                        .append(Component.space())
                        .append(Component.text(value));
    }

    private static Component propValue(String prop, Component value) {

        return Component.empty()
                        .append(Component.text(prop + ":").style(Style.style(TextDecoration.BOLD)))
                        .append(Component.space())
                        .append(value);
    }

    private static Component aboutWeapon(Weapon weapon) {

        String    colorHex = ArmoryUtils.asHex(weapon.getTrailColor());
        TextColor color    = TextColor.color(weapon.getTrailColor().asRGB());
        return Component
                .empty()
                .append(Component.text("About this weapon..."))
                .append(Component.newline())
                .append(propValue("Rank", ArmoryUtils.toRoman(weapon.getTier())))
                .append(Component.newline())
                .append(propValue("Reach", String.valueOf(weapon.getReach())))
                .append(Component.newline())
                .append(propValue("Power", String.valueOf(weapon.getPower())))
                .append(Component.newline())
                .append(propValue("Color", Component.text(colorHex).color(color)))
                .append(Component.newline())
                .append(propValue("Shot Cooldown", weapon.getTimeShot() + " ticks"))
                .append(Component.newline())
                .append(propValue("Reload Cooldown", weapon.getTimeReload() + " ticks"));
    }

    private static Component licenseGivenSenderNotification(Player player, int level) {

        return Component.empty()
                        .append(Component.text("You gave a tier ")
                                         .color(MinecraftColors._A))
                        .append(Component.text(ArmoryUtils.toRoman(level)).color(MinecraftColors._F))
                        .append(Component.text(" license to ").color(MinecraftColors._A))
                        .append(Component.text(player.getName()).color(MinecraftColors.AQUA));
    }

    private static Component licenseGivenTargetNotification(int level) {

        return Component.empty()
                        .append(Component.text("You received a tier ").color(MinecraftColors._A))
                        .append(Component.text(ArmoryUtils.toRoman(level)).color(MinecraftColors._F))
                        .append(Component.text(" license.").color(MinecraftColors._A));
    }

    private static Component licenseTakenSenderNotification(Player player) {

        return Component.empty()
                        .append(Component.text("You removed ").color(MinecraftColors._A))
                        .append(Component.text(player.getName()).color(MinecraftColors.AQUA))
                        .append(Component.text("'s license.").color(MinecraftColors._A));
    }

    private static Component licenseTakenTargetNotification() {

        return Component.empty()
                        .append(Component.text("Your license has been removed.").color(MinecraftColors._A));
    }

    // </editor-fold>

    private static Component noWeaponInHand() {

        return Component.empty()
                        .append(Component.text("The held item is not a weapon.").color(MinecraftColors._C));
    }

    private static Component weaponUpdated() {

        return Component.empty()
                        .append(Component.text("The weapon has been updated.").color(MinecraftColors._A));
    }

    private void updateWeapon(Player player, Consumer<Weapon> onWeaponLoad) {

        ItemStack        stack          = player.getInventory().getItemInMainHand();
        ItemMeta         meta           = stack.getItemMeta();
        Optional<Weapon> optionalWeapon = Weapon.from(this.module, meta);

        if (optionalWeapon.isPresent()) {
            Weapon weapon = optionalWeapon.get();
            onWeaponLoad.accept(weapon);
            Weapon.persist(this.module, meta, weapon);
            stack.setItemMeta(meta);
            player.sendMessage(weaponUpdated());
            return;
        }

        player.sendMessage(noWeaponInHand());
    }

    @MinecraftCommand(
            value = "license give {player} [level]",
            opOnly = true,
            allowConsole = true,
            allowCommandBlock = true
    )
    public void giveLicense(@Sender CommandSender sender, @Param Player target, @Param int level) {

        License license = License.from(this.module, target).orElse(new License(this.module));
        license.setLevel(level);
        License.persist(this.module, target, license);

        sender.sendMessage(licenseGivenSenderNotification(target, level));
        target.sendMessage(licenseGivenTargetNotification(level));
    }

    @MinecraftCommand(value = "license take {player}", opOnly = true, allowConsole = true, allowCommandBlock = true)
    public void removePlayerLicence(@Sender CommandSender sender, @Param Player player) {

        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        persistentDataContainer.remove(ArmoryKeys.licence(this.module));

        sender.sendMessage(licenseTakenSenderNotification(player));
        player.sendMessage(licenseTakenTargetNotification());
    }

    @MinecraftCommand(value = "weapon details")
    public void viewWeaponMeta(@Sender Player player) {

        ItemStack        stack          = player.getInventory().getItemInMainHand();
        ItemMeta         meta           = stack.getItemMeta();
        Optional<Weapon> optionalWeapon = Weapon.from(this.module, meta);

        if (optionalWeapon.isPresent()) {
            player.sendMessage(aboutWeapon(optionalWeapon.get()));
            return;
        }

        player.sendMessage(noWeaponInHand());
    }

    @MinecraftCommand(value = "weapon set tier [tier]", opOnly = true)
    public void updateWeaponTier(@Sender Player player, @Param int tier) {

        this.updateWeapon(player, weapon -> weapon.setTier(tier));
    }

    @MinecraftCommand(value = "weapon set reach [reach]", opOnly = true)
    public void updateWeaponReach(@Sender Player player, @Param int reach) {

        this.updateWeapon(player, weapon -> weapon.setReach(reach));
    }

    @MinecraftCommand(value = "weapon set ammo [ammo]", opOnly = true)
    public void updateWeaponAmmo(@Sender Player player, @Param int ammo) {

        this.updateWeapon(player, weapon -> weapon.setAmmo(ammo));
    }

    @MinecraftCommand(value = "weapon set maxAmmo [ammo]", opOnly = true)
    public void updateWeaponMaxAmmo(@Sender Player player, @Param int ammo) {

        this.updateWeapon(player, weapon -> weapon.setMaxAmmo(ammo));
    }

    @MinecraftCommand(value = "weapon set type {weaponType}", opOnly = true)
    public void updateWeaponType(@Sender Player player, @Param WeaponType type) {

        this.updateWeapon(player, weapon -> weapon.setType(type));
    }

    @MinecraftCommand(value = "weapon set power [power]", opOnly = true)
    public void updateWeaponPower(@Sender Player player, @Param int power) {

        this.updateWeapon(player, weapon -> weapon.setPower(power));
    }

    @MinecraftCommand(value = "weapon set color [r] [g] [b]", opOnly = true)
    public void updateWeaponPower(@Sender Player player, @Param int r, @Param int g, @Param int b) {

        this.updateWeapon(player, weapon -> weapon.setTrailColor(Color.fromRGB(r, g, b)));
    }

    @MinecraftCommand(value = "weapon set timing shot [ticks]", opOnly = true)
    public void updateWeaponShotTime(@Sender Player player, @Param int ticks) {

        this.updateWeapon(player, weapon -> weapon.setTimeShot(ticks));
    }

    @MinecraftCommand(value = "weapon set timing reload [ticks]", opOnly = true)
    public void updateWeaponReloadTime(@Sender Player player, @Param int ticks) {

        this.updateWeapon(player, weapon -> weapon.setTimeReload(ticks));
    }

    @MinecraftCommand(value = "weapon create {weaponType} [reach] [ammo] [power]")
    public void createWeapon(@Sender Player player, @Param WeaponType weaponType, @Param int reach, @Param int ammo, @Param int power) {

        ItemStack stack  = new ItemStack(Material.CROSSBOW, 1);
        Weapon    weapon = new Weapon(this.module);

        weapon.setTimeReload(40);
        weapon.setTimeShot(5);
        weapon.setTrailColor(Color.RED);
        weapon.setReach(reach);
        weapon.setAmmo(ammo);
        weapon.setMaxAmmo(ammo);
        weapon.setType(weaponType);
        weapon.setPower(power);

        ItemMeta meta = stack.getItemMeta();
        Weapon.persist(this.module, meta, weapon);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
    }

    @MinecraftCommand(value = "ammo create {ammoType} [tier]")
    public void createAmmo(@Sender Player player, @Param AmmoType ammoType, @Param int tier) {

        ItemStack  stack      = new ItemStack(ammoType.getDefaultMaterial(), 1);
        Ammunition ammunition = new Ammunition(this.module);

        ammunition.setType(ammoType);
        ammunition.setTier(tier);

        ItemMeta meta = stack.getItemMeta();
        Ammunition.persist(this.module, meta, ammunition);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
    }

}
