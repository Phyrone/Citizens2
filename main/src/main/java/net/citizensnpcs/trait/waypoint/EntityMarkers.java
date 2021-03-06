package net.citizensnpcs.trait.waypoint;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.google.common.collect.Maps;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

/**
 * A helper class for storing a number of entity markers. By default an entity marker is a non-persisted EnderSignal.
 */
public class EntityMarkers<T> {
    private final Map<T, Entity> markers = Maps.newHashMap();
    private final NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());

    /**
     * Creates and persists (in memory) an {@link Entity} marker.
     *
     * @param marker
     *            the storage marker
     * @param at
     *            the spawn location
     * @return the created entity
     */
    public Entity createMarker(T marker, Location at) {
        Entity entity = spawnMarker(at.getWorld(), at);
        if (entity == null)
            return null;
        markers.put(marker, entity);
        return entity;
    }

    public void destroyMarkers() {
        for (Entity entity : markers.values()) {
            entity.remove();
        }
        markers.clear();
    }

    public void removeMarker(T marker) {
        Entity entity = markers.remove(marker);
        if (entity != null) {
            entity.remove();
        }
    }

    /**
     * Spawns a marker {@link Entity} without storing it for later use.
     *
     * @param world
     *            the world (unused currently)
     * @param at
     *            the location
     * @return the spawned entity
     */
    public Entity spawnMarker(World world, Location at) {
        NPC npc = registry.createNPC(EntityType.ENDER_SIGNAL, "");
        npc.spawn(at.clone().add(0.5, 0, 0.5), SpawnReason.CREATE);
        return npc.getEntity();
    }
}