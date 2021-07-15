package mc.elderbr.smarthopper.utils;

import mc.elderbr.smarthopper.interfaces.VGlobal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Debug {

    private static BufferedReader reader;
    private static File path = new File(VGlobal.ARQUIVO.getAbsolutePath() + "/debug");
    private static File file = new File(path, Utils.toData().replaceAll("/", "").concat(".txt"));
    private static String txtReader;
    private static List<String> debugTxt;

    public static void Write(String msg) {

        try {
            if (!path.exists()) {
                path.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "Debug(String msg)", Debug.class);
        }

        ler();

        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {

            for (String txt : debugTxt) {
                w.write(txt);
                w.newLine();
            }

            w.write(Utils.toData()+" - "+msg);
            w.flush();
        } catch (IOException e) {
            Msg.ServidorErro(e, "Debug(String msg)", Debug.class);
        }
    }

    public static void WriteMsg(String msg) {

        try {
            if (!path.exists()) {
                path.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "Debug(String msg)", Debug.class);
        }

        ler();

        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {

            for (String txt : debugTxt) {
                w.write(txt);
                w.newLine();
            }

            w.write(Utils.toData()+" - "+msg);
            w.flush();

            Msg.ServidorGreen(msg);// MENSAGEM QUE VAI SER EXIBIDA NO CONSOLE

        } catch (IOException e) {
            Msg.ServidorErro(e, "Debug(String msg)", Debug.class);
        }
    }

    private static void ler() {
        try {
            debugTxt = new ArrayList<>();
            reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            while ((txtReader = reader.readLine()) != null) {
                debugTxt.add(txtReader);
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "ler()", Debug.class);
        }
    }

}
