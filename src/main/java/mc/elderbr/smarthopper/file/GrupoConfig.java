package mc.elderbr.smarthopper.file;


import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Pocao;
import mc.elderbr.smarthopper.utils.Debug;
import mc.elderbr.smarthopper.utils.Msg;
import mc.elderbr.smarthopper.utils.Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static mc.elderbr.smarthopper.model.Grupo.DELETE;

public class GrupoConfig {

    private File fileConfig = new File(VGlobal.ARQUIVO, "grupo.yml");
    private YamlConfiguration config;

    private InputStream inputStream;
    private BufferedReader reader;
    private String txtReader;
    private String key;
    private String value;
    private Map<String, String> langMap = new HashMap<>();

    private Grupo grupo;
    private int idGrupo = 1;

    private Item item;
    private List<String> itemStringList;
    private String nameMaterial;

    private Pocao potion;
    private ItemStack itemPotion;
    private PotionMeta potionMeta;
    private List<String> listPotion;

    public GrupoConfig() {

        // POÇÕES
        potion = new Pocao();
        listPotion = potion.getListPotion();


        Debug.Write("Verificando se existe o arquivo grupo.yml");
        if (!fileConfig.exists()) {
            try {
                Debug.Write("Criando o arquivo grupo.yml");
                fileConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            createDefault();// CRIANDO O GRUPO DOS ITENS
        }
        Debug.Write("Carregando o arquivo grupo.yml");
        config = YamlConfiguration.loadConfiguration(fileConfig);

        carregar();
    }

    public GrupoConfig(Grupo grupo, int event) {

        config = YamlConfiguration.loadConfiguration(fileConfig);

        switch (event) {
            case Grupo.NEW:
                // ADICIONANDO O NOME DO GRUPO
                config.set(grupo.getName().concat(".grupo_id"), grupo.getID());
                config.set(grupo.getName().concat(".grupo_name"), grupo.getName());
                // ADICIONANDO A LINGUAGEM
                config.set(grupo.getName().concat(".lang"), grupo.getLang());
                break;
            case DELETE:
                // ADICIONANDO O NOME DO GRUPO
                config.set(grupo.getName(), null);

                // REMOVE A VARIAL GLOBAL
                VGlobal.GRUPO_MAP_ID.remove(grupo.getID());// ADICIONANDO A BUSCA PELO ID
                VGlobal.GRUPO_MAP_NAME.remove(grupo.getName());// ADICIONANDO A BUSCA PELO NOME
                VGlobal.GRUPO_NAME_LIST.remove(grupo.getName());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
                VGlobal.GRUPO_LANG_MAP.remove(grupo.getName());// ADICIONANDO A BUSCA PELO LANG
                VGlobal.GRUPO_MAP.remove(grupo.getName());// ADICIONANDO NA LISTA DE LANG TRADUZIDO
                VGlobal.GRUPO_ITEM_MAP_LIST.remove(grupo.getName());// LISTA DE ITEM DO GRUPO EM TEXTO
                break;

        }
        // ADICIONANDO ITENS DO GRUPO
        if (event != DELETE) {
            itemStringList = new ArrayList<>();
            for (Item items : grupo.getItemList()) {
                itemStringList.add(items.getName());
            }
            config.set(grupo.getName().concat(".grupo_item"), itemStringList);
        }
        save();
    }


    private void carregar() {

        Debug.WriteMsg("Carregando grupos...");

        // REMOVENDO ITEM COM A NOMENCLATURA DIFERENTE
        List<String> list = (List<String>) config.getList("grass.grupo_item");
        list.remove("grass path");
        config.set("grass.grupo_item", list);
        save();



        for (String grups : config.getValues(false).keySet()) {

            grupo = new Grupo();
            grupo.setID(config.getInt(grups.concat(".grupo_id")));
            grupo.setName(config.getString(grups.concat(".grupo_name")));
            grupo.setLang(((MemorySection) config.get(grups.concat(".lang"))));

            // LISTA DE LANGS
            for (Map.Entry<String, String> langs : grupo.getLang().entrySet()) {
                VGlobal.GRUPO_NAME_LIST.add(langs.getKey().toLowerCase());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
                VGlobal.GRUPO_MAP.put(langs.getValue().toString(), grupo.getName());// ADICIONANDO NA LISTA DE LANG TRADUZIDO
                VGlobal.GRUPO_LANG_MAP.put(grupo.getName(), new HashMap<String, String>() {{
                    put(langs.getKey(), langs.getValue().toString());
                }});
            }


            // LISTA DE ITENS
            List<String> grupoStringItem = new ArrayList<>();
            itemStringList = config.getStringList(grups.concat(".grupo_item"));

            if (!itemStringList.isEmpty()) {

                for (String items : itemStringList) {
                    // CRIANDO NO ITEM

                    // Se for Poção
                    if (potion.getListPotion().contains(items)) {
                        item = new Item(potion.parseItemStack(items));
                    } else {
                        item = new Item(items);
                    }


                    grupo.addItemList(item);// ADICIONA O ITEM NA LISTA DO GRUPO
                    grupoStringItem.add(item.getName());

                    // CRIANDO LISTA DE ITEM QUE RETORNA O NOME DO GRUPO
                    if (VGlobal.GRUPO_LIST_ITEM.get(item.getName()) == null) {
                        VGlobal.GRUPO_LIST_ITEM.put(item.getName(), new ArrayList(Arrays.asList(grupo.getName())));
                    } else {
                        VGlobal.GRUPO_LIST_ITEM.get(item.getName()).add(grupo.getName());
                    }
                }
            }

            // ADICIONANDO A VARIAL GLOBAL
            VGlobal.GRUPO_MAP_ID.put(grupo.getID(), grupo);// ADICIONANDO A BUSCA PELO ID
            VGlobal.GRUPO_MAP_NAME.put(grupo.getName(), grupo);// ADICIONANDO A BUSCA PELO NOME
            VGlobal.GRUPO_NAME_LIST.add(grupo.getName());// ADICIONANDO NA LISTA DE NOMES DO GRUPO
            VGlobal.GRUPO_LANG_MAP.put(grupo.getName(), grupo.getLang());// ADICIONANDO A BUSCA PELO LANG
            VGlobal.GRUPO_MAP.put(grupo.getName(), grupo.getName());// ADICIONANDO NA LISTA DE LANG TRADUZIDO
            VGlobal.GRUPO_ITEM_MAP_LIST.put(grupo.getName(), grupoStringItem);// LISTA DE ITEM DO GRUPO EM TEXTO

            Debug.Write("Carregando o grupo >> " + grupo.getName());


        }
        Debug.WriteMsg("Grupos carregado com sucesso!");
    }

    private void createDefault() {

        Debug.WriteMsg("Criando grupos...");

        inputStream = getClass().getResourceAsStream("/grupo.yml");
        config = YamlConfiguration.loadConfiguration(fileConfig);
        try {

            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((txtReader = reader.readLine()) != null) {

                key = txtReader.substring(0, txtReader.indexOf(":"));
                value = txtReader.substring(txtReader.indexOf(":") + 2, txtReader.length());

                grupo = new Grupo();
                grupo.setID(idGrupo);
                grupo.setName(key);

                /* CRIANDO O YML DO GRUPO */

                // ADICIONANDO O NOME DO GRUPO
                config.set(key.concat(".grupo_id"), idGrupo);
                config.set(key.concat(".grupo_name"), key);
                // ADICIONANDO LANG NA LISTA
                langMap = new HashMap<>();
                langMap.put("pt_br", StringEscapeUtils.unescapeJava(value));
                config.set(key.concat(".lang"), langMap);

                // ADICIONANDO OS ITEM AO GRUPO
                itemStringList = new ArrayList<>();
                for (Material materials : Material.values()) {

                    nameMaterial = Utils.ToMaterial(materials);// CONVERTE MATERIAL PARA STRING

                    // ITEM QUE NÃO FAZEM PARTE DA LISTA DO GRUPO

                    // ADICIONA NA LISTA DE ITEM DO GRUPO
                    if (grupo.isItemGrupo(nameMaterial) && grupo.isContentItem(nameMaterial) && !grupo.getName().contains("potion")) {
                        itemStringList.add(nameMaterial);
                    }

                    // Adiciona o carvão vegetal ao grupo de carvão
                    if (grupo.getName().equals("coal") && nameMaterial.equals("charcoal")) {
                        itemStringList.add(nameMaterial);
                    }

                    // Adiciona a batata no grupo de assados
                    if (grupo.getName().equals("cooked") && nameMaterial.equals("baked potato")) {
                        itemStringList.add(nameMaterial);
                    }

                    // Adiciona todos os tipos de fornalhas no grupo fornalhas
                    if (grupo.getName().equals("furnace") && nameMaterial.equals("smoker")) {
                        itemStringList.add(nameMaterial);
                    }

                    // Adiconando cortado de pedras no grupo mes de trabalho
                    if (grupo.getName().equals("table") && nameMaterial.equals("stonecutter")) {
                        itemStringList.add(nameMaterial);
                    }

                    // Grupo de redstones
                    if (grupo.getName().equals("redstones")) {
                        if (nameMaterial.contains("redstone")
                                || nameMaterial.equals("dispenser")
                                || nameMaterial.equals("note block")
                                || nameMaterial.contains("piston")
                                || nameMaterial.equals("lever")
                                || nameMaterial.contains("pressure plate")
                                || nameMaterial.contains("button")
                                || nameMaterial.contains("trapdoor")
                                || nameMaterial.contains("tripwire")
                                || nameMaterial.contains("chest")
                                && !nameMaterial.contains("chestplate")
                                || nameMaterial.contains("daylight")
                                || nameMaterial.contains("hopper")
                                || nameMaterial.contains("drooper")
                                || nameMaterial.contains("observer")
                                || nameMaterial.contains("iron door")
                                || nameMaterial.contains("comparator")
                                || nameMaterial.contains("repeater")) {
                            itemStringList.add(nameMaterial);
                        }
                    }

                    // Grupo de flores
                    if (grupo.getName().equals("flowers")) {
                        if (nameMaterial.equals("grass")
                                || nameMaterial.equals("fern")
                                || nameMaterial.equals("dead bush")
                                || nameMaterial.contains("seagrass")
                                || nameMaterial.equals("sea pickle")
                                || nameMaterial.equals("dandelion")
                                || nameMaterial.equals("poppy")
                                || nameMaterial.equals("blue orchid")
                                || nameMaterial.equals("allium")
                                || nameMaterial.equals("azure bluet")
                                || nameMaterial.contains("tulip")
                                || nameMaterial.equals("oxeye daisy")
                                || nameMaterial.equals("cornflower")
                                || nameMaterial.contains("lily")
                                || nameMaterial.contains("rose")
                                || nameMaterial.equals("vine")
                                || nameMaterial.equals("sunflower")
                                || nameMaterial.equals("lilac")
                                || nameMaterial.equals("peony")
                                || nameMaterial.equals("tall grass")
                                || nameMaterial.equals("large fern")
                        ) {
                            itemStringList.add(nameMaterial);
                        }
                    }
                    // Poções
                    if (grupo.getName().equals("potion")) {
                        for (String potions : listPotion) {
                            if (!potions.contains("lingering") && !potions.contains("splash") && !itemStringList.contains(potions)) {
                                itemStringList.add(potions);
                            }
                        }
                    }
                    if (grupo.getName().contains("splash")) {
                        for (String potions : listPotion) {
                            if (potions.contains("splash") && !itemStringList.contains(potions)) {
                                itemStringList.add(potions);
                            }
                        }
                    }
                    if (grupo.getName().contains("lingering")) {
                        for (String potions : listPotion) {
                            if (potions.contains("lingering") && !itemStringList.contains(potions)) {
                                itemStringList.add(potions);
                            }
                        }
                    }

                }// FIM DO MATERIALS

                // Ferramentas de Pedras
                if (grupo.getName().equals("stone tools")) {
                    itemStringList.add("stone sword");
                    itemStringList.add("stone shovel");
                    itemStringList.add("stone pickaxe");
                    itemStringList.add("stone axe");
                    itemStringList.add("stone hoe");
                }

                // Ferramentas de ferro
                if (grupo.getName().equals("iron tools")) {
                    itemStringList.add("iron sword");
                    itemStringList.add("iron shovel");
                    itemStringList.add("iron pickaxe");
                    itemStringList.add("iron axe");
                    itemStringList.add("iron hoe");
                }

                // Ferramentas de ouro
                if (grupo.getName().equals("golden tools")) {
                    itemStringList.add("golden sword");
                    itemStringList.add("golden shovel");
                    itemStringList.add("golden pickaxe");
                    itemStringList.add("golden axe");
                    itemStringList.add("golden hoe");
                }

                // Ferramentas de diamante
                if (grupo.getName().equals("diamond tools")) {
                    itemStringList.add("diamond sword");
                    itemStringList.add("diamond shovel");
                    itemStringList.add("diamond pickaxe");
                    itemStringList.add("diamond axe");
                    itemStringList.add("diamond hoe");
                }

                // Ferramentas de netherite
                if (grupo.getName().equals("netherite tools")) {
                    itemStringList.add("netherite sword");
                    itemStringList.add("netherite shovel");
                    itemStringList.add("netherite pickaxe");
                    itemStringList.add("netherite axe");
                    itemStringList.add("netherite hoe");
                }

                // ITENS PARA SEREM ASSADOS
                if (grupo.getName().equals("carne crua")) {
                    itemStringList.add("potato");
                    itemStringList.add("beef");
                    itemStringList.add("porkchop");
                    itemStringList.add("mutton");
                    itemStringList.add("chicken");
                    itemStringList.add("rabbit");
                    itemStringList.add("cod");
                    itemStringList.add("salmon");
                    itemStringList.add("kelp");
                }

                //Poções
                if(potion.getListPotion().contains(grupo.getName())){
                    for(String potions : potion.getListPotion()) {
                        Msg.ServidorGreen("potion >> "+ potions, getClass());
                        if(potions.contains(grupo.getName())) {
                            itemStringList.add(potions);
                        }
                    }
                }

                config.set(key.concat(".grupo_item"), itemStringList);
                save();

                idGrupo++;
            }// FIM WHILE

        } catch (IOException e) {
            Msg.ServidorErro(e, "createDefault()", getClass());
            Debug.Write("Erro ao criar o arquivo de grupo.yml:\nErro: " + e.getMessage());
        }
        Debug.WriteMsg("Grupos criados com sucesso!");
    }

    private void update() {

        Msg.ServidorGold("Verificando se precisa atualizar os grupos...");

        for (Map.Entry<String, Object> grups : config.getValues(true).entrySet()) {
            key = grups.getKey().toString();
            value = grups.getValue().toString();

            if (!key.contains("id") && !key.contains("name") && !key.contains("item") && !value.contains("Memory")) {
                String grupName = key.substring(0, key.indexOf("."));
                Msg.ServidorGold("Atualizando o grupo: " + grupName);
                String lang = key.substring(key.indexOf(".") + 1, key.length());
                config.set(key, null);
                config.set(grupName.concat(".lang." + lang), value);
                save();
            }

        }
        Msg.ServidorGold("Verificando se precisa atualizar os grupos finalizada!!!");
    }

    private void save() {
        try {
            config.save(fileConfig);
        } catch (IOException e) {
            Msg.ServidorErro(e, "save()", getClass());
        }
    }

    private void createNameGrupo() {
        List<String> grupoList = new ArrayList<>();
        String name = null;
        for (Material material : Material.values()) {
            name = Utils.ToMaterial(material);
            if (material.isItem() && !material.isAir()) {
                if (name.contains("\\s")) {
                    for (String names : name.split("\\s")) {
                        if (!grupoList.contains(names)) {
                            grupoList.add(names);
                        }
                    }
                } else {
                    if (!grupoList.contains(name)) {
                        grupoList.add(name);
                    }
                }
            }
        }
    }
}
