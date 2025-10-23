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

public class HellasFormsInfoConfig {
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File configFile;

    private String version = null;
    private String[] dependencies = new String[0];
    private String[] features = new String[0];

    private boolean valid = false;

    /**
     * Lade Konfiguration aus dem Serververzeichnis (config/hellasforms.json).
     * Falls nicht vorhanden oder ungültig, werden Defaults aus resources verwendet.
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

        // Wenn keine gültige Serverdatei, Defaults nutzen
        loadDefaultsFromResource();
    }

    /**
     * Lade Default-Config aus Ressourcen: config/hellasforms.json (classpath).
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

    public boolean isValid() { return valid; }

    public void save() {
        if (configFile == null) return;
        try (Writer writer = Files.newBufferedWriter(configFile.toPath(), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersion() { return version != null ? version : "0.0.0"; }
    public String[] getDependencies() { return dependencies != null ? dependencies : new String[0]; }
    public String[] getFeatures() { return features != null ? features : new String[0]; }
}