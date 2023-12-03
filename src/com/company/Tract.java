package com.company;

public class Tract {
    
    private int tractNo;
    private String pin;
    private String structureType;
    private String ownerStatus;
    private String contactStatus;
    private String name;
    private String coordinates;
    private String address;
    private String phoneNo;
    private String occupentNo;
    private String worksLand;
    private String contacted;
    private String attemptDetails;
    private String consultationDate;
    private String followUP;
    private String Comments;
    private String callDetails;
    private boolean used;
    
    public Tract (){
        
    }
    
    public Tract (int tractNo, String pin, String structureType,String ownerStatus,
            String contactStatus, String name, String coordinates, String address, String phoneNo, String occupentNo,
            String worksLand, String contacted, String attemptDetails, String consultationDate,
            String followUP, String Comments, String callDetails, boolean used){
        
        setTractNo(tractNo);
        setPin(pin);
        setStructureType(structureType);
        setOwnerStatus(ownerStatus);
        setContactStatus(contactStatus);
        setName(name);
        setCoordinates(coordinates);
        setAddress(address);
        setPhoneNo(phoneNo);
        setOccupentNo(occupentNo);
        setWorksLand(worksLand);
        setContacted(contacted);
        setAttemptDetails(attemptDetails);
        setConsultationDate(consultationDate);
        setFollowUP(followUP);
        setComments(Comments);       
        setCallDetails(callDetails);
        setUsed(used);
    }

    public int getTractNo() {
        return tractNo;
    }

    public void setTractNo(int tractNo) {
        this.tractNo = tractNo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getOwnerStatus() {
        return ownerStatus;
    }

    public void setOwnerStatus(String ownerStatus) {
        this.ownerStatus = ownerStatus;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
        public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOccupentNo() {
        return occupentNo;
    }

    public void setOccupentNo(String occupentNo) {
        this.occupentNo = occupentNo;
    }

    public String getWorksLand() {
        return worksLand;
    }

    public void setWorksLand(String worksLand) {
        this.worksLand = worksLand;
    }

    public String getContacted() {
        return contacted;
    }

    public void setContacted(String contacted) {
        this.contacted = contacted;
    }

    public String getAttemptDetails() {
        return attemptDetails;
    }

    public void setAttemptDetails(String attemptDetails) {
        this.attemptDetails = attemptDetails;
    }

    public String getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(String consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getFollowUP() {
        return followUP;
    }

    public void setFollowUP(String followUP) {
        this.followUP = followUP;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String Comments) {
        this.Comments = Comments;
    }
    
    public String getCallDetails() {
        return callDetails;
    }

    public void setCallDetails(String callDetails) {
        this.callDetails = callDetails;
    }
    
    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean FindMultitract(Tract other) {

        boolean test = false;

        if (other.getName() == this.getName()) {
            test = true;
        }

        return false;
    }
    
}
