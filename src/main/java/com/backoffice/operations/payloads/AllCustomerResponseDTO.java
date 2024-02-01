package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllCustomerResponseDTO {
	@JsonProperty("result")
    private Result result;

    @JsonProperty("exception")
    private Object exception;

    @JsonProperty("pagination")
    private Object pagination;

    public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
    public Object getException() {
		return exception;
	}

	public void setException(Object exception) {
		this.exception = exception;
	}

	public Object getPagination() {
		return pagination;
	}

	public void setPagination(Object pagination) {
		this.pagination = pagination;
	}


	public class Result {
		
		@JsonProperty("title")
        private String title;

        @JsonProperty("firstName")
        private String firstName;

        @JsonProperty("lastName")
        private String lastName;

        @JsonProperty("entityId")
        private String entityId;

        @JsonProperty("entityCategory")
        private String entityCategory;

        @JsonProperty("parentFkey")
        private String parentFkey;

        @JsonProperty("contactNo")
        private String contactNo;

        @JsonProperty("emailAddress")
        private String emailAddress;

        @JsonProperty("customerCredit")
        private CustomerCreditDTO customerCredit;

        @JsonProperty("kit")
        private int kit;

        @JsonProperty("pkey")
        private String pkey;

        @JsonProperty("status")
        private String status;

        @JsonProperty("customerSegment")
        private String customerSegment;

        @JsonProperty("joiningDate")
        private String joiningDate;

        @JsonProperty("closingDate")
        private String closingDate;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getEntityCategory() {
			return entityCategory;
		}

		public void setEntityCategory(String entityCategory) {
			this.entityCategory = entityCategory;
		}

		public String getParentFkey() {
			return parentFkey;
		}

		public void setParentFkey(String parentFkey) {
			this.parentFkey = parentFkey;
		}

		public String getContactNo() {
			return contactNo;
		}

		public void setContactNo(String contactNo) {
			this.contactNo = contactNo;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public CustomerCreditDTO getCustomerCredit() {
			return customerCredit;
		}

		public void setCustomerCredit(CustomerCreditDTO customerCredit) {
			this.customerCredit = customerCredit;
		}

		public int getKit() {
			return kit;
		}

		public void setKit(int kit) {
			this.kit = kit;
		}

		public String getPkey() {
			return pkey;
		}

		public void setPkey(String pkey) {
			this.pkey = pkey;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCustomerSegment() {
			return customerSegment;
		}

		public void setCustomerSegment(String customerSegment) {
			this.customerSegment = customerSegment;
		}

		public String getJoiningDate() {
			return joiningDate;
		}

		public void setJoiningDate(String joiningDate) {
			this.joiningDate = joiningDate;
		}

		public String getClosingDate() {
			return closingDate;
		}

		public void setClosingDate(String closingDate) {
			this.closingDate = closingDate;
		}

    }

    public class CustomerCreditDTO {
        @JsonProperty("creditLimit")
        private double creditLimit;

        @JsonProperty("cashLimit")
        private double cashLimit;
        
        @JsonProperty("childStatement")
        private Object childStatement;

        @JsonProperty("lastStatementGeneratedDate")
        private Object lastStatementGeneratedDate;

        @JsonProperty("nextStatementGeneratedDate")
        private String nextStatementGeneratedDate;

        @JsonProperty("creditStatus")
        private String creditStatus;

        @JsonProperty("currentStatementCustomerMapping")
        private Object currentStatementCustomerMapping;

        @JsonProperty("lastStatementCustomerMapping")
        private Object lastStatementCustomerMapping;

        @JsonProperty("statementDay")
        private String statementDay;

        @JsonProperty("fixedDueDay")
        private String fixedDueDay;

        @JsonProperty("dueDaysFromStatementDate")
        private int dueDaysFromStatementDate;

        @JsonProperty("expiryDate")
        private String expiryDate;

        @JsonProperty("closureReason")
        private Object closureReason;

        @JsonProperty("cardType")
        private CardType cardType;

		public double getCreditLimit() {
			return creditLimit;
		}

		public void setCreditLimit(double creditLimit) {
			this.creditLimit = creditLimit;
		}

		public double getCashLimit() {
			return cashLimit;
		}

		public void setCashLimit(double cashLimit) {
			this.cashLimit = cashLimit;
		}

		public Object getChildStatement() {
			return childStatement;
		}

		public void setChildStatement(Object childStatement) {
			this.childStatement = childStatement;
		}

		public Object getLastStatementGeneratedDate() {
			return lastStatementGeneratedDate;
		}

		public void setLastStatementGeneratedDate(Object lastStatementGeneratedDate) {
			this.lastStatementGeneratedDate = lastStatementGeneratedDate;
		}

		public String getNextStatementGeneratedDate() {
			return nextStatementGeneratedDate;
		}

		public void setNextStatementGeneratedDate(String nextStatementGeneratedDate) {
			this.nextStatementGeneratedDate = nextStatementGeneratedDate;
		}

		public String getCreditStatus() {
			return creditStatus;
		}

		public void setCreditStatus(String creditStatus) {
			this.creditStatus = creditStatus;
		}

		public Object getCurrentStatementCustomerMapping() {
			return currentStatementCustomerMapping;
		}

		public void setCurrentStatementCustomerMapping(Object currentStatementCustomerMapping) {
			this.currentStatementCustomerMapping = currentStatementCustomerMapping;
		}

		public Object getLastStatementCustomerMapping() {
			return lastStatementCustomerMapping;
		}

		public void setLastStatementCustomerMapping(Object lastStatementCustomerMapping) {
			this.lastStatementCustomerMapping = lastStatementCustomerMapping;
		}

		public String getStatementDay() {
			return statementDay;
		}

		public void setStatementDay(String statementDay) {
			this.statementDay = statementDay;
		}

		public String getFixedDueDay() {
			return fixedDueDay;
		}

		public void setFixedDueDay(String fixedDueDay) {
			this.fixedDueDay = fixedDueDay;
		}

		public int getDueDaysFromStatementDate() {
			return dueDaysFromStatementDate;
		}

		public void setDueDaysFromStatementDate(int dueDaysFromStatementDate) {
			this.dueDaysFromStatementDate = dueDaysFromStatementDate;
		}

		public String getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Object getClosureReason() {
			return closureReason;
		}

		public void setClosureReason(Object closureReason) {
			this.closureReason = closureReason;
		}

		public CardType getCardType() {
			return cardType;
		}

		public void setCardType(CardType cardType) {
			this.cardType = cardType;
		}

        
    }
    
    public class CardType {
    	@JsonProperty("cardCategory")
        private String cardCategory;

        @JsonProperty("cardCategoryLevel")
        private int cardCategoryLevel;

        @JsonProperty("noOfBalanceTransferAllowed")
        private int noOfBalanceTransferAllowed;

        @JsonProperty("annualInterestRate")
        private double annualInterestRate;

        @JsonProperty("monthlyInterestRate")
        private double monthlyInterestRate;

        @JsonProperty("minAmountEmi")
        private double minAmountEmi;

        @JsonProperty("minTenureEmi")
        private int minTenureEmi;

        @JsonProperty("maxTenureEmi")
        private int maxTenureEmi;

        @JsonProperty("noOfLongueSpace")
        private int noOfLongueSpace;

        @JsonProperty("noOfCashKnockOff")
        private int noOfCashKnockOff;

        @JsonProperty("cardFee")
        private double cardFee;

        @JsonProperty("cardClosureFee")
        private double cardClosureFee;

        @JsonProperty("memberShipFee")
        private double memberShipFee;

        @JsonProperty("freePhysicalStatement")
        private int freePhysicalStatement;

        @JsonProperty("physicalStatementFee")
        private double physicalStatementFee;

        @JsonProperty("overLimitFee")
        private String overLimitFee;

        @JsonProperty("latePaymentFee")
        private String latePaymentFee;

        @JsonProperty("txnLimit")
        private double txnLimit;

        @JsonProperty("membershipWaiverAmount")
        private double membershipWaiverAmount;

        @JsonProperty("membershipRenewal")
        private String membershipRenewal;

        @JsonProperty("membershipWaiverPeriod")
        private String membershipWaiverPeriod;

        @JsonProperty("statementDays")
        private String statementDays;

        @JsonProperty("fixedDueDays")
        private String fixedDueDays;

        @JsonProperty("forexMarkup")
        private double forexMarkup;

        @JsonProperty("cardCurrencyCode")
        private String cardCurrencyCode;

        @JsonProperty("blockLimitForEmiInterest")
        private boolean blockLimitForEmiInterest;

        @JsonProperty("instantLateFee")
        private boolean instantLateFee;

        @JsonProperty("includeOverLimitForMinDueCalculation")
        private boolean includeOverLimitForMinDueCalculation;

        @JsonProperty("overLimitminDuePercentage")
        private double overLimitminDuePercentage;

        @JsonProperty("maxAllowedLoan")
        private double maxAllowedLoan;

        @JsonProperty("allowedDaysAfterDueDayForEntireStmtLoan")
        private int allowedDaysAfterDueDayForEntireStmtLoan;

        @JsonProperty("minDaysEmiConversion")
        private int minDaysEmiConversion;

        @JsonProperty("allowBilledTxnForEmi")
        private boolean allowBilledTxnForEmi;

        @JsonProperty("allowPartialKnockOffTxnForEmi")
        private boolean allowPartialKnockOffTxnForEmi;

        @JsonProperty("bounceFee")
        private String bounceFee;

        @JsonProperty("physicalCardFee")
        private String physicalCardFee;

        @JsonProperty("bankPoolAccountNumber")
        private String bankPoolAccountNumber;

        @JsonProperty("bucketBasedRepaymentPushToRewards")
        private boolean bucketBasedRepaymentPushToRewards;

        @JsonProperty("enableRewards")
        private boolean enableRewards;

        @JsonProperty("freeFirstCardIssuance")
        private boolean freeFirstCardIssuance;

        @JsonProperty("expiryInMonths")
        private int expiryInMonths;

		public String getCardCategory() {
			return cardCategory;
		}

		public void setCardCategory(String cardCategory) {
			this.cardCategory = cardCategory;
		}

		public int getCardCategoryLevel() {
			return cardCategoryLevel;
		}

		public void setCardCategoryLevel(int cardCategoryLevel) {
			this.cardCategoryLevel = cardCategoryLevel;
		}

		public int getNoOfBalanceTransferAllowed() {
			return noOfBalanceTransferAllowed;
		}

		public void setNoOfBalanceTransferAllowed(int noOfBalanceTransferAllowed) {
			this.noOfBalanceTransferAllowed = noOfBalanceTransferAllowed;
		}

		public double getAnnualInterestRate() {
			return annualInterestRate;
		}

		public void setAnnualInterestRate(double annualInterestRate) {
			this.annualInterestRate = annualInterestRate;
		}

		public double getMonthlyInterestRate() {
			return monthlyInterestRate;
		}

		public void setMonthlyInterestRate(double monthlyInterestRate) {
			this.monthlyInterestRate = monthlyInterestRate;
		}

		public double getMinAmountEmi() {
			return minAmountEmi;
		}

		public void setMinAmountEmi(double minAmountEmi) {
			this.minAmountEmi = minAmountEmi;
		}

		public int getMinTenureEmi() {
			return minTenureEmi;
		}

		public void setMinTenureEmi(int minTenureEmi) {
			this.minTenureEmi = minTenureEmi;
		}

		public int getMaxTenureEmi() {
			return maxTenureEmi;
		}

		public void setMaxTenureEmi(int maxTenureEmi) {
			this.maxTenureEmi = maxTenureEmi;
		}

		public int getNoOfLongueSpace() {
			return noOfLongueSpace;
		}

		public void setNoOfLongueSpace(int noOfLongueSpace) {
			this.noOfLongueSpace = noOfLongueSpace;
		}

		public int getNoOfCashKnockOff() {
			return noOfCashKnockOff;
		}

		public void setNoOfCashKnockOff(int noOfCashKnockOff) {
			this.noOfCashKnockOff = noOfCashKnockOff;
		}

		public double getCardFee() {
			return cardFee;
		}

		public void setCardFee(double cardFee) {
			this.cardFee = cardFee;
		}

		public double getCardClosureFee() {
			return cardClosureFee;
		}

		public void setCardClosureFee(double cardClosureFee) {
			this.cardClosureFee = cardClosureFee;
		}

		public double getMemberShipFee() {
			return memberShipFee;
		}

		public void setMemberShipFee(double memberShipFee) {
			this.memberShipFee = memberShipFee;
		}

		public int getFreePhysicalStatement() {
			return freePhysicalStatement;
		}

		public void setFreePhysicalStatement(int freePhysicalStatement) {
			this.freePhysicalStatement = freePhysicalStatement;
		}

		public double getPhysicalStatementFee() {
			return physicalStatementFee;
		}

		public void setPhysicalStatementFee(double physicalStatementFee) {
			this.physicalStatementFee = physicalStatementFee;
		}

		public String getOverLimitFee() {
			return overLimitFee;
		}

		public void setOverLimitFee(String overLimitFee) {
			this.overLimitFee = overLimitFee;
		}

		public String getLatePaymentFee() {
			return latePaymentFee;
		}

		public void setLatePaymentFee(String latePaymentFee) {
			this.latePaymentFee = latePaymentFee;
		}

		public double getTxnLimit() {
			return txnLimit;
		}

		public void setTxnLimit(double txnLimit) {
			this.txnLimit = txnLimit;
		}

		public double getMembershipWaiverAmount() {
			return membershipWaiverAmount;
		}

		public void setMembershipWaiverAmount(double membershipWaiverAmount) {
			this.membershipWaiverAmount = membershipWaiverAmount;
		}

		public String getMembershipRenewal() {
			return membershipRenewal;
		}

		public void setMembershipRenewal(String membershipRenewal) {
			this.membershipRenewal = membershipRenewal;
		}

		public String getMembershipWaiverPeriod() {
			return membershipWaiverPeriod;
		}

		public void setMembershipWaiverPeriod(String membershipWaiverPeriod) {
			this.membershipWaiverPeriod = membershipWaiverPeriod;
		}

		public String getStatementDays() {
			return statementDays;
		}

		public void setStatementDays(String statementDays) {
			this.statementDays = statementDays;
		}

		public String getFixedDueDays() {
			return fixedDueDays;
		}

		public void setFixedDueDays(String fixedDueDays) {
			this.fixedDueDays = fixedDueDays;
		}

		public double getForexMarkup() {
			return forexMarkup;
		}

		public void setForexMarkup(double forexMarkup) {
			this.forexMarkup = forexMarkup;
		}

		public String getCardCurrencyCode() {
			return cardCurrencyCode;
		}

		public void setCardCurrencyCode(String cardCurrencyCode) {
			this.cardCurrencyCode = cardCurrencyCode;
		}

		public boolean isBlockLimitForEmiInterest() {
			return blockLimitForEmiInterest;
		}

		public void setBlockLimitForEmiInterest(boolean blockLimitForEmiInterest) {
			this.blockLimitForEmiInterest = blockLimitForEmiInterest;
		}

		public boolean isInstantLateFee() {
			return instantLateFee;
		}

		public void setInstantLateFee(boolean instantLateFee) {
			this.instantLateFee = instantLateFee;
		}

		public boolean isIncludeOverLimitForMinDueCalculation() {
			return includeOverLimitForMinDueCalculation;
		}

		public void setIncludeOverLimitForMinDueCalculation(boolean includeOverLimitForMinDueCalculation) {
			this.includeOverLimitForMinDueCalculation = includeOverLimitForMinDueCalculation;
		}

		public double getOverLimitminDuePercentage() {
			return overLimitminDuePercentage;
		}

		public void setOverLimitminDuePercentage(double overLimitminDuePercentage) {
			this.overLimitminDuePercentage = overLimitminDuePercentage;
		}

		public double getMaxAllowedLoan() {
			return maxAllowedLoan;
		}

		public void setMaxAllowedLoan(double maxAllowedLoan) {
			this.maxAllowedLoan = maxAllowedLoan;
		}

		public int getAllowedDaysAfterDueDayForEntireStmtLoan() {
			return allowedDaysAfterDueDayForEntireStmtLoan;
		}

		public void setAllowedDaysAfterDueDayForEntireStmtLoan(int allowedDaysAfterDueDayForEntireStmtLoan) {
			this.allowedDaysAfterDueDayForEntireStmtLoan = allowedDaysAfterDueDayForEntireStmtLoan;
		}

		public int getMinDaysEmiConversion() {
			return minDaysEmiConversion;
		}

		public void setMinDaysEmiConversion(int minDaysEmiConversion) {
			this.minDaysEmiConversion = minDaysEmiConversion;
		}

		public boolean isAllowBilledTxnForEmi() {
			return allowBilledTxnForEmi;
		}

		public void setAllowBilledTxnForEmi(boolean allowBilledTxnForEmi) {
			this.allowBilledTxnForEmi = allowBilledTxnForEmi;
		}

		public boolean isAllowPartialKnockOffTxnForEmi() {
			return allowPartialKnockOffTxnForEmi;
		}

		public void setAllowPartialKnockOffTxnForEmi(boolean allowPartialKnockOffTxnForEmi) {
			this.allowPartialKnockOffTxnForEmi = allowPartialKnockOffTxnForEmi;
		}

		public String getBounceFee() {
			return bounceFee;
		}

		public void setBounceFee(String bounceFee) {
			this.bounceFee = bounceFee;
		}

		public String getPhysicalCardFee() {
			return physicalCardFee;
		}

		public void setPhysicalCardFee(String physicalCardFee) {
			this.physicalCardFee = physicalCardFee;
		}

		public String getBankPoolAccountNumber() {
			return bankPoolAccountNumber;
		}

		public void setBankPoolAccountNumber(String bankPoolAccountNumber) {
			this.bankPoolAccountNumber = bankPoolAccountNumber;
		}

		public boolean isBucketBasedRepaymentPushToRewards() {
			return bucketBasedRepaymentPushToRewards;
		}

		public void setBucketBasedRepaymentPushToRewards(boolean bucketBasedRepaymentPushToRewards) {
			this.bucketBasedRepaymentPushToRewards = bucketBasedRepaymentPushToRewards;
		}

		public boolean isEnableRewards() {
			return enableRewards;
		}

		public void setEnableRewards(boolean enableRewards) {
			this.enableRewards = enableRewards;
		}

		public boolean isFreeFirstCardIssuance() {
			return freeFirstCardIssuance;
		}

		public void setFreeFirstCardIssuance(boolean freeFirstCardIssuance) {
			this.freeFirstCardIssuance = freeFirstCardIssuance;
		}

		public int getExpiryInMonths() {
			return expiryInMonths;
		}

		public void setExpiryInMonths(int expiryInMonths) {
			this.expiryInMonths = expiryInMonths;
		}
        
    }

}

