package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.enums.MessageType;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Message;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MessageDao implements VGlobal {

    private YamlConfiguration config;
    private Set<Message> codes = new TreeSet<>();

    public MessageDao() {

        try {
            if (!FILE_MESSAGE.exists()) {
                FILE_MESSAGE.createNewFile();
                create();
            }
        } catch (IOException e) {
            Msg.ServidorGold("Erro ao criar o arquivo message.yml:\nErro: " + e.getMessage());
        }
        config = YamlConfiguration.loadConfiguration(FILE_MESSAGE);
    }

    public String findByMessage(String code) {
        config = YamlConfiguration.loadConfiguration(FILE_MESSAGE);
        return config.getString(code);
    }

    public Map<String, Message> findAll() {
        // Lista auxiliar
        codes = new HashSet<>();
        config = YamlConfiguration.loadConfiguration(FILE_MESSAGE);
        for (Map.Entry<String, Object> obj : config.getValues(false).entrySet()) {
            // Pegando os nomes das mensagens
            codes.add(new Message(obj.getKey(), obj.getValue().toString(), obj.getKey().toString()));
            // Adicionando Message na variavel global
            MESSAGE_MAP.put(obj.getKey(), new Message(obj.getKey(), obj.getValue().toString(), null));
        }
        return MESSAGE_MAP;
    }

    public void insert(Message message) {
        try {
            // Adicionando o nome da mensagem na lista auxiliar
            codes = new HashSet<>();
            codes.add(message);
            MESSAGE_MAP.put(message.getCode(), message);

            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() throws IOException {
        if (codes == null || codes.size() < 1) {
            return;
        }
        if(config == null){
            config = YamlConfiguration.loadConfiguration(FILE_MESSAGE);
        }
        for (Message message : codes) {
            config.set(message.getCode(), message.getMsg());
            if (message.getComments() != null) {
                config.setComments(message.getCode(), Arrays.asList(message.getComments()));
            }
            config.save(FILE_MESSAGE);
        }
    }

    public void delete(Message message) throws IOException {
        config.set(message.getCode(), null);
        config.save(FILE_MESSAGE);
    }

    public void update(Message message) throws IOException {
        config.set(message.getCode(), message.getMsg());
        if (message.getComments() != null) {
            config.setComments(message.getCode(), Arrays.asList(message.getComments()));
        }
        config.save(FILE_MESSAGE);
    }

    private void create() {
        List<String> list = new ArrayList<>();

        for(MessageType type : MessageType.values()){
            list.add(type.name());
        }

        Collections.sort(list);
        for(String value : list){
            MessageType type = MessageType.valueOf(value);
            Message message = new Message(type.getCode(), type.getMsg());
            insert(message);
        }
    }
}