package Server;

import org.json.JSONObject;
import persistence.Writable;

public class User implements Writable {
    private final String name;
    private int level;
    private int xp;
    private int xpNeeded;
    private String image;
    private String desc;

    public User(String name, int level, int xp, int xpNeeded, String image, String desc) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.xpNeeded = xpNeeded;
        this.image = image;
        this.desc = desc;
    }

    public void levelUp() {
        level++;
        xpNeeded = xpNeeded*level;

    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void xpUp() {
        xp++;
        if (xp == xpNeeded) {
            levelUp();
            xp = 0;
        }
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }


    public int getXp() {
        return xp;
    }

    public int getXpNeeded() {
        return xpNeeded;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("level", level);
        json.put("xp", xp);
        json.put("xpNeeded", xpNeeded);

        json.put("image", image);
        json.put("desc", desc);
        return json;
    }
}
