package nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.fit.messages;

import androidx.annotation.Nullable;

import nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.GarminTimeUtils;
import nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.fit.RecordData;
import nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.fit.RecordDefinition;
import nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.fit.RecordHeader;
import nodomain.freeyourgadget.gadgetbridge.util.Optional;

//
// WARNING: This class was auto-generated, please avoid modifying it directly.
// See nodomain.freeyourgadget.gadgetbridge.service.devices.garmin.fit.codegen.FitCodeGen
//
public class FitMonitoring extends RecordData {
    public FitMonitoring(final RecordDefinition recordDefinition, final RecordHeader recordHeader) {
        super(recordDefinition, recordHeader);

        final int globalNumber = recordDefinition.getGlobalFITMessage().getNumber();
        if (globalNumber != 55) {
            throw new IllegalArgumentException("FitMonitoring expects global messages of " + 55 + ", got " + globalNumber);
        }
    }

    @Nullable
    public Long getDistance() {
        return (Long) getFieldByNumber(2);
    }

    @Nullable
    public Long getCycles() {
        return (Long) getFieldByNumber(3);
    }

    @Nullable
    public Long getActiveTime() {
        return (Long) getFieldByNumber(4);
    }

    @Nullable
    public Integer getActivityType() {
        return (Integer) getFieldByNumber(5);
    }

    @Nullable
    public Integer getActiveCalories() {
        return (Integer) getFieldByNumber(19);
    }

    @Nullable
    public Integer getDurationMin() {
        return (Integer) getFieldByNumber(29);
    }

    @Nullable
    public Integer getCurrentActivityTypeIntensity() {
        return (Integer) getFieldByNumber(24);
    }

    @Nullable
    public Integer getTimestamp16() {
        return (Integer) getFieldByNumber(26);
    }

    @Nullable
    public Integer getHeartRate() {
        return (Integer) getFieldByNumber(27);
    }

    @Nullable
    public Long getTimestamp() {
        return (Long) getFieldByNumber(253);
    }

    // manual changes below

    @Override
    public Long getComputedTimestamp() {
        final Integer timestamp16 = getTimestamp16();
        final Long computedTimestamp = super.getComputedTimestamp();
        if (timestamp16 != null && computedTimestamp != null) {
            return (long) GarminTimeUtils.garminTimestampToUnixTime(
                    (GarminTimeUtils.unixTimeToGarminTimestamp(computedTimestamp.intValue()) & ~0xFFFF) | (timestamp16 & 0xFFFF)
            );
        }
        return computedTimestamp;
    }

    public Optional<Integer> getComputedActivityType() {
        final Integer activityType = getActivityType();
        if (activityType != null) {
            return Optional.of(activityType);
        }

        final Integer currentActivityTypeIntensity = getCurrentActivityTypeIntensity();
        if (currentActivityTypeIntensity != null) {
            return Optional.of(currentActivityTypeIntensity & 0x1F);
        }

        return Optional.empty();
    }

    public Integer getComputedIntensity() {
        final Integer currentActivityTypeIntensity = getCurrentActivityTypeIntensity();
        if (currentActivityTypeIntensity != null) {
            return (currentActivityTypeIntensity >> 5) & 0x7;
        }

        return null;
    }
}