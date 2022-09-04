package mc.leaf.modules.weapons.listeners;

import mc.leaf.core.events.LeafListener;
import mc.leaf.core.utils.MinecraftColors;
import mc.leaf.modules.weapons.LeafWeapons;
import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.data.entities.Licence;
import mc.leaf.modules.weapons.data.entities.Weapon;
import mc.leaf.modules.weapons.entities.BulletEntity;
import mc.leaf.modules.weapons.event.ShotEvent;
import mc.leaf.modules.weapons.utils.WeaponUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

public class PlayerShootListener extends LeafListener {

    private final LeafWeaponsModule module;

    public PlayerShootListener(LeafWeaponsModule module) {

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

        if (event.getItem() == null) {
            return; // No item was in hand.
        }

        Optional<Weapon>  optionalWeapon  = WeaponUtils.extractWeapon(this.module, event.getItem());
        Optional<Licence> optionalLicence = WeaponUtils.extractLicence(this.module, event.getPlayer());

        if (optionalWeapon.isEmpty()) {
            return; // It's not a weapon.
        }

        // It's a weapon, cancel the event to avoid any problem.
        event.setCancelled(true);


        if (optionalLicence.isEmpty()) {
            event.getPlayer().sendMessage(LeafWeapons.PREFIX + " §cYou don't have a licence.");
            return;
        }

        Weapon  weapon  = optionalWeapon.get();
        Licence licence = optionalLicence.get();

        if (!licence.canUseWeapon(weapon)) {
            event.getPlayer().sendMessage(LeafWeapons.PREFIX + " §cYour licence do not have the required rank.");
            return;
        }


        if (reload) {
            if (weapon.getAmmo().equals(weapon.getMaxAmmo())) {
                return;
            }

            if (weapon.reload(this.module, event.getPlayer(), event.getItem())) {
                weapon.setNextShot(System.currentTimeMillis() + weapon.getTimeReload());
                event.getPlayer().getWorld()
                        .playSound(event.getPlayer().getLocation(), weapon.getSoundCategory() + ".reload", 1, 1);
            } else {
                TextComponent subtitle = Component.text("No ammunition").color(MinecraftColors._C);
                Title.Times   times    = Title.Times.of(Duration.ZERO, Duration.of(3, ChronoUnit.SECONDS), Duration.ZERO);
                event.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text().asComponent());
                event.getPlayer().sendTitlePart(TitlePart.SUBTITLE, subtitle.asComponent());
                event.getPlayer().sendTitlePart(TitlePart.TIMES, times);
            }
        }

        if (fire) {
            if (weapon.getNextShot() > System.currentTimeMillis()) {
                return;
            }

            if (weapon.getAmmo() == 0) {
                TextComponent subtitle = Component.text("Reload !").color(MinecraftColors._A);
                Title.Times   times    = Title.Times.of(Duration.ZERO, Duration.of(2, ChronoUnit.SECONDS), Duration.ZERO);
                event.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text().asComponent());
                event.getPlayer().sendTitlePart(TitlePart.SUBTITLE, subtitle.asComponent());
                event.getPlayer().sendTitlePart(TitlePart.TIMES, times);
                event.getPlayer().getWorld()
                        .playSound(event.getPlayer().getLocation(), weapon.getSoundCategory() + ".empty", 1, 1);
                return;
            }

            ShotEvent shotEvent = new ShotEvent(event.getPlayer(), weapon);
            Bukkit.getPluginManager().callEvent(shotEvent);

            if (!shotEvent.isCancelled()) {
                weapon.setAmmo(weapon.getAmmo() - 1);

                switch (weapon.getBulletType()) {
                    case "normal" -> {
                        shotEvent.shot();
                        event.getPlayer().getWorld().playSound(event.getPlayer()
                                .getLocation(), weapon.getSoundCategory() + ".fire", 10, (new Random().nextFloat() / 2) + 0.75f);
                        weapon.setNextShot(System.currentTimeMillis() + weapon.getTimeShot());
                    }
                    case "explosive" -> {
                        BulletEntity bullet = shotEvent.getBullet();
                        this.module.getBulletManager().offer(bullet);
                        event.getPlayer().getWorld().playSound(event.getPlayer()
                                .getLocation(), weapon.getSoundCategory() + ".ignite", 10, (new Random().nextFloat() / 2) + 0.75f);
                    }
                }

                TextComponent subtitle = Component.text(weapon.getAmmo() + " / " + weapon.getMaxAmmo())
                        .color(MinecraftColors._A);
                Title.Times times = Title.Times.of(Duration.ZERO, Duration.of(1, ChronoUnit.SECONDS), Duration.ZERO);
                event.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text().asComponent());
                event.getPlayer().sendTitlePart(TitlePart.SUBTITLE, subtitle.asComponent());
                event.getPlayer().sendTitlePart(TitlePart.TIMES, times);
            }
        }

        WeaponUtils.saveWeapon(this.module, event.getItem(), weapon, false);
    }

}
