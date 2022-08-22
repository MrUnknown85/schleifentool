/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nico
 */
public class Log {

    private static PrintStream log;

    static {
        try {
            log = new PrintStream("log.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void d(String message) {
        System.out.println(message);
        if (log != null) {
            log.print(message + "\n");
        }
    }

    public static void err(Throwable throwable) {
        File errorFolder = new File("errors");
        Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, throwable);
        if (!errorFolder.exists()) {
            errorFolder.mkdir();
        }
        try ( PrintStream out = new PrintStream(errorFolder.getPath() + "/error" + System.currentTimeMillis() + ".txt")) {
            throwable.printStackTrace(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
