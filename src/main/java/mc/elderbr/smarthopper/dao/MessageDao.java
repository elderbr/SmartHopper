package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Message;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

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


        List<String> sb = new ArrayList<>();
        // Obtém o InputStream para o arquivo de recursos
        InputStream inputStream = getClass().getResourceAsStream("/message.yml");

        // Verifica se o recurso existe
        if (inputStream == null) {
            System.out.println("O arquivo message.yml não foi encontrado no diretório de recursos.");
            return;
        }

        // Usa o InputStream com um InputStreamReader e um BufferedReader para ler o arquivo
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.add(linha);
            }
        } catch (IOException e) {
            Msg.ServidorGold("Ocorreu um erro ao tentar ler o arquivo");
        }

        Collections.sort(sb);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_MESSAGE)))) {
            for (String msg : sb) {
                writer.write(msg);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            Msg.ServidorGold("Erro ao tentar criar o arquivo message.yml");
        }
    }
}