package StevenDimDoors.dimdoors.helpers;

import StevenDimDoors.dimdoors.mod_pocketDim;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class copyfile {

    public static boolean copyFile(String ori, String dest) {
        try {
            OutputStream out;
            //Note: For this to work properly, you must use getClass() on an instance of the class,
            //not on the value obtained from .class. That was what caused this code to fail before.
            //SchematicLoader didn't have this problem because we used instances of it.
            try (InputStream in = mod_pocketDim.instance.getClass().getResourceAsStream(ori)) {
                out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Unable to get resource: " + ori);
            return false;
        }
        return true;
    }

    private copyfile() {
    }
}
