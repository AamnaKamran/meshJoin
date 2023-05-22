import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MasterData {
    List<Customers> customers = new ArrayList<>();
    List<Products> products = new ArrayList<>();

    void setMasterData(ResultSet custs, ResultSet prods){
        if(custs!=null) {
            Customers c = new Customers();
            c.setCustomers(custs);
            customers.add(c);
        }

        if(prods!=null) {
            Products p = new Products();
            p.setProducts(prods);
            products.add(p);
        }
    }

    String getMasterDataC(int i){
        String tuple = customers.get(i).getCustomers();
        return tuple;
    }

    String getMasterDataP(int i){
        String tuple = products.get(i).getProducts();
        return tuple;
    }
}
