package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Debugs {


    private static LocalDate DATA = LocalDate.now();
    private static File DIRETORY = new File(VGlobal.ARQUIVO+File.separator+"debugs");
    private static File FILE = new File(DIRETORY.getAbsolutePath(), "debug_" + DATA.toString().replaceAll("-","_") + ".txt");
    private static List<String> list = new ArrayList<>();

    public static void ler() {

        String txt = null;

        if(!DIRETORY.exists()){
            DIRETORY.mkdir();
        }

        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                Msg.ServidorErro("Erro ao criar o arquivo debug " + FILE.getName(), "", Debugs.class, e);
            }
        }

        list = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(FILE.toPath(), StandardCharsets.UTF_8)) {
            while ((txt = r.readLine()) != null) {
                list.add(txt);
            }
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao ler o arquivo debug " + FILE.getName(), "", Debugs.class, e);
        }
    }

    public static void escrever(@NotNull String texto) {

        ler();

        try (BufferedWriter w = Files.newBufferedWriter(FILE.toPath(), StandardCharsets.UTF_8)) {
            for (String txt : list){
                w.write(txt);
                w.newLine();
                w.flush();
            }
            w.write(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))+" - "+texto);
            w.newLine();
            w.flush();
            Msg.ServidorGold(texto);
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao escrever o arquivo debug " + FILE.getName(), "", Debugs.class, e);
        }
    }

}
