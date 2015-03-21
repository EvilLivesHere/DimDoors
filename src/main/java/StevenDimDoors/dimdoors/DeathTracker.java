package StevenDimDoors.dimdoors;

import cpw.mods.fml.common.FMLLog;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class DeathTracker {

    private static final ArrayList<String> usernameList = new ArrayList<String>(0);
    private static final HashSet<String> usernameSet = new HashSet<String>(0);
    private String filePath;
    private boolean modified;

    public DeathTracker(String filePath) {
        reset(filePath);
        readFromFile();
    }

    public void reset(String f) {
        usernameList.clear();
        usernameSet.clear();
        filePath = f;
        modified = false;
    }

    private void readFromFile() {
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        usernameSet.add(line);
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            FMLLog.warning("An unexpected exception occurred while trying to read DeathTracker data:");
            FMLLog.warning(e.toString());
        }
        usernameList.addAll(usernameSet);
    }

    public void writeToFile() {
        try {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(filePath);
                for (String username : usernameList) {
                    writer.println(username);
                }
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            modified = false;
        } catch (FileNotFoundException e) {
            FMLLog.warning("An unexpected exception occurred while trying to read DeathTracker data:");
            FMLLog.warning(e.toString());
        }
    }

    public boolean isModified() {
        return modified;
    }

    public static boolean isEmpty() {
        return usernameList.isEmpty();
    }

    public static String getRandomUsername(Random random) {
        if (usernameList.isEmpty()) {
            throw new IllegalStateException("Cannot retrieve a random username from an empty list.");
        }
        return usernameList.get(random.nextInt(usernameList.size()));
    }

    public boolean addUsername(String username) {
        if (usernameSet.add(username)) {
            usernameList.add(username);
            modified = true;
            return true;
        }
        return false;
    }
}
