package mc.leaf.modules.armory.events;

import mc.leaf.modules.armory.generics.WeaponWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerUseWeaponEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers  = new HandlerList();
    private              boolean     cancelled = false;

    public PlayerUseWeaponEvent(@NotNull Player who, WeaponWrapper wrapper) {

        super(who);
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {

        return handlers;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {

        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins.
     *
     * @param cancel
     *         true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {

        this.cancelled = true;
    }

}
