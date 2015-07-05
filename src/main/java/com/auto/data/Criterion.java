package com.auto.data;

public class Criterion {

    private int makrId;
    private int modelId;
    private int fromYear;
    private int toYear;

    public int getMakrId() {
        return makrId;
    }

    public int getModelId() {
        return modelId;
    }

    public int getFromYear() {
        return fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    private Criterion(Builder builder) {
        makrId = builder.makrId;
        modelId = builder.modelId;
        fromYear = builder.fromYear;
        toYear = builder.toYear;
    }


    public static final class Builder {
        private int makrId;
        private int modelId;
        private int fromYear;
        private int toYear;

        public Builder() {
        }

        public Builder makrId(int makrId) {
            this.makrId = makrId;
            return this;
        }

        public Builder modelId(int modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder fromYear(int fromYear) {
            this.fromYear = fromYear;
            return this;
        }

        public Builder toYear(int toYear) {
            this.toYear = toYear;
            return this;
        }

        public Criterion build() {
            return new Criterion(this);
        }
    }
}
