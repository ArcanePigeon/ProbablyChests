package org.cloudwarp.mobscarecrow.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.mobscarecrow.MobScarecrow;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class Config {
	private static Config instance = null;

	public NbtCompound configData;
	private final Logger LOGGER = MobScarecrow.LOGGER;
	private static final String CONFIG_FILE = "config/mobscarecrow.json";
	private int difference = 0;

	private Config() {
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	public int getScarecrowRadius (){
		return configData.getInt("mob_scarecrow_radius");
	}

	public int getIntOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getInt(key);
		} else {
			++difference;
			return defaults.getInt(key);
		}
	}

	public boolean getBooleanOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getBoolean(key);
		} else {
			++difference;
			return defaults.getBoolean(key);
		}
	}

	private String getStringOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getString(key);
		} else {
			++difference;
			return defaults.getString(key);
		}
	}

	private NbtCompound getCompoundOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getCompound(key);
		} else {
			++difference;
			return defaults.getCompound(key);
		}
	}

	private double getDoubleOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getDouble(key);
		} else {
			++difference;
			return defaults.getDouble(key);
		}
	}

	private float getFloatOrDefault(NbtCompound getFrom, String key, NbtCompound defaults) {
		if (getFrom.contains(key)) {
			return getFrom.getFloat(key);
		} else {
			++difference;
			return defaults.getFloat(key);
		}
	}

	public int getIntOrDefault(JsonObject getFrom, String key, NbtCompound defaults) {
		if (getFrom.has(key)) {
			return getFrom.get(key).getAsInt();
		} else {
			++difference;
			return defaults.getInt(key);
		}
	}

	public boolean getBooleanOrDefault(JsonObject getFrom, String key, NbtCompound defaults) {
		if (getFrom.has(key)) {
			return getFrom.get(key).getAsBoolean();
		} else {
			++difference;
			return defaults.getBoolean(key);
		}
	}

	private String getStringOrDefault(JsonObject getFrom, String key, NbtCompound defaults) {
		if (getFrom.has(key)) {
			return getFrom.get(key).getAsString();
		} else {
			++difference;
			return defaults.getString(key);
		}
	}

	private double getDoubleOrDefault(JsonObject getFrom, String key, NbtCompound defaults) {
		if (getFrom.has(key)) {
			return getFrom.get(key).getAsDouble();
		} else {
			++difference;
			return defaults.getDouble(key);
		}
	}

	private float getFloatOrDefault(JsonObject getFrom, String key, NbtCompound defaults) {
		if (getFrom.has(key)) {
			return getFrom.get(key).getAsFloat();
		} else {
			++difference;
			return defaults.getFloat(key);
		}
	}

	private NbtCompound getDefaults() {
		NbtCompound defaultConfig = new NbtCompound();

		defaultConfig.putInt("mob_scarecrow_radius", 8);

		return defaultConfig;
	}

	private JsonObject toJson(NbtCompound tag) {
		JsonObject json = new JsonObject();

		NbtCompound defaults = getDefaults();

		json.addProperty("mob_scarecrow_radius", getIntOrDefault(tag, "mob_scarecrow_radius", defaults));

		createFile(json, difference > 0);
		difference = 0;
		return json;
	}

	public static JsonObject getJsonObject(String json) {
		return JsonParser.parseString(json).getAsJsonObject();
	}
	private NbtCompound toNbtCompound(JsonObject json) {
		NbtCompound tag = new NbtCompound();

		NbtCompound defaults = getDefaults();

		tag.putInt("mob_scarecrow_radius", getIntOrDefault(json, "mob_scarecrow_radius", defaults));

		createFile(toJson(tag), difference > 0);
		difference = 0;
		return tag;
	}

	public static String readFile(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\Z");
		var result = scanner.next();
		scanner.close();
		return result;
	}

	@SuppressWarnings("UnusedReturnValue")
	public boolean loadConfig() {
		try {
			return loadConfig(getJsonObject(readFile(new File(CONFIG_FILE))));
		} catch (Exception e) {
			LOGGER.info("Found error with config. Using default config.");
			this.configData = getDefaults();
			createFile(toJson(this.configData), true);
			return false;
		}
	}

	private boolean loadConfig(JsonObject fileConfig) {
		try {
			this.configData = toNbtCompound(fileConfig);
			return true;
		} catch (Exception e) {
			LOGGER.info("Found error with config. Using default config.");
			this.configData = getDefaults();
			createFile(toJson(this.configData), true);
			return false;
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	public boolean loadConfig(NbtCompound config) {
		try {
			this.configData = config;
			return true;
		} catch (Exception e) {
			LOGGER.info("Found error with config. Using default config.");
			this.configData = getDefaults();
			createFile(toJson(this.configData), true);
			return false;
		}
	}

	private void createFile(JsonObject contents, boolean overwrite) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		contents = JsonParser.parseString(gson.toJson(contents)).getAsJsonObject();

		File file = new File(Config.CONFIG_FILE);
		if (file.exists() && !overwrite) {
			return;
		}
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		file.setReadable(true);
		file.setWritable(true);
		if (contents == null) {
			return;
		}
		try (FileWriter writer = new FileWriter(file)) {
			String json = gson.toJson(contents).replace("\n", "").replace("\r", "");
			writer.write(gson.toJson(JsonParser.parseString(json).getAsJsonObject()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void print(ServerPlayerEntity player) {
		var q = new LinkedList<JsonObject>();
		q.add(toJson(configData));
		while (!q.isEmpty()) {
			var current = q.poll();
			for (var entry : current.entrySet()) {
				var key = entry.getKey();
				var value = entry.getValue();
				if (value.isJsonObject()) {
					q.add(value.getAsJsonObject());
					continue;
				}
				player.sendMessage(new LiteralText("§6[§e" + key + "§6] §3 " + value), false);
			}
		}
	}
}
