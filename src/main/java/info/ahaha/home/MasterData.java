package info.ahaha.home;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MasterData implements Serializable {

    private final PlayerData playerData;
    private final List<HomeMasterData> homeMasterData = new ArrayList<>();

    public MasterData(UUID uuid) {
        this.playerData = new PlayerData(uuid);
    }

    public MasterData(PlayerData data) {
        this.playerData = data;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public List<HomeMasterData> getHomeMasterData() {
        return homeMasterData;
    }

    public boolean addMasterHome(String name, Location loc) {
        if (getHomeMasterData().size() >= playerData.getMaxSubmitNum()) return false;
        homeMasterData.add(new HomeMasterData(name, loc));
        return true;
    }

    public boolean removeMasterHome(String homeName) {
        return homeMasterData.removeIf(h -> h.getName().equalsIgnoreCase(homeName));
    }

}
