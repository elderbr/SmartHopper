package mc.elderbr.smarthopper.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static String NAME_ARRAY(String[] args) {
        StringBuilder txt = new StringBuilder();
        for (String name : args) {
            txt.append(name + " ");
        }
        return txt.toString().trim();
    }

    public static String ToUTF(String obj) {
        return StringUtils.capitalize(obj);
    }

    public static String toUP(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    public static String toData() {
        Calendar data = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(data.getTime());
    }

    public static String adicionarEspacoAntesMaiusculas(String name) {
        StringBuilder novoNome = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char letra = name.charAt(i);
            // Verifica se o caractere é maiúsculo e não é o primeiro caractere
            if (Character.isUpperCase(letra) && i != 0) {
                novoNome.append(" ");  // Adiciona espaço antes da letra maiúscula
            }
            novoNome.append(letra);
        }
        return novoNome.toString().toLowerCase();
    }

    public static boolean isValidURL(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            huc.setConnectTimeout(3000); // Tempo limite de conexão (3 segundos)
            huc.connect();

            int responseCode = huc.getResponseCode();
            return (responseCode >= 200 && responseCode < 400); // Respostas 2xx e 3xx são válidas
        } catch (MalformedURLException e) {
            return false; // A URL não é válida
        } catch (IOException e) {
            return false; // A URL não pôde ser acessada
        }
    }
}
