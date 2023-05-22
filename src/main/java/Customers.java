import java.sql.ResultSet;

public class Customers {
    String cust_ID;
    String custName;

    public String getCust_ID() {
        return cust_ID;
    }

    public void setCust_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    void setCustomers(ResultSet resultSet){
        try{
            setCust_ID(resultSet.getString(1));
            setCustName(resultSet.getString(2));
        }
        catch(Exception e){}
    }

    String getCustomers(){
        String tuple = getCust_ID() + getCustName();
        return tuple;
    }
}
