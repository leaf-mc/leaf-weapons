package mc.leaf.modules.weapons;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.core.interfaces.impl.LeafModule;
import mc.leaf.modules.weapons.commands.WeaponCommand;
import mc.leaf.modules.weapons.listeners.PlayerLootListener;
import mc.leaf.modules.weapons.listeners.PlayerShootListener;
import mc.leaf.modules.weapons.managers.BulletManager;
import mc.leaf.modules.weapons.managers.SneakyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LeafWeaponsModule extends LeafModule {

    private NamespacedKey weaponNamespace;
    private NamespacedKey licenceNamespace;
    private NamespacedKey ammunitionNamespace;

    private BulletManager bulletManager;
    private SneakyManager sneakyManager;

    public LeafWeaponsModule(JavaPlugin plugin, ILeafCore core) {

        super(core, plugin);

        this.getCore().registerModule(this);
        this.getCore().registerDynamicOptions("weaponType", Arrays.asList("normal", "explosive"));
    }

    @Override
    public void onEnable() {

        this.getPlugin().getLogger().info("Registering namespaced keys...");
        this.weaponNamespace     = new NamespacedKey(this.getPlugin(), "weapon");
        this.licenceNamespace    = new NamespacedKey(this.getPlugin(), "licence");
        this.ammunitionNamespace = new NamespacedKey(this.getPlugin(), "ammunition");

        this.getPlugin().getLogger().info("Registering managers...");
        this.bulletManager = new BulletManager(this);
        this.sneakyManager = new SneakyManager(this);
        this.bulletManager.start();
        this.sneakyManager.start();

        this.getPlugin().getLogger().info("Registering recipes...");
        this.createRecipes();
        this.getPlugin().getLogger().info("LeafWeapon has been enabled.");
    }

    @Override
    public void onDisable() {

        this.getPlugin().getLogger().info("Stopping managers...");
        this.bulletManager.stop();
        this.sneakyManager.stop();

        this.getPlugin().getLogger().info("Unregistering command...");
        Optional.ofNullable(Bukkit.getPluginCommand("weapons")).ifPresent(command -> command.setExecutor(null));

        this.getPlugin().getLogger().info("Cleaning up...");
        this.weaponNamespace     = null;
        this.licenceNamespace    = null;
        this.ammunitionNamespace = null;
        this.bulletManager       = null;
        this.sneakyManager       = null;

        this.getPlugin().getLogger().info("LeafWeapon has been disabled.");
    }


    @Override
    public String getName() {

        return "Weapons";
    }

    @Override
    public List<Listener> getListeners() {

        return Arrays.asList(new PlayerLootListener(this), new PlayerShootListener(this));
    }

    @Override
    public Map<String, PluginCommandImpl> getCommands() {

        return new HashMap<>() {{
            this.put("weapons", new WeaponCommand(LeafWeaponsModule.this));
        }};
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

    private void createRecipes() {

        ItemStack normalTierOne      = WeaponGenerator.getTierOneRiffleAmmo().asItem(this);
        ItemStack normalTierTwo      = WeaponGenerator.getTierTwoRiffleAmmo().asItem(this);
        ItemStack normalTierThree    = WeaponGenerator.getTierThreeRiffleAmmo().asItem(this);
        ItemStack explosiveTierOne   = WeaponGenerator.getTierOneRocketLauncherAmmo().asItem(this);
        ItemStack explosiveTierTwo   = WeaponGenerator.getTierTwoRocketLauncherAmmo().asItem(this);
        ItemStack explosiveTierThree = WeaponGenerator.getTierThreeRocketLauncherAmmo().asItem(this);

        NamespacedKey normalTierOneKey      = new NamespacedKey(this.getPlugin(), "ammunition.normal.1");
        NamespacedKey normalTierTwoKey      = new NamespacedKey(this.getPlugin(), "ammunition.normal.2");
        NamespacedKey normalTierThreeKey    = new NamespacedKey(this.getPlugin(), "ammunition.normal.3");
        NamespacedKey explosiveTierOneKey   = new NamespacedKey(this.getPlugin(), "ammunition.explosive.1");
        NamespacedKey explosiveTierTwoKey   = new NamespacedKey(this.getPlugin(), "ammunition.explosive.2");
        NamespacedKey explosiveTierThreeKey = new NamespacedKey(this.getPlugin(), "ammunition.explosive.3");

        ShapedRecipe normalTierOneCraft      = this.setupCraft(normalTierOneKey, normalTierOne, new ItemStack(Material.IRON_NUGGET));
        ShapedRecipe normalTierTwoCraft      = this.setupCraft(normalTierTwoKey, normalTierTwo, normalTierOne);
        ShapedRecipe normalTierThreeCraft    = this.setupCraft(normalTierThreeKey, normalTierThree, normalTierTwo);
        ShapedRecipe explosiveTierOneCraft   = this.setupCraft(explosiveTierOneKey, explosiveTierOne, new ItemStack(Material.GOLD_NUGGET));
        ShapedRecipe explosiveTierTwoCraft   = this.setupCraft(explosiveTierTwoKey, explosiveTierTwo, explosiveTierOne);
        ShapedRecipe explosiveTierThreeCraft = this.setupCraft(explosiveTierThreeKey, explosiveTierThree, explosiveTierTwo);

        this.getPlugin().getServer().addRecipe(normalTierOneCraft);
        this.getPlugin().getServer().addRecipe(normalTierTwoCraft);
        this.getPlugin().getServer().addRecipe(normalTierThreeCraft);
        this.getPlugin().getServer().addRecipe(explosiveTierOneCraft);
        this.getPlugin().getServer().addRecipe(explosiveTierTwoCraft);
        this.getPlugin().getServer().addRecipe(explosiveTierThreeCraft);

    }

    private ShapedRecipe setupCraft(NamespacedKey key, ItemStack result, ItemStack need) {

        ShapedRecipe craft = new ShapedRecipe(key, result);
        craft.shape("xo", "oo");
        craft.setIngredient('x', need);
        craft.setIngredient('o', Material.GUNPOWDER);
        return craft;
    }

}
