package mc.leaf.modules.armory.listeners;

import mc.leaf.core.events.LeafListener;
import mc.leaf.core.utils.MinecraftColors;
import mc.leaf.modules.armory.ArmoryModule;
import mc.leaf.modules.armory.entities.Ammunition;
import mc.leaf.modules.armory.entities.License;
import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.events.BulletFiredEvent;
import mc.leaf.modules.armory.events.PlayerUseWeaponEvent;
import mc.leaf.modules.armory.events.WeaponTargetFoundEvent;
import mc.leaf.modules.armory.generics.Bullet;
import mc.leaf.modules.armory.generics.ShotTarget;
import mc.leaf.modules.armory.generics.WeaponWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ArmoryPlayerWeaponListener extends LeafListener {

    private final ArmoryModule module;

    public ArmoryPlayerWeaponListener(ArmoryModule module) {

        this.module = module;
    }

    @Override
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        boolean fire   = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        boolean reload = event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;

        if (!fire && !reload) {
            return; // Not a weapon related event
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return; // No item was in hand.
        }

        if (event.getPlayer().hasCooldown(item.getType())) {
            return;
        }

        Optional<Weapon>  optionalWeapon  = Weapon.from(this.module, item.getItemMeta());
        Optional<License> optionalLicence = License.from(this.module, event.getPlayer());

        if (optionalWeapon.isEmpty()) {
            return; // It's not a weapon.
        }

        if (optionalLicence.isEmpty()) {
            event.getPlayer().sendMessage(ArmoryModule.PREFIX + " §cYou don't have a licence.");
            return;
        }
        event.setCancelled(true);

        Weapon  weapon  = optionalWeapon.get();
        License license = optionalLicence.get();

        if (!license.canUseWeapon(weapon)) {
            event.getPlayer().sendMessage(ArmoryModule.PREFIX + " §cYour licence do not have the required rank.");
            return;
        }

        if (fire) {
            this.handleWeaponUse(event.getPlayer(), item, weapon);
        } else {
            this.doWeaponReload(event.getPlayer(), item, weapon);
        }

    }

    private void doWeaponReload(Player player, ItemStack stack, Weapon weapon) {

        if (weapon.getAmmo().equals(weapon.getMaxAmmo())) {
            return; // Already full
        }

        boolean needAmmo = player.getGameMode() != GameMode.CREATIVE && stack.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 0;

        int maxReloadAmount = weapon.getMaxAmmo() - weapon.getAmmo();
        int reloadAmount    = needAmmo ? 0 : maxReloadAmount;

        if (needAmmo) {

            for (int slotId = 0; slotId <= 40; slotId++) {
                ItemStack slotStack = player.getInventory().getItem(slotId);

                if (slotStack == null) {
                    continue;
                }

                Optional<Ammunition> optionalAmmo = Ammunition.from(this.module, slotStack.getItemMeta());
                if (optionalAmmo.isPresent()) {
                    Ammunition ammunition = optionalAmmo.get();
                    if (ammunition.isCompatible(weapon)) {
                        int amount = slotStack.getAmount();

                        if (amount > (maxReloadAmount - reloadAmount)) {
                            reloadAmount = maxReloadAmount;
                            slotStack.setAmount(amount - reloadAmount);
                            player.getInventory().setItem(slotId, slotStack);
                            break;
                        } else if (amount == (maxReloadAmount - reloadAmount)) {
                            reloadAmount = maxReloadAmount;
                            slotStack.setAmount(0);
                            break;
                        } else {
                            reloadAmount += amount;
                            slotStack.setAmount(0);
                        }

                        player.getInventory().setItem(slotId, slotStack);
                    }
                }
            }
        }

        if (reloadAmount == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);
            TextComponent subtitle = Component.text("No ammunition").color(MinecraftColors._C);
            Title.Times   times    = Title.Times.times(Duration.ZERO, Duration.of(3, ChronoUnit.SECONDS), Duration.ZERO);
            player.sendTitlePart(TitlePart.TITLE, Component.text().asComponent());
            player.sendTitlePart(TitlePart.SUBTITLE, subtitle.asComponent());
            player.sendTitlePart(TitlePart.TIMES, times);
            return;
        }

        weapon.setAmmo(weapon.getAmmo() + reloadAmount);
        ItemMeta meta = stack.getItemMeta();
        Weapon.persist(this.module, meta, weapon);
        stack.setItemMeta(meta);
        player.setCooldown(stack.getType(), weapon.getTimeReload());
    }

    private void handleWeaponUse(Player player, ItemStack stack, Weapon weapon) {

        if (weapon.getAmmo() == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 10, 2);
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 10, 0);
            return;
        }

        WeaponWrapper        wrapper              = weapon.getWrapper(player);
        PlayerUseWeaponEvent playerUseWeaponEvent = new PlayerUseWeaponEvent(player, weapon.getWrapper(player));
        Bukkit.getPluginManager().callEvent(playerUseWeaponEvent);

        if (playerUseWeaponEvent.isCancelled()) {
            return;
        }

        List<ShotTarget>       targets                = wrapper.findTargets();
        WeaponTargetFoundEvent weaponTargetFoundEvent = new WeaponTargetFoundEvent(player, wrapper, targets);
        Bukkit.getPluginManager().callEvent(weaponTargetFoundEvent);

        if (weaponTargetFoundEvent.isCancelled()) {
            return;
        }

        List<BulletFiredEvent> bulletEvents = weaponTargetFoundEvent.getTargets().stream()
                                                                    .map(wrapper::shotAt)
                                                                    .map(bullet -> new BulletFiredEvent(player, bullet))
                                                                    .toList();

        bulletEvents.forEach(Bukkit.getPluginManager()::callEvent);

        List<Bullet> bullets = bulletEvents.stream()
                                           .filter(ev -> !ev.isCancelled())
                                           .map(BulletFiredEvent::getBullet)
                                           .limit(weapon.getAmmo())
                                           .toList();

        if (bullets.isEmpty()) {
            return;
        }

        bullets.forEach(bullet -> {
            if (bullet.needTick()) {
                this.module.getBulletManager().offer(bullet);
            } else {
                bullet.tick();
            }

            wrapper.doShotFeedback(bullet);
        });

        weapon.setAmmo(weapon.getAmmo() - bullets.size());
        ItemMeta meta = stack.getItemMeta();
        Weapon.persist(this.module, meta, weapon);
        stack.setItemMeta(meta);
        player.setCooldown(stack.getType(), weapon.getTimeReload());
    }

}
