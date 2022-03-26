package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.dao.GrupoDao;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrupoFile {

    private final File FILE = new File(VGlobal.ARQUIVO, "grupo.yml");
    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);

    private Grupo grupo;
    private GrupoDao grupoDao = new GrupoDao();
    private Item item;
    private List<Item> listItem = new ArrayList<>();

    public GrupoFile() {

        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // SE A VERSÃO DO PLUGIN FOR MENOR QUE A VERSÃO ATUAL
        if (VGlobal.VERSAO > Config.Version()) {
            ler();
            createGrupo();
        }
        if (VGlobal.LIST_GRUPO.size() > grupoDao.selectAll().size()) {
            createGrupo();
        }
    }

    // LER O ARQUIVO grupo.yml E SALVA NO BANCO DE DADOS COM OS SEUS ITENS
    public void ler() {
        for (String values : config.getValues(false).keySet()) {
            grupo = new Grupo();
            grupo.setCdGrupo(config.getInt(values.concat(".grupo_id")));
            grupo.setDsGrupo(config.getString(values.concat(".grupo_name")));
            // SE O GRUPO FOR SALVO
            if (grupoDao.insertID(grupo) > 0) {
                // ADICIONANDO LISTA DE ITEM
                listItem = new ArrayList<>();
                for (Object items : config.getList(values.concat(".grupo_item"))) {
                    item = VGlobal.ITEM_NAME_MAP.get((String) items);
                    Msg.Item(item, getClass());
                    if (item != null) {
                        grupoDao.insertItem(grupo, item);
                        listItem.add(item);
                    }
                }
            }
        }
        grupoDao.createGrupo();
    }

    public void createGrupo() {
        try {

            List<String> comentario = new ArrayList<>();
            comentario.add("Grupo 01");

            for (Grupo grupo : VGlobal.LIST_GRUPO) {
                Msg.Grupo(grupo, getClass());
                config.setComments(grupo.getDsGrupo(), comentario);
                config.set(grupo.getDsGrupo().concat(".grupo_id"), grupo.getCdGrupo());
                config.set(grupo.getDsGrupo().concat(".grupo_name"), grupo.getDsGrupo());
                config.set(grupo.getDsGrupo().concat(".lang"), "pt_br");
                config.save(FILE);
            }
        } catch (IOException e) {
            Msg.ServidorErro(e, "", getClass());
        }
    }
}
