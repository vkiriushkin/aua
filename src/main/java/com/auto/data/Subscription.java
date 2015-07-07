package com.auto.data;

public class Subscription {

    private final long id;
    private final long userId;
    private Criterion criterion;
    private PeriodicType periodicType;

    public Subscription(long id, long userId, Criterion criterion, PeriodicType periodicType) {
        this.id = id;
        this.userId = userId;
        this.criterion = criterion;
        this.periodicType = periodicType;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public PeriodicType getPeriodicType() {
        return periodicType;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public void setPeriodicType(PeriodicType periodicType) {
        this.periodicType = periodicType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
