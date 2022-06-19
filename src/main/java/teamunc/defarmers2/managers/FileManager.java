package teamunc.defarmers2.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.utils.gsonAdapter.LocationAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class FileManager extends Manager {
    private File pluginDataFile;

    private GsonBuilder gsonBuilder;


    // SINGLETON
    private static FileManager instance;

    public static FileManager getInstance() {
        return instance;
    }
    private FileManager() {
        super();
    }

    public static void enable() {
        instance = new FileManager();
    }

    @Override
    public void onInit() {
        this.pluginDataFile = plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init GsonBuilder
        this.gsonBuilder = new GsonBuilder();
    }

    @Override
    public void onDisable() {

    }

    public File[] getAllShematicFiles() {
        return pluginDataFile.listFiles((dir, name) -> name.endsWith(".schematic"));
    }

    public boolean fileExists(String fileName) {
        return pluginDataFile.exists() && new File(pluginDataFile + "/" + fileName + ".json").exists();
    }

    public Gson getGson() {
        return this.gsonBuilder
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .create();
    }

    public <T> T loadJson(String fileName, Type type) {
        Defarmers2.getInstance().getLogger().log(Level.INFO,"[FileManager] loadJson: " + fileName);
        try {
            Reader reader = Files.newBufferedReader(Paths.get(pluginDataFile + "/" + fileName + ".json"), StandardCharsets.UTF_8);
            return getGson().fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void saveJson(String fileName, Object object) {
        Defarmers2.getInstance().getLogger().log(Level.INFO,"[FileManager] saveJson: " + fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pluginDataFile + "/" + fileName + ".json");
            fileOutputStream.write(getGson().toJson(object).getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
