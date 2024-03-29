package teamunc.defarmers2.utils.worldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BiomeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.FileManager;
import teamunc.defarmers2.managers.GameManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;


public class ApiWorldEdit {

    // place a platform at each location in the array of a given radius
    public static void placePlatform(Location[] locations, int radius) {
        Location[] copy = locations.clone();
        for (Location location : copy) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    location.clone().add(i, 0, j).getBlock().setType(Material.STONE);
                }
            }
        }
    }

    public static void managePhase1Area(Location[] locations, boolean setup) {
        // now array of Location
        Location[] copy = new Location[locations.length];

        for (int i = 0; i < locations.length; i++) {
            copy[i] = locations[i].clone().add(25, 3, 25);
        }

        if (setup) {
            setupArea(copy, "phase1");
        } else {
            resetAreaPhase1(locations);
        }
    }



    public static void managePhase2Area(Location[] locations, boolean setup) {
        if (setup) {
            setupArea(locations, "phase2");
        } else {
            resetAreaPhase2(locations);
        }
    }

    public static void managePhase3Area(Location[] locations, boolean setup) {
        if (setup) {
            setupArea(locations, "phase3");
        } else {
            resetAreaPhase3(locations[0]);
        }
    }

    public static void setupArea(Location[] locations, String fileName) {

        // loading schematics of a given phase
        File[] filesRaw = FileManager.getInstance().getAllShematicFiles();
        ArrayList<File> files = Arrays.stream(filesRaw).filter(file -> file.getName().contains(fileName)).collect(Collectors.toCollection(ArrayList::new));

        if (files.size() == 0) {
            Defarmers2.getInstance().getLogger().log(Level.SEVERE,"No schematics found for phase " + fileName);
            placePlatform(locations, 1);
            if (fileName.contains("phase1")) setupItemsList("default_itemList");
            return;
        }

        // searching a random schematic
        File file = files.get(new Random(Defarmers2.getInstance().getGameManager().getSeedOrGenerate()).nextInt(files.size()));

        if (fileName.contains("phase1")) setupItemsList(file.getName());

        // place schematics
        for (Location location : locations) {

            ClipboardFormat format = ClipboardFormats.findByFile(file);

            if (format != null) {

                BlockVector3 pos = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                World world = new BukkitWorld(Objects.requireNonNull(location.getWorld()));


                try (ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()))) {
                    Clipboard clipboard = reader.read();
                    try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                        Operation operation = new ClipboardHolder(clipboard)
                                .createPaste(editSession)
                                .to(pos)
                                .ignoreAirBlocks(false)
                                .build();
                        Operations.complete(operation);
                    }
                    clipboard.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void resetAreaPhase1(Location[] locations) {

        for (Location location : locations) {
            for (int i = -26; i <= 26; i++) {
                for (int j = -26; j <= 26; j++) {
                    for (int k = -121; k <= 2; k++) {
                        Block blockCloned = location.clone().add(i, k, j).getBlock();
                        if (blockCloned.getType() != Material.AIR)
                            blockCloned.setType(Material.AIR);
                    }
                }
            }
        }
    }

    private static void resetAreaPhase2(Location[] locations) {

        for (Location location : locations) {
            for (int i = -16; i <= 16; i++) {
                for (int j = -16; j <= 16; j++) {
                    for (int k = -15; k <= 15; k++) {
                        Block blockCloned = location.clone().add(i, k, j).getBlock();
                        if (blockCloned.getType() != Material.AIR)
                            blockCloned.setType(Material.AIR);
                    }
                }
            }
        }
    }

    private static void resetAreaPhase3(Location location) {
        for (int i = -71; i <= 71; i++) {
            for (int j = -71; j <= 71; j++) {
                for (int k = -18; k <= 50; k++) {
                    Block blockCloned = location.clone().add(i, k, j).getBlock();
                    if (blockCloned.getType() != Material.AIR)
                        blockCloned.setType(Material.AIR);
                }
            }
        }
    }

    public static void generateCircle(int rayon, Location center, Material block) {
        for (int i = -rayon; i <= rayon; i++) {
            for (int j = -rayon; j <= rayon; j++) {
                if (Math.sqrt(i * i + j * j) <= rayon) {
                    center.clone().add(i, 0, j).getBlock().setType(block);
                }
            }
        }
    }

    public static void generateWall(int hauteur, int largeur, Location center, Material block, Direction direction) {
        for (int i = 0; i <= hauteur; i++) {
            for (int j = -largeur; j <= largeur; j++) {
                if ( direction == Direction.NORTH || direction == Direction.SOUTH) {
                    if (center.clone().add(0, i, j).getBlock().getType() != Material.BARRIER)
                        center.clone().add(0, i, j).getBlock().setType(block);
                } else {
                    if (center.clone().add(j, i, 0).getBlock().getType() != Material.BARRIER)
                        center.clone().add(j, i, 0).getBlock().setType(block);
                }
            }
        }
    }
    public static void setupItemsList(String fileName) {
        Defarmers2 plugin = Defarmers2.getInstance();
        GameManager gameManager = plugin.getGameManager();
        InGameItemsList itemsList = null;
        itemsList = gameManager.getFileManager().loadJson(fileName, InGameItemsList.class);

        if (itemsList == null) {
            plugin.getLogger().log(Level.SEVERE, "Error while loading items list, using default instead");
            itemsList = gameManager.getFileManager().loadJson("default_itemList", InGameItemsList.class);
        }
        gameManager.getGameStates().setItemsList(itemsList);
    }
}
