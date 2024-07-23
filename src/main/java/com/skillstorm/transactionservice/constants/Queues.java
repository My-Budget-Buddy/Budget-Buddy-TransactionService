package com.skillstorm.transactionservice.constants;

public enum Queues {
    BUDGET_REQUEST("budget-request"),
    BUDGET_RESPONSE("budget-response");

    private final String queue;

    Queues(String queue) {
        this.queue = queue;
    }

    @Override
    public String toString() {
        return queue;
    }
}
