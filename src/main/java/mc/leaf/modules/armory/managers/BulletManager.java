package mc.leaf.modules.armory.managers;

import mc.leaf.core.async.LeafManager;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.generics.Bullet;

import java.util.ArrayList;
import java.util.Collection;

public class BulletManager extends LeafManager<Bullet> {

    public BulletManager(ILeafModule module) {

        super(module);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        Collection<Bullet> bullets = new ArrayList<>();
        while (!this.getHolder().isEmpty()) {
            Bullet entity = this.getHolder().poll();
            if (entity != null) {
                entity.tick();

                if (entity.isAlive()) {
                    bullets.add(entity);
                }
            }
        }
        bullets.forEach(this::offer);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
