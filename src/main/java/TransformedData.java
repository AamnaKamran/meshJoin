import java.util.Calendar;

public class TransformedData {
    String transID;
    String prodID;
    String custID;
    String storeID;
    String storeName;
    String timeID;
    String tDate;
    double quantity;
    String custName;
    String suppID;
    String suppName;
    double price;
    String prodName;
    double sale;

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTimeID() {
        return timeID;
    }

    public void setTimeID(String timeID) {
        this.timeID = timeID;
    }

    public String gettDate() {
        return tDate;
    }

    public int[] getDateMonthYear(){
        int[] dmy = new int[3];

        String d = String.valueOf(tDate.charAt(0) + tDate.charAt(1));
        String m = String.valueOf(tDate.charAt(3) + tDate.charAt(4));
        String y = String.valueOf(tDate.charAt(6) + tDate.charAt(7));

        dmy[0] = Integer.parseInt(d);
        dmy[1] = Integer.parseInt(m);
        dmy[2] = Integer.parseInt(y);

        return dmy;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getSuppID() {
        return suppID;
    }

    public void setSuppID(String suppID) {
        this.suppID = suppID;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    String getDay(){
        int[] dmy = getDateMonthYear();

        Calendar c = Calendar.getInstance();
        c.set(dmy[2], dmy[1]-1, dmy[0]);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String day = "";
        switch(dayOfWeek){
            case 1:
                day="Sunday";
                break;
            case 2:
                day="Monday";
                break;
            case 3:
                day="Tuesday";
                break;
            case 4:
                day="Wednesday";
                break;
            case 5:
                day="Thursday";
                break;
            case 6:
                day="Friday";
                break;
            case 7:
                day="Saturday";
                break;
        }
        return day;
    }

    String getTransformedData(){
        String tuple = getCustID() + getCustName() + getStoreName() + getCustName();
        return tuple;
    }
}
