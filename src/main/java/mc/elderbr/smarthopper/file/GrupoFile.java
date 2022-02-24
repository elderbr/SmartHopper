package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrupoFile {

    private final File FILE = new File(VGlobal.ARQUIVO, "grupo.yml");
    private final YamlConfiguration config;

    private Grupo grupo;
    private Item item;
    private List<Item> listItem = new ArrayList<>();

    public GrupoFile() {

        if(!FILE.exists()){
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(FILE);
        ler();

    }

    public void ler(){
        for( String values : config.getValues(false).keySet()){
            grupo = new Grupo();
            grupo.setDsGrupo( config.getString(values.concat(".grupo_name")));
            grupo.setCdGrupo(config.getInt(values.concat(".grupo_id")));
            listItem = new ArrayList<>();
            for(Object items : config.getList(values.concat(".grupo_item"))){
                item = new Item();
                item.setDsItem((String) items);
                listItem.add(item);
            }
            grupo.setListItem(listItem);
            Msg.Grupo(grupo, getClass());
        }
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

    public void escrever() {
        Grupo grupo = new Grupo();
        try (BufferedWriter w = Files.newBufferedWriter(FILE.toPath(), StandardCharsets.UTF_8)) {
            w.write("Grupos:");
            for (Grupo gp : VGlobal.LIST_GRUPO) {
                w.newLine();
                w.write("  - " + gp.getDsGrupo());
            }
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
