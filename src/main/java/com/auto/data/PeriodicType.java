package com.auto.data;

import java.util.concurrent.TimeUnit;

public enum PeriodicType {
    HOURLY(1, TimeUnit.HOURS),
    DAILY(1, TimeUnit.DAYS),
    WEEKLY(7, TimeUnit.DAYS),
    ;

    private long period;
    private TimeUnit timeUnit;

    PeriodicType(long period, TimeUnit timeUnit) {
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public long getPeriod() {
        return period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
