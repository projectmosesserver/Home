package info.ahaha.home;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData implements Serializable {

    private final UUID uuid;
    private int maxSubmitNum;
    private boolean isCostMaterial;
    private final List<HomeData> data = new ArrayList<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.maxSubmitNum = Home.plugin.getManager().getConfig().getInt("DefaultSubmit");
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isCostMaterial(){
        return isCostMaterial;
    }

    public void setCostMaterial() {
        isCostMaterial = !isCostMaterial;
    }

    public int getMaxSubmitNum() {
        return maxSubmitNum;
    }

    public boolean addSubmitNum() {
        int maxSubmit = Home.plugin.getManager().getConfig().getInt("MaxSubmit");
        if (getMaxSubmitNum() >= maxSubmit) return false;
        else {
            maxSubmitNum++;
            return true;
        }
    }

    public boolean addHome(String name, Location location) {
        if (getData().size() >= maxSubmitNum) return false;
        else {
            data.add(new HomeData(name, location));
            return true;
        }
    }

    public List<HomeData> getData() {
        return data;
    }

    public boolean removeHome(String name){
        for (HomeData data : data){
            if (data.getName().equalsIgnoreCase(name)){
                this.data.remove(data);
                return true;
            }
        }
        return false;
    }
}
