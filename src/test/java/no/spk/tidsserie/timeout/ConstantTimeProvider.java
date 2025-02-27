package no.spk.tidsserie.timeout;

import java.time.LocalTime;

public class ConstantTimeProvider implements TimeProvider {
    private LocalTime time;
    private long millies;

    public ConstantTimeProvider(final LocalTime time, final long millies) {
        this.time = time;
        this.millies = millies;
    }

    @Override
    public LocalTime currentTime() {
        return time;
    }

    @Override
    public long currentMillies() {
        return millies;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public void setMillies(final long millies) {
        this.millies = millies;
    }
}
