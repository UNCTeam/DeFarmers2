package teamunc.defarmers2.mobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public enum EnumMobStatue implements Serializable {
    CONFUSE,
    BEEE_EVIL,
    BEEE,
    FOLLOWING_PLAYER
    ;

    private UUID triggeringPlayer;
    public void action(ArrayList<UUID> mobs) {
        switch (this) {

            case CONFUSE:
                this.actionConfuse(mobs);
                break;
            case BEEE_EVIL:
                this.actionBeeeEvil(mobs);
                break;
            case BEEE:
                this.actionBeee(mobs);
                break;
            case FOLLOWING_PLAYER:
                actionFollowingPlayer(mobs);
                break;
        }
    }

    public void init(ArrayList<UUID> mobs, UUID triggeringPlayer) {
        this.triggeringPlayer = triggeringPlayer;
        switch (this) {
            case CONFUSE:
                this.initConfuse(mobs);
                break;
            case BEEE_EVIL:
                this.initBeeeEvil(mobs);
                break;
            case BEEE:
                this.initBeee(mobs);
                break;
            case FOLLOWING_PLAYER:
                initFollowingPlayer(mobs);
                break;
        }
    }

    public void end(ArrayList<UUID> mobs) {
        switch (this) {
            case CONFUSE:
                this.endConfuse(mobs);
                break;
            case BEEE_EVIL:
                this.endBeeeEvil(mobs);
                break;
            case BEEE:
                this.endBeee(mobs);
                break;
            case FOLLOWING_PLAYER:
                endFollowingPlayer(mobs);
                break;
        }
    }

    /// CONFUSE ///
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

    /// FOLLOW FOLLOWING_PLAYER ///
    private void actionFollowingPlayer(ArrayList<UUID> mobs) {
        // No.
    }
    private void initFollowingPlayer(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            LivingEntity nextTarget = (Player) Bukkit.getEntity(triggeringPlayer);
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                if (nextTarget != null) {
                    gameStates.setMobTargeting(uuid, nextTarget.getUniqueId());
                    mob.setTarget(nextTarget);
                }
            }
        }
    }
    private void endFollowingPlayer(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                // update target
                if (gameStates.hasMobTargeting(uuid)) {
                    // reset target
                    gameStates.setMobTargeting(uuid, null);
                }
            }
        }
    }

    /// BEEEEE ///
    private void actionBeee(ArrayList<UUID> mobs) {
        // No.
    }
    private void initBeee(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                gameStates.removeAllAgressor(uuid);
                gameStates.addMobUntouchable(uuid);
                mob.setAI(false);
                mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, false, false));
                mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 100, false, false));

                LivingEntity sheep = mob.getLocation().getWorld().spawn(mob.getLocation(), Sheep.class);
                sheep.setAI(false);
            }
        }
    }
    private void endBeee(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                gameStates.removeMobUntouchable(uuid);
                mob.setAI(true);
                mob.removePotionEffect(PotionEffectType.INVISIBILITY);
                mob.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                mob.getLocation().getWorld().getNearbyEntities(mob.getLocation(),2,2,2).forEach(entity -> {
                    if (entity instanceof Sheep) {
                        entity.remove();
                    }
                });

                gameStates.setMobTargeting(uuid, null);
                mob.setTarget(null);
            }
        }
    }

    /// BEEEEE EVIL ///
    private void actionBeeeEvil(ArrayList<UUID> mobs) {
        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                mob.getLocation().getWorld().getNearbyEntities(mob.getLocation(),2,6,2).forEach(entity -> {
                    if (entity instanceof Sheep) {
                        entity.getLocation().setYaw(mob.getLocation().getYaw()+10);
                    }
                });
            }
        }
    }

    private void initBeeeEvil(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                gameStates.removeAllAgressor(uuid);
                gameStates.addMobUntouchable(uuid);
                mob.setAI(false);
                mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 100, false, false));

                Sheep sheep = mob.getLocation().getWorld().spawn(mob.getLocation().clone().add(0,3,0), Sheep.class);
                sheep.setAI(false);
                sheep.setColor(DyeColor.RED);
            }
        }
    }
    private void endBeeeEvil(ArrayList<UUID> mobs) {
        GameStates gameStates = Defarmers2.getInstance().getGameManager().getGameStates();

        for (UUID uuid : mobs) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);

            if (mob != null) {
                gameStates.removeMobUntouchable(uuid);
                mob.setAI(true);
                mob.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                mob.getLocation().getWorld().getNearbyEntities(mob.getLocation(),2,6,2).forEach(entity -> {
                    if (entity instanceof Sheep) {
                        entity.remove();
                    }
                });

                gameStates.setMobTargeting(uuid, null);
                mob.setTarget(null);
            }
        }
    }
}
