package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.dao.ItemDao;
import mc.elderbr.smarthopper.dao.LangDao;
import mc.elderbr.smarthopper.dao.TraducaoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class TraducaoConfig {

    private static File directory = new File(VGlobal.ARQUIVO + File.separator + "langs");

    private File[] listFile = directory.listFiles();
    private String nameFile;
    private String lang;
    private String[] itemsName;
    private String itemName;

    private Item item;
    private ItemDao itemDao;

    private LangDao langDao;
    private TraducaoDao traducaoDao;

    public TraducaoConfig() {

        // VERIFICA SE EXISTE O DIRETORIO DO LANGS
        if (!directory.exists()) {
            directory.mkdir();
        }

        // INSTANCIANDO A CLASSE DO BANCO
        itemDao = new ItemDao();
        langDao = new LangDao();
        traducaoDao = new TraducaoDao();

        // SE EXISTIR MAIS DE UM ARQUIVO LANG
        if (listFile.length > 0) {
            for (File file : listFile) {
                lang = file.getName().substring(0, file.getName().lastIndexOf('_'));// PEGA O NOME DO LANG
                langDao.insert(lang);// ADICIONA O LANG SE NÃO EXITIR

                // ADICIONANDO O LINGUAGEM NA LISTA DA CONFIGURAÇÃO

                // PERCORRENDO O ARQUIVO LANG
                try (BufferedReader ler = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                    while ((itemName = ler.readLine()) != null) {
                        itemsName = itemName.split(":");

                        // CRIANDO NOVO ITEM
                        item = new Item();
                        item.setName(itemsName[0]);
                        item.setDsLang(lang);
                        // BUSCA O ITEM PEGANDO O SEU CÓDIGO
                        item = itemDao.select(item);
                        if (item == null) {
                            continue;
                        }
                        item.setDsTraducao(itemsName[1].replaceAll("\"", "").trim());
                        traducaoDao.insert(item);
                    }
                } catch (FileNotFoundException e) {
                    Msg.ServidorErro("Erro ao adicionar o lang da pasta langs!!!", "TraducaoConfig", getClass(), e);
                } catch (IOException e) {
                    Msg.ServidorErro("Erro ao adicionar o lang da pasta langs!!!", "TraducaoConfig", getClass(), e);
                }
                file.delete();
            }
        }

    }
}
