import java.sql.ResultSet;

public class Transactions {
    String trans_ID;
    String prod_ID;
    String cust_ID;
    String store_ID;
    String storeName;
    String time_ID;
    String tDate;
    Double Quantity;

    public String getTrans_ID() {
        return trans_ID;
    }

    public void setTrans_ID(String trans_ID) {
        this.trans_ID = trans_ID;
    }

    public String getProd_ID() {
        return prod_ID;
    }

    public void setProd_ID(String prod_ID) {
        this.prod_ID = prod_ID;
    }

    public String getCust_ID() {
        return cust_ID;
    }

    public void setCust_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }

    public String getTime_ID() {
        return time_ID;
    }

    public void setTime_ID(String time_ID) {
        this.time_ID = time_ID;
    }

    public String gettDate() {
        return tDate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    void setTransactions(ResultSet resultSet){
        try {
            setTrans_ID(resultSet.getString(1));
            setProd_ID(resultSet.getString(2));
            setCust_ID(resultSet.getString(3));
            setStore_ID(resultSet.getString(4));
            setStoreName(resultSet.getString(5));
            setTime_ID(resultSet.getString(6));
            settDate(resultSet.getString(7));
            setQuantity(resultSet.getInt(8));
        }
        catch(Exception e){}
    }

    public String getTransaction(){

        String tuple = getTrans_ID() + getProd_ID() + getCust_ID()
                + getStore_ID() + getStoreName() + getTime_ID()
                + gettDate() + getQuantity();

        return tuple;
    }
}
