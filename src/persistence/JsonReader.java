package persistence;

import Server.Server;
import Server.User;
import Server.Player;
import Server.GameState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads Bank from JSON data stored in file
// code gotten from JsonSerializationDemo

public class JsonReader {
    private final String source;

    // Effects: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Bank from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Server read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseServer(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Bank from JSON object and returns it
    private Server parseServer(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Server g = new Server(name);
        addsUsers(g, jsonObject);
        addsPlayers(g, jsonObject);
        addsGameStates(g, jsonObject);
        return g;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addsUsers(Server g, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("User");
        for (Object json : jsonArray) {
            JSONObject nextUser = (JSONObject) json;
            addUser(g, nextUser);
        }
    }

    private void addsPlayers(Server g, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Player");
        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            addPlayer(g, nextPlayer);
        }
    }

    private void addsGameStates(Server g, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("GameState");
        for (Object json : jsonArray) {
            JSONObject nextGameState = (JSONObject) json;
            addGameState(g, nextGameState);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addUser(Server g, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int xp = jsonObject.getInt("xp");
        int xpNeeded = jsonObject.getInt("xpNeeded");
        int level = jsonObject.getInt("level");
//        String guess = jsonObject.getString("guess");
        String image = jsonObject.getString("image");
        String desc = jsonObject.getString("desc");
        User user = new User(name, level, xp, xpNeeded, image, desc);
//        user.setGuess(guess);
        try {
            g.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPlayer(Server g, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String guess = jsonObject.getString("guess");
        Player player = new Player(name, guess);
        player.setGuess(guess);
        try {
            g.addPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addGameState(Server g, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        boolean state = jsonObject.getBoolean("state");
        GameState gameState = new GameState(name, state);
        gameState.setState(state);
        try {
            g.addState(gameState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
