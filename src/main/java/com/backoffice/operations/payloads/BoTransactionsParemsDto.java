package com.backoffice.operations.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoTransactionsParemsDto {
    long pageNo;
    long pageSize;
	String fromDate;
	String toDate ;
	String txnCategory;
}
