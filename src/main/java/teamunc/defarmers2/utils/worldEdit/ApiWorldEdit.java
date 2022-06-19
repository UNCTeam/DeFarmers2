package teamunc.defarmers2.utils.worldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.FileManager;
import teamunc.defarmers2.managers.GameManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;


public class ApiWorldEdit {
    // place block at each location in the array
    public static void placeBlocks(Location[] locations, Material block) {
        for (Location location : locations) {
            location.getBlock().setType(block);
        }
    }

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

    public static void setupPhase1Area(Location[] locations) {
        setupArea(locations, "phase1");
    }

    public static void setupPhase2Area(Location[] locations) {
        setupArea(locations, "phase2");
    }

    public static void setupPhase3Area(Location[] locations) {
        setupArea(locations, "phase3");
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
        File file = files.get((int) (Math.random() * files.size()));

        if (fileName.contains("phase1")) setupItemsList(file.getName());

        // place schematics
        for (Location location : locations) {

            ClipboardFormat format = ClipboardFormats.findByFile(file);

            try {
                assert format != null;
                try (ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()))) {
                    Clipboard clipboard = reader.read();

                    int x = location.getBlockX();
                    int y = location.getBlockY();
                    int z = location.getBlockZ();

                    try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().limitUnlimited().build()) {
                        Operation operation = new ClipboardHolder(clipboard)
                                .createPaste(editSession)
                                .to(BlockVector3.at(x, y, z))
                                .ignoreAirBlocks(false)
                                .build();
                        Operations.complete(operation);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setupItemsList(String fileName) {
        Defarmers2 plugin = Defarmers2.getInstance();
        GameManager gameManager = plugin.getGameManager();
        InGameItemsList itemsList = gameManager.getFileManager().loadJson(fileName, InGameItemsList.class);

        gameManager.getGameStates().setItemsList(itemsList);

    }
}
