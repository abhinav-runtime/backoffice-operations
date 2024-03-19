package com.backoffice.operations.entity;

import jakarta.persistence.Column;

public class DailyTrxnLimitAmt {

    String segment;
    Double minPerTrxnAmt;
    Double maxPerTrxnAmt;
    Double dailyAmt;
    Double monthlyAmt;
    Double dailyCount;
    Double monthlyCount;
}
