package com.iafenvoy.iceandfire.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RoostWorldData extends SavedData {
    private static final String IDENTIFIER =
            "iceandfire_roost_positions";

    private static final Factory<RoostWorldData> TYPE =
            new Factory<>(
                    RoostWorldData::new,
                    RoostWorldData::load,
                    DataFixTypes.CHUNK
            );

    private final List<BlockPos> roosts =
            new ArrayList<>();

    public static RoostWorldData get(Level level) {

        if (level instanceof ServerLevel serverLevel) {

            DimensionDataStorage storage =
                    serverLevel.getDataStorage();

            return storage.computeIfAbsent(
                    TYPE,
                    IDENTIFIER
            );
        }

        return null;
    }

    private static RoostWorldData load(
            CompoundTag tag,
            HolderLookup.Provider lookup
    ) {

        RoostWorldData data =
                new RoostWorldData();

        ListTag list =
                tag.getList("Roosts", 10);

        for (int i = 0; i < list.size(); i++) {

            CompoundTag entry =
                    list.getCompound(i);

            data.roosts.add(
                    new BlockPos(
                            entry.getInt("X"),
                            entry.getInt("Y"),
                            entry.getInt("Z")
                    )
            );
        }

        return data;
    }

    public void addRoost(BlockPos pos) {

        roosts.add(pos);
        setDirty();
    }

    public List<BlockPos> getRoosts() {

        return roosts;
    }

    @Override
    public @NotNull CompoundTag save(
            @NotNull CompoundTag tag,
            HolderLookup.@NotNull Provider lookup
    ) {

        ListTag list =
                new ListTag();

        for (BlockPos pos : roosts) {

            CompoundTag entry =
                    new CompoundTag();

            entry.putInt("X", pos.getX());
            entry.putInt("Y", pos.getY());
            entry.putInt("Z", pos.getZ());

            list.add(entry);
        }

        tag.put("Roosts", list);

        return tag;
    }
}
