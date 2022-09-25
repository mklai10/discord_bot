package Server;

import org.json.JSONObject;
import persistence.Writable;

public class GameState implements Writable {
    private final String name;
    private boolean state;

    public GameState(String name, boolean state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("state", state);
        return json;
    }
}
