package info.ahaha.home;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MasterData extends PlayerData {

    private final List<HomeMasterData> homeMasterData = new ArrayList<>();

    public MasterData(UUID uuid) {
        super(uuid);
    }

    public MasterData(PlayerData data) {
        super(data.getUuid());
        for (int i = this.getMaxSubmitNum(); i <= data.getMaxSubmitNum(); i++) {
            addSubmitNum();
        }
    }

    public List<HomeMasterData> getHomeMasterData() {
        return homeMasterData;
    }

    public boolean addMasterHome(String name, Location loc) {
        if (getHomeMasterData().size() >= this.getMaxSubmitNum()) return false;
        homeMasterData.add(new HomeMasterData(name, loc));
        return true;
    }

    public boolean removeMasterHome(String homeName) {
        return homeMasterData.removeIf(h -> h.getName().equalsIgnoreCase(homeName));
    }

}
