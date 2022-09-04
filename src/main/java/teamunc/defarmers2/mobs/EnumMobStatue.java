package teamunc.defarmers2.mobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.UUID;

public enum EnumMobStatue {
    CONFUSE,
    BEEE_EVIL,
    BEEE,
    FOLLOWING_PLAYER
    ;

    public void action(ArrayList<UUID> mobs) {
        switch (this) {

            case CONFUSE:
                this.actionConfuse(mobs);
                break;
            case BEEE_EVIL:
                break;
            case BEEE:
                break;
            case FOLLOWING_PLAYER:
                break;
        }
    }

    public void init(ArrayList<UUID> mobs) {
        switch (this) {
            case CONFUSE:
                this.initConfuse(mobs);
                break;
            case BEEE_EVIL:
                break;
            case BEEE:
                break;
            case FOLLOWING_PLAYER:
                break;
        }
    }

    public void end(ArrayList<UUID> mobs) {
        switch (this) {
            case CONFUSE:
                this.endConfuse(mobs);
                break;
            case BEEE_EVIL:
                break;
            case BEEE:
                break;
            case FOLLOWING_PLAYER:
                break;
        }
    }

    private void actionConfuse(ArrayList<UUID> mobs) {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        GameStates gameStates = gameManager.getGameStates();
        CustomMobsManager customMobsManager = CustomMobsManager.getInstance();

        for (UUID uuid : mobs) {
            Entity nextTarget = null;
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                // update name
                String newName;
                newName = ChatColor.BLACK + "Confused " + ChatColor.RESET + mob.getCustomName();

                mob.setCustomName(newName);
                mob.setCustomNameVisible(true);

                // update target
                if (!gameStates.hasMobTargeting(uuid)) {
                    // set a new target
                    nextTarget = customMobsManager.getNearestLivingEntity(null, mob, null);
                    if (nextTarget != null) gameStates.setMobTargeting(uuid, nextTarget.getUniqueId());
                } else {
                    UUID targetedUuid = gameStates.getMobTargeting(uuid);
                    Entity targetedEntity = Bukkit.getEntity(targetedUuid);

                    if (targetedEntity != null) {
                        nextTarget = targetedEntity;
                    } else {
                        // targeted entity is dead
                        gameStates.removeMobTargeting(targetedUuid);
                        nextTarget = customMobsManager.getNearestLivingEntity(null, mob, null);
                    }
                }
            }

            if (nextTarget != null) {
                mob.setTarget((LivingEntity) nextTarget);
            }
        }
    }

    private void initConfuse(ArrayList<UUID> mobs) {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        GameStates gameStates = gameManager.getGameStates();
        CustomMobsManager customMobsManager = CustomMobsManager.getInstance();

        for (UUID uuid : mobs) {
            LivingEntity nextTarget = null;
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                    // set a new first target
                    nextTarget = customMobsManager.getNearestLivingEntity(null, mob, null);
                    if (nextTarget != null) gameStates.setMobTargeting(uuid, nextTarget.getUniqueId());
            }

            if (nextTarget != null) {
                mob.setTarget(nextTarget);
            }
        }
    }

    private void endConfuse(ArrayList<UUID> mobs) {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        GameStates gameStates = gameManager.getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                // update name
                String newName;
                newName = ChatColor.WHITE + "Cured " + ChatColor.RESET + mob.getCustomName();

                mob.setCustomName(newName);
                mob.setCustomNameVisible(true);

                // update target
                if (gameStates.hasMobTargeting(uuid)) {
                    // set a new target
                    gameStates.setMobTargeting(uuid, null);
                }
            }
        }
    }

}
