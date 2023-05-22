import java.text.ParseException;

public class main {
    public static void main(String args[]) throws ParseException {

        MyJDBC.connectDatabase();

        MeshJoin meshJoin = new MeshJoin();

        meshJoin.loadTransactions(MyJDBC.connection2); //db
        meshJoin.loadMasterData(MyJDBC.connection2); //db

        meshJoin.callJoin();

        meshJoin.transformedToStar(MyJDBC.connection1); //metro

    }
}
