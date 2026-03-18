package com.tiktak24.user.rideSharing.model;

public class RiderDetails {
    int riderNumber;
    String riderTitle,riderName,riderPhone,riderTotalFareLbl,riderTotalFareValue,riderPaymentModeLbl,riderPaymentModeValue,riderTotalSheetLbl,riderTotalSheetValue;



    public RiderDetails() {
    }

    public RiderDetails(int riderNumber, String riderTitle, String riderName, String riderPhone, String riderTotalFareLbl, String riderTotalFareValue, String riderPaymentModeLbl, String riderPaymentModeValue, String riderTotalSheetLbl, String riderTotalSheetValue) {
        this.riderNumber = riderNumber;
        this.riderTitle = riderTitle;
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.riderTotalFareLbl = riderTotalFareLbl;
        this.riderTotalFareValue = riderTotalFareValue;
        this.riderPaymentModeLbl = riderPaymentModeLbl;
        this.riderPaymentModeValue = riderPaymentModeValue;
        this.riderTotalSheetLbl = riderTotalSheetLbl;
        this.riderTotalSheetValue = riderTotalSheetValue;
    }

    public int getRiderNumber() {
        return riderNumber;
    }

    public void setRiderNumber(int riderNumber) {
        this.riderNumber = riderNumber;
    }

    public String getRiderTitle() {
        return riderTitle;
    }

    public void setRiderTitle(String riderTitle) {
        this.riderTitle = riderTitle;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public String getRiderTotalFareLbl() {
        return riderTotalFareLbl;
    }

    public void setRiderTotalFareLbl(String riderTotalFareLbl) {
        this.riderTotalFareLbl = riderTotalFareLbl;
    }

    public String getRiderTotalFareValue() {
        return riderTotalFareValue;
    }

    public void setRiderTotalFareValue(String riderTotalFareValue) {
        this.riderTotalFareValue = riderTotalFareValue;
    }

    public String getRiderPaymentModeLbl() {
        return riderPaymentModeLbl;
    }

    public void setRiderPaymentModeLbl(String riderPaymentModeLbl) {
        this.riderPaymentModeLbl = riderPaymentModeLbl;
    }

    public String getRiderPaymentModeValue() {
        return riderPaymentModeValue;
    }

    public void setRiderPaymentModeValue(String riderPaymentModeValue) {
        this.riderPaymentModeValue = riderPaymentModeValue;
    }

    public String getRiderTotalSheetLbl() {
        return riderTotalSheetLbl;
    }

    public void setRiderTotalSheetLbl(String riderTotalSheetLbl) {
        this.riderTotalSheetLbl = riderTotalSheetLbl;
    }

    public String getRiderTotalSheetValue() {
        return riderTotalSheetValue;
    }

    public void setRiderTotalSheetValue(String riderTotalSheetValue) {
        this.riderTotalSheetValue = riderTotalSheetValue;
    }
}
