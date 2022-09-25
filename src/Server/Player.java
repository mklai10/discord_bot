package Server;

import org.json.JSONObject;
import persistence.Writable;

public class Player implements Writable {
    private final String name;
    private String guess;

    public Player(String name, String guess) {
        this.name = name;
        this.guess = guess;
    }

    public String getName() {
        return name;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("guess", guess);
        return json;
    }
}
