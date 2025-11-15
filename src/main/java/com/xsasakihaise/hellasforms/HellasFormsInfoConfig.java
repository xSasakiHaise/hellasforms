package com.xsasakihaise.hellasforms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Minimal JSON-backed config file that exposes metadata about the installed
 * HellasForms build (version number, dependency list and exported features).
 *
 * <p>The file lives at {@code config/hellasforms.json} on a server install and
 * mirrors the resource bundled with the mod if it does not exist yet.</p>
 */
public class HellasFormsInfoConfig {
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File configFile;

    private String version = null;
    private String[] dependencies = new String[0];
    private String[] features = new String[0];

    private boolean valid = false;

    /**
     * Loads the configuration from the given server root directory.
     * If the file does not exist (or fails to parse) a default copy from
     * the mod resources will be used instead.
     */
    public void load(File serverRoot) {
        File configDir = new File(serverRoot, "config");
        if (!configDir.exists()) configDir.mkdirs();

        configFile = new File(configDir, "hellasforms.json");

        if (configFile.exists()) {
            try (Reader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
                HellasFormsInfoConfig loaded = gson.fromJson(reader, HellasFormsInfoConfig.class);
                if (loaded != null) {
                    this.version = loaded.version;
                    this.dependencies = loaded.dependencies != null ? loaded.dependencies : new String[0];
                    this.features = loaded.features != null ? loaded.features : new String[0];
                    this.valid = true;
                    return;
                }
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                this.valid = false;
                return;
            }
        }

        // Fallback to bundled defaults if we could not read a server side file.
        loadDefaultsFromResource();
    }

    /**
     * Populates the config object with the JSON file that ships with the mod jar.
     */
    public void loadDefaultsFromResource() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config/hellasforms.json")) {
            if (is != null) {
                try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    HellasFormsInfoConfig loaded = gson.fromJson(isr, HellasFormsInfoConfig.class);
                    if (loaded != null) {
                        this.version = loaded.version;
                        this.dependencies = loaded.dependencies != null ? loaded.dependencies : new String[0];
                        this.features = loaded.features != null ? loaded.features : new String[0];
                        this.valid = true;
                        return;
                    }
                }
            } else {
                this.valid = false;
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            this.valid = false;
        }
    }

    /**
     * @return {@code true} when the config information has been populated either
     * from disk or from the default resource file.
     */
    public boolean isValid() { return valid; }

    public void save() {
        if (configFile == null) return;
        try (Writer writer = Files.newBufferedWriter(configFile.toPath(), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return declared version string or a placeholder when not configured.
     */
    public String getVersion() { return version != null ? version : "0.0.0"; }

    /**
     * @return the human readable dependency list advertised to server staff.
     */
    public String[] getDependencies() { return dependencies != null ? dependencies : new String[0]; }

    /**
     * @return bullet-point friendly list of features (mirrors FEATURES.md contents).
     */
    public String[] getFeatures() { return features != null ? features : new String[0]; }
}