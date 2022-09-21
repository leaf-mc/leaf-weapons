package mc.leaf.modules.armory;

import mc.leaf.core.api.command.PluginCommandImpl;
import mc.leaf.core.interfaces.ILeafCore;
import mc.leaf.core.interfaces.impl.LeafModule;
import mc.leaf.modules.armory.enums.AmmoType;
import mc.leaf.modules.armory.enums.WeaponType;
import mc.leaf.modules.armory.listeners.ArmoryPlayerWeaponListener;
import mc.leaf.modules.armory.listeners.ArmoryWeaponLootListener;
import mc.leaf.modules.armory.managers.ArmoryDisplayManager;
import mc.leaf.modules.armory.managers.BulletManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmoryModule extends LeafModule {

    public static final String PREFIX = "§l[§aLeaf§Armory§r§l]§r";

    private BulletManager        bulletManager;
    private ArmoryDisplayManager armoryDisplayManager;

    public ArmoryModule(JavaPlugin plugin, ILeafCore core) {

        super(core, plugin);
        this.getCore().registerDynamicOptions("weaponType", WeaponType.autocomplete());
        this.getCore().registerDynamicOptions("ammoType", AmmoType.autocomplete());
    }

    @Override
    public void onEnable() {

        this.getPlugin().getLogger().info("Registering managers...");
        this.bulletManager        = new BulletManager(this);
        this.armoryDisplayManager = new ArmoryDisplayManager(this);
        this.bulletManager.start();
        this.armoryDisplayManager.start();

        this.getPlugin().getLogger().info("Registering recipes...");
        this.createRecipes();
        this.getPlugin().getLogger().info("Armory has been enabled.");
    }

    @Override
    public void onDisable() {

        this.getPlugin().getLogger().info("Stopping managers...");
        this.bulletManager.stop();
        this.armoryDisplayManager.stop();

        this.getPlugin().getLogger().info("Cleaning up...");
        this.bulletManager        = null;
        this.armoryDisplayManager = null;

        this.getPlugin().getLogger().info("Armory has been disabled.");
    }

    @Override
    public String getName() {

        return "Armory";
    }

    @Override
    public List<Listener> getListeners() {

        return Arrays.asList(new ArmoryWeaponLootListener(this), new ArmoryPlayerWeaponListener(this));
    }

    @Override
    public Map<String, PluginCommandImpl> getCommands() {

        return new HashMap<>() {{
            this.put("armory", new ArmoryCommand(ArmoryModule.this));
        }};
    }

    public BulletManager getBulletManager() {

        return this.bulletManager;
    }

    private void createRecipes() {

        ItemStack normalTierOne      = ArmoryGenerator.getRiffleAmmo(this, 1);
        ItemStack normalTierTwo      = ArmoryGenerator.getRiffleAmmo(this, 2);
        ItemStack normalTierThree    = ArmoryGenerator.getRiffleAmmo(this, 3);
        ItemStack explosiveTierOne   = ArmoryGenerator.getRocketLauncherAmmo(this, 1);
        ItemStack explosiveTierTwo   = ArmoryGenerator.getRocketLauncherAmmo(this, 2);
        ItemStack explosiveTierThree = ArmoryGenerator.getRocketLauncherAmmo(this, 3);

        NamespacedKey normalTierOneKey      = ArmoryKeys.normalAmmunition(this, 1);
        NamespacedKey normalTierTwoKey      = ArmoryKeys.normalAmmunition(this, 2);
        NamespacedKey normalTierThreeKey    = ArmoryKeys.normalAmmunition(this, 3);
        NamespacedKey explosiveTierOneKey   = ArmoryKeys.explosiveAmmunition(this, 1);
        NamespacedKey explosiveTierTwoKey   = ArmoryKeys.explosiveAmmunition(this, 2);
        NamespacedKey explosiveTierThreeKey = ArmoryKeys.explosiveAmmunition(this, 3);

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
