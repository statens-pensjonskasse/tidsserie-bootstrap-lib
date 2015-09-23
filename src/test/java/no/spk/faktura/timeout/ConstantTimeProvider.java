package no.spk.faktura.timeout;

import java.time.LocalTime;

public class ConstantTimeProvider implements TimeProvider {
    LocalTime time;
    long millies;

    public ConstantTimeProvider(LocalTime time, long millies) {
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

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setMillies(long millies) {
        this.millies = millies;
    }
}
