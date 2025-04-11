package person;

interface User{
    public String name;
    public String NRIC;
    public String password;
    public MaritalStatus maritalStatus;
    public enum MaritalStatus { MARRIED, SINGLE};

    String getName();
    void setName(String name);
    void changePassword(String newPassword);
}