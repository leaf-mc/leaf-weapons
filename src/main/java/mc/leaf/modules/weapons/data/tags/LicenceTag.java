package mc.leaf.modules.weapons.data.tags;

import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.weapons.data.PersistentData;
import mc.leaf.modules.weapons.data.entities.Licence;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LicenceTag extends PersistentData<Licence> {

    public LicenceTag(ILeafModule module) {

        super(module);
    }

    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @NotNull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {

        return PersistentDataContainer.class;
    }

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @NotNull
    @Override
    public Class<Licence> getComplexType() {

        return Licence.class;
    }

    /**
     * Returns the primitive data that resembles the complex object passed to this method.
     *
     * @param complex
     *         the complex object instance
     * @param context
     *         the context this operation is running in
     *
     * @return the primitive value
     */
    @NotNull
    @Override
    public PersistentDataContainer toPrimitive(@NotNull Licence complex, @NotNull PersistentDataAdapterContext context) {

        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(this.getKey("level"), PersistentDataType.INTEGER, complex.getLevel());
        return container;
    }

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive
     *         the primitive value
     * @param context
     *         the context this operation is running in
     *
     * @return the complex object instance
     */
    @NotNull
    @Override
    public Licence fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {

        Licence licence = new Licence();
        licence.setLevel(primitive.get(this.getKey("level"), PersistentDataType.INTEGER));
        return licence;
    }

}
