package mc.leaf.modules.weapons.data;

import mc.leaf.core.interfaces.ILeafModule;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class PersistentData<T> implements PersistentDataType<PersistentDataContainer, T> {

    private final ILeafModule module;

    protected PersistentData(ILeafModule module) {

        this.module = module;
    }

    public ILeafModule getModule() {

        return module;
    }

    public NamespacedKey getKey(String key) {

        return new NamespacedKey(this.module.getPlugin(), key);
    }

}
