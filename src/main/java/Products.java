import java.sql.ResultSet;
import java.sql.SQLException;

public class Products {
    String prod_ID;
    String prodName;
    String supp_ID;
    String suppName;
    Double price;

    public String getSupp_ID() {
        return supp_ID;
    }

    public void setSupp_ID(String supp_ID) {
        this.supp_ID = supp_ID;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProd_ID() {
        return prod_ID;
    }

    public void setProd_ID(String prod_ID) {
        this.prod_ID = prod_ID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    void setProducts(ResultSet resultSet){
        try {
            setProd_ID(resultSet.getString(1));
            setProdName(resultSet.getString(2));
            setSupp_ID(resultSet.getString(3));
            setSuppName(resultSet.getString(4));
            setPrice(resultSet.getDouble(5));

        } catch (SQLException e) {}
    }

    String getProducts(){
        String tuple = getProd_ID() + getProdName() + getSupp_ID()
                + getSuppName() + getPrice();

        return tuple;
    }
}
