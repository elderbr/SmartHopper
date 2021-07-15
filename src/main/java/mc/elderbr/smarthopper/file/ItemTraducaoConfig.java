package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ItemTraducaoConfig {

    private final File FILE_DIRECTORY = new File(VGlobal.ARQUIVO, File.separator.concat("lang"));

    private InputStream input;
    private BufferedReader reader;
    private String txtReader;
    private BufferedWriter writer;

    private File fileBR = new File(FILE_DIRECTORY, "pt_br.yml");
    private File filePT = new File(FILE_DIRECTORY, "pt_pt.yml");

    private File file;
    private File fileConfig;
    private String langCod;
    private String langRegion;

    private YamlConfiguration config;
    private Traducao traducao;
    private List<String> listItemTraducaoDefault;
    private String key;
    private String value;
    private HashMap<String, String> langMap;

    public ItemTraducaoConfig() {

        Debug.WriteMsg("Tradução iniciada...");

        try {
            if (!FILE_DIRECTORY.exists()) {
                FILE_DIRECTORY.mkdir();
            }
            if (!fileBR.exists()) {
                Debug.WriteMsg("Tradução criando arquivo pt_br.yml!!!");
                fileBR.createNewFile();
                createBR();// Cria o arquivo pt_br.yml na pasta lang
            }
            if (!filePT.exists()) {
                Debug.WriteMsg("Tradução criando arquivo pt_pt.yml!!!");
                filePT.createNewFile();
                createPT();// Cria o arquivo pt_pt.yml na pasta lang
            }

            // ESTRAINDO A TRADUÇÃO DOS ARQUIVOS DIFERENTES DE .yml
            createLang();

            // CARREGA TODOS OS LANGS
            carregar();

        } catch (IOException e) {

        }

    }


    private void createBR() {

        try {

            writer = Files.newBufferedWriter(fileBR.toPath(), StandardCharsets.UTF_8);

            input = getClass().getResourceAsStream("/pt_br.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            while ((txtReader = reader.readLine()) != null) {
                writer.write(StringEscapeUtils.unescapeJava(txtReader));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    private void createPT() {

        try {

            writer = Files.newBufferedWriter(filePT.toPath(), StandardCharsets.UTF_8);

            input = getClass().getResourceAsStream("/pt_pt.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            while ((txtReader = reader.readLine()) != null) {
                writer.write(StringEscapeUtils.unescapeJava(txtReader));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    private void createLang() {
        try {
            Debug.WriteMsg("Tradução percorrendo os arquivos...");
            file = new File(FILE_DIRECTORY.getAbsolutePath());
            for (File fileList : file.listFiles()) {// Lista de arquivos na pasta lang
                if (!fileList.getName().contains(".yml")) {// Busca os arquivos diferentes de .yml
                    // Lendo os arquivos que não tem a instenção .yml
                    reader = Files.newBufferedReader(fileList.toPath(), StandardCharsets.UTF_8);// Obtem o arquivo
                    listItemTraducaoDefault = new ArrayList<>();
                    while ((txtReader = reader.readLine()) != null) {
                        // Pegando o nome da linguagem
                        if (txtReader.contains("language.code")) {// Busca o texto language.code
                            langCod = txtReader.replaceAll("\"language.code\": \"", "").replaceAll("[\",]", "").trim();
                        }
                        // Pegando o nome do Pais
                        if (txtReader.contains("language.region")) {
                            langRegion = txtReader.replaceAll("\"language.region\": \"", "").replaceAll("[\",]", "").trim();
                        }
                        // Pegando os itens, encantamentos e poções
                        if (txtReader.contains("item.minecraft")
                                || txtReader.contains("block.minecraft.")
                                || txtReader.contains("enchantment.minecraft.")
                                || txtReader.contains("potion.effect.")) {
                            if (!listItemTraducaoDefault.contains(txtReader)) {
                                listItemTraducaoDefault.add(txtReader);
                            }
                        }
                    }
                    reader.close();
                    fileList.delete();

                    // PERCORRE TODOS OS ITENS, BLOCOS, ENCANTAMENTOS E POÇÕES
                    fileConfig = new File(FILE_DIRECTORY, langCod.concat(".yml"));
                    config = YamlConfiguration.loadConfiguration(fileConfig);

                    Collections.sort(listItemTraducaoDefault);
                    for (String items : listItemTraducaoDefault) {
                        txtReader = items.replaceAll("\"", "")
                                .replaceAll("item.minecraft.", "")
                                .replaceAll("block.minecraft.", "")
                                .replaceAll("enchantment.minecraft.", "")
                                .replaceAll("_potion.effect.", "")
                                .replaceAll("potion.effect.", "")
                                .replaceAll("[_,.]", " ").trim();

                        key = txtReader.substring(0, txtReader.indexOf(":")).trim();
                        value = txtReader.substring(txtReader.indexOf(":")+2,txtReader.length());
                        config.set(key, Utils.ToUTF(value));
                        config.save(fileConfig);
                    }
                }
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "createLang()", getClass());
        }
    }

    private void carregar(){
        Debug.WriteMsg("Carregando as traduções....");
        file = new File(FILE_DIRECTORY.getAbsolutePath());
        for (File fileList : file.listFiles()) {// Lista de arquivos na pasta lang
            if (fileList.getName().contains(".yml")) {// Busca os arquivos diferentes de .yml
                config = YamlConfiguration.loadConfiguration(fileList);
                for(Map.Entry<String, Object> langs : config.getValues(false).entrySet()) {

                    langCod = fileList.getName().toString().replaceAll(".yml","");

                    langMap = new HashMap<>();
                    langMap.put(langCod, langs.getValue().toString());

                    // ADICIONANDO A TRADUÇÃO A LISTA DE ITENS
                    if(VGlobal.ITEM_LANG_MAP.get(langs.getKey())==null) {
                        VGlobal.ITEM_LANG_MAP.put(langs.getKey(), langMap);
                    }else{
                        VGlobal.ITEM_LANG_MAP.get(langs.getKey()).put(langCod, langs.getValue().toString());
                    }
                }
            }
        }
        Debug.WriteMsg("Traduções carregadas com sucesso!!!");
    }
}
