import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class MeshJoin {
    MasterData diskBuffer;

    Hashtable<String, String> hashTableC;
    Hashtable<String, String> hashTableP;

    Queue<Transactions> queue;
    List<Transactions> streamBuffer;

    List<TransformedData> transformedData; // of length == length of Transactions

    Transactions transaction;

    // we have 5 partitions, 30 records each
    int numPartitions = 5;

    // each partition: 20 products + 10 customers from master data
    int recordNumC = 10;
    int recordNumP = 20;

    int repsT = 0; // to keep track where i left off in the transactions table
    int repsC = 0; // to keep track where i left off in the customers table
    int repsP = 0; // to keep track where i left off in the products table

    List<Customers> cTmp;
    List<Products> pTmp;

    public MeshJoin() {
        queue = new LinkedList<>();
        transformedData = new ArrayList<>();
    }

    // load all transactions to the stream buffer -> QUEUE
    public void loadTransactions(Connection con){
        streamBuffer = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from transactions");

            while (resultSet.next()) {
                transaction = new Transactions();
                transaction.setTransactions(resultSet);
                streamBuffer.add(transaction);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

//        for (int i = 0; i < streamBuffer.size(); i++) {
//            System.out.println(streamBuffer.get(i).getTransaction());
//        }
    }

    // load customers and products to disk buffer
    public void loadMasterData(Connection con){
        diskBuffer = new MasterData();

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from customers");

            while (resultSet.next()) {
                diskBuffer.setMasterData(resultSet,null);
            }

            resultSet = stmt.executeQuery("select * from products");

            while (resultSet.next()) {
                diskBuffer.setMasterData(null, resultSet);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

//        System.out.println("CUSTOMERS");

//        for (int j = 0; j < diskBuffer.customers.size(); j++) {
//                System.out.println(diskBuffer.getMasterDataC(j));
//        }
//
//        System.out.println("PRODUCTS");
//        for (int j = 0; j < diskBuffer.products.size(); j++) {
//                System.out.println(diskBuffer.getMasterDataP(j));
//        }

    }

    private void loadToQueue(){
        queue = new LinkedList<>();

        for (int i = repsT*numPartitions; i < (repsT*numPartitions)+numPartitions ;i++) {
            queue.add(streamBuffer.get(i));
        }
//        System.out.println(queue.size());
    }

    private void loadToHashTable(){
        hashTableC = new Hashtable<>();
        hashTableP = new Hashtable<>();

        Queue<Transactions> tmp = new LinkedList<>();

        for (int i = repsT*numPartitions; i < (repsT*numPartitions)+numPartitions ;i++) {
            tmp.add(streamBuffer.get(i));
        }

        for (int i = 0; i < numPartitions; i++) {
            Transactions t = tmp.remove();
            hashTableC.put(t.cust_ID, t.getTransaction());
            hashTableP.put(t.prod_ID, t.getTransaction());
        }

        repsT++;

//       System.out.println(hashTableC);
//       System.out.println(hashTableP);
    }

    private void getMasterDataPartition(){
        if(repsC == 5 && repsP == 5){
            repsC = 0;
            repsP = 0;
        }

        cTmp = new ArrayList<>();
        pTmp = new ArrayList<>();

        // load first 10 elements from products
        for (int j = repsC * recordNumC; j < (repsC * recordNumC) + recordNumC; j++) {
            cTmp.add(diskBuffer.customers.get(j));
        }
        repsC++;

        // load first 20 elements from products
        for (int j = repsP * recordNumP; j < (repsP * recordNumP) + recordNumP; j++) {
            pTmp.add(diskBuffer.products.get(j));
        }
        repsP++;

//        System.out.println(cTmp.get(0).getCustomers());
//        System.out.println(pTmp.get(0).getProducts());
    }

    public void callJoin(){

        for (int i = 0; i < streamBuffer.size()/numPartitions; i++) {
            //System.out.println(streamBuffer.size()/numPartitions);

            loadToQueue();
            loadToHashTable();

            // 1. take records from master data [DONE ^]
            // 2. compare their ids with those in the hashtable (multithreaded?)
            // 3. if present, join with record in queue
            // 4. repeat until all the records in partition are checked against queue record
            // 5. move to next queue element
            // 6. repeat steps 1 to 5 for each partition

            TransformedData td = null;
            for(Transactions t : queue) {
                td = new TransformedData();
                td.setTransID(t.trans_ID);
                for (int j = 0; j < numPartitions; j++) {
                    getMasterDataPartition();
                    join(cTmp, pTmp, t, td);
                }
                transformedData.add(td);
            }
        }

        for (int j = 0; j < transformedData.size(); j++) {
            System.out.println(transformedData.get(j).getTransformedData());
        }
//       System.out.println(transformedData.get(0).getTransformedData());
//       System.out.println(transformedData.size());

    }

    private void join(List<Customers> cTmp, List<Products> pTmp, Transactions t, TransformedData td){

        //System.out.println(t.getTransaction());

        for (int i = 0; i < cTmp.size(); i++) {
            //System.out.println("inside customers");
            Customers c = cTmp.get(i); // cTmp is master data "customers"
            if (hashTableC.containsKey(c.cust_ID)) {
                if (t.cust_ID.equals(c.cust_ID)) {
                    td.setCustID(c.cust_ID);
                    td.setCustName(c.custName);
                }
            }
        }

        for (int i = 0; i < pTmp.size(); i++) {
            //System.out.println("inside products");
            Products p = pTmp.get(i); // cTmp is master data "customers"
            if (hashTableP.containsKey(p.prod_ID)) {
                if (t.prod_ID.equals(p.prod_ID)) {
                    td.setProdID(p.prod_ID);
                    td.setProdName(p.prodName);
                    td.setPrice(p.price);
                    td.setSuppID(p.supp_ID);
                    td.setSuppName(p.suppName);
                }
            }
        }

        td.setStoreID(t.store_ID);
        td.setStoreName(t.storeName);
        td.setTimeID(t.time_ID);
        td.settDate(t.tDate);
        td.setQuantity(t.Quantity);
        if(td.getQuantity()!=null && td.getPrice()!=null) {
            td.setSale(td.getQuantity() * td.getPrice());
        }

        //System.out.println(td.getTransformedData());
    }

    public void transformedToStar(Connection con){

        int[] dmy = new int[3];
        try {
            for(TransformedData td : transformedData) {

                String query = " INSERT IGNORE INTO CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME)"
                        + " values (?, ?)";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getCustID());
                preparedStmt.setString (2, td.getCustName());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("CUSTOMERS");

                query = " INSERT IGNORE INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, PRICE)"
                        + " values (?, ?, ?)";

                // create the mysql insert preparedstatement
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getProdID());
                preparedStmt.setString (2, td.getProdName());
                preparedStmt.setDouble (3, td.getPrice());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("PRODUCTS");

                query = " INSERT IGNORE INTO STORE (STORE_ID, STORE_NAME)"
                        + " values (?, ?)";

                // create the mysql insert preparedstatement
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getStoreID());
                preparedStmt.setString (2, td.getStoreName());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("STORE");

                query = " INSERT IGNORE INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME)"
                        + " values (?, ?)";

                // create the mysql insert preparedstatement
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getSuppID());
                preparedStmt.setString (2, td.getSuppName());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("SUPPLIER");

                query = " INSERT IGNORE INTO TIME (TIME_ID, DATE, MONTH, QUARTER, YEAR, DAY)"
                        + " values (?, ?, ?, ?, ?, ?)";

                // create the mysql insert preparedstatement
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getTimeID());

                String str[] = td.gettDate().split("-");
                dmy[2] = Integer.parseInt(str[0]);
                dmy[1] = Integer.parseInt(str[1]);
                dmy[0] = Integer.parseInt(str[2]);

                //System.out.println(dmy[0]);
                //System.out.println(dmy[1]);
                //System.out.println(dmy[2]);
                preparedStmt.setInt (2, dmy[0]);
                preparedStmt.setInt (3, dmy[1]);
                int q = 0;
                if(dmy[1]<=3)
                    q = 1;
                else if(dmy[1]<=6)
                    q = 2;
                else if(dmy[1]<=9)
                    q = 3;
                else
                    q = 4;
                preparedStmt.setInt (4, q);
                preparedStmt.setInt (5, dmy[2]);
                preparedStmt.setString (6, td.getDay());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("TIME");
//
                query = " INSERT IGNORE INTO METROTRANSACTIONS (PRODUCT_ID, SUPPLIER_ID, CUSTOMER_ID, STORE_ID, TIME_ID, QUANTITY, SALES)"
                        + " values (?, ?, ?, ?, ?, ?, ?)";

                // create the mysql insert preparedstatement
                preparedStmt = con.prepareStatement(query);
                preparedStmt.setString (1, td.getProdID());
                preparedStmt.setString (2, td.getSuppID());
                preparedStmt.setString (3, td.getCustID());
                preparedStmt.setString (4, td.getStoreID());
                preparedStmt.setString (5, td.getTimeID());
                preparedStmt.setDouble (6, td.getQuantity());
                preparedStmt.setDouble (7, td.getSale());

                // execute the preparedstatement
                preparedStmt.execute();

                //System.out.println("FACT TABLE");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
