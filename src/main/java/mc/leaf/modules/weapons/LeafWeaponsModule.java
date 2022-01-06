package mc.leaf.modules.weapons;

import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.weapons.commands.WeaponCommand;
import mc.leaf.modules.weapons.listeners.PlayerLootListener;
import mc.leaf.modules.weapons.listeners.PlayerShootListener;
import mc.leaf.modules.weapons.managers.BulletManager;
import mc.leaf.modules.weapons.managers.SneakyManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Optional;

public class LeafWeaponsModule implements ILeafModule {

    private final JavaPlugin plugin;
    private final ILeafCore  core;
    private       boolean    enabled = false;

    private NamespacedKey weaponNamespace;
    private NamespacedKey licenceNamespace;
    private NamespacedKey ammunitionNamespace;

    private BulletManager bulletManager;
    private SneakyManager sneakyManager;

    public LeafWeaponsModule(JavaPlugin plugin, ILeafCore core) {

        this.plugin = plugin;
        this.core   = core;
        this.core.registerModule(this);
        this.core.registerDynamicOptions("weaponType", Arrays.asList("normal", "explosive"));
    }

    @Override
    public void onEnable() {

        this.weaponNamespace     = new NamespacedKey(this.plugin, "weapon");
        this.licenceNamespace    = new NamespacedKey(this.plugin, "licence");
        this.ammunitionNamespace = new NamespacedKey(this.plugin, "ammunition");
        this.bulletManager       = new BulletManager(this);
        this.sneakyManager       = new SneakyManager(this);

        this.bulletManager.start();
        this.sneakyManager.start();

        // Add custom event support later in leaf-core (how though ?)
        this.core.getEventBridge().register(this, new PlayerLootListener(this));
        this.core.getEventBridge().register(this, new PlayerShootListener(this));

        Optional.ofNullable(Bukkit.getPluginCommand("weapons"))
                .ifPresent(command -> command.setExecutor(new WeaponCommand(this)));
        this.enabled = true;
    }

    @Override
    public void onDisable() {

        this.bulletManager.stop();
        this.sneakyManager.stop();

        this.weaponNamespace     = null;
        this.licenceNamespace    = null;
        this.ammunitionNamespace = null;
        this.bulletManager       = null;
        this.sneakyManager       = null;

        Optional.ofNullable(Bukkit.getPluginCommand("weapons")).ifPresent(command -> command.setExecutor(null));
        this.enabled = false;
    }

    @Override
    public ILeafCore getCore() {

        return this.core;
    }

    @Override
    public String getName() {

        return "Weapons";
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }

    @Override
    public JavaPlugin getPlugin() {

        return this.plugin;
    }

    public NamespacedKey getAmmunitionNamespace() {

        return this.ammunitionNamespace;
    }

    public NamespacedKey getLicenceNamespace() {

        return this.licenceNamespace;
    }

    public NamespacedKey getWeaponNamespace() {

        return this.weaponNamespace;
    }

    public BulletManager getBulletManager() {

        return this.bulletManager;
    }

    public SneakyManager getSneakyManager() {

        return this.sneakyManager;
    }

}
