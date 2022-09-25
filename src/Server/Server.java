package Server;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server implements Writable {
    private final String name;
    private final List<User> users;
    private final List<Player> players;
    private final List<GameState> states;

    public Server(String name) {
        this.name = name;
        users = new ArrayList<>();
        players = new ArrayList<>();
        states = new ArrayList<>();
    }

    // EFFECTS: returns an unmodifiable list of thingies in this workroom
    public List<User> getThingies() {
        return Collections.unmodifiableList(users);
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String name) throws Exception {
        User user = null;
        for (User p : users) {
            if (p.getName().equals(name)) {
                user = p;
            }
        }
        if (user == null) {
            throw new Exception();
        } else {
            return user;
        }
    }

    public Player getPlayer(String name) throws Exception {
        Player player = null;
        for (Player p : players) {
            if (p.getName().equals(name)) {
                player = p;
            }
        }
        if (player == null) {
            throw new Exception();
        } else {
            return player;
        }
    }

    public GameState getGameState(String name) throws Exception {
        GameState gameState = null;
        for (GameState p : states) {
            if (p.getName().equals(name)) {
                gameState = p;
            }
        }
        if (gameState == null) {
            throw new Exception();
        } else {
            return gameState;
        }
    }

    public void addUser(User user) throws Exception{
        String name = user.getName();
        boolean inUser = false;
        for (User p : users) {
            if (p.getName().equals(name)) {
                inUser = true;
                break;
            }
        }
        if (!inUser) {
            users.add(user);
        } else {
            throw new Exception();
        }
    }

    public void addPlayer(Player player) throws Exception{
        String name = player.getName();
        boolean inPlayer = false;
        for (Player p : players) {
            if (p.getName().equals(name)) {
                inPlayer = true;
                break;
            }
        }
        if (!inPlayer) {
            players.add(player);
        } else {
            throw new Exception();
        }
    }

    public void addState(GameState state) throws Exception{
        String name = state.getName();
        boolean inGameState = false;
        for (GameState p : states) {
            if (p.getName().equals(name)) {
                inGameState = true;
                break;
            }
        }
        if (!inGameState) {
            states.add(state);
        } else {
            throw new Exception();
        }
    }

    public void removePlayer(Player player) throws Exception{
        String name = player.getName();
        boolean inPlayer = false;
        for (Player p : players) {
            if (p.getName().equals(name)) {
                inPlayer = true;
                break;
            }
        }
        if (inPlayer) {
            players.remove(player);
        } else {
            throw new Exception();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("User", usersToJson());
        json.put("Player", playersToJson());
        json.put("GameState", gameStateToJson());
        return json;
    }

    private JSONArray usersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (User p : users) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Player p : players) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

    private JSONArray gameStateToJson() {
        JSONArray jsonArray = new JSONArray();

        for (GameState p : states) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
}
