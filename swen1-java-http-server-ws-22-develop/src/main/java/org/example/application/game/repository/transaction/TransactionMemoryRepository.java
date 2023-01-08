package org.example.application.game.repository.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.session.Session;
import org.example.application.game.model.transaction.Transactions;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionMemoryRepository implements TransactionRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public List<Transactions> aquirePackage(Request request) throws SQLException {
        db.connect();
        List<Map<String, Object>> package1 = db.aquirePackageFromDB(request.getAuthorization());
        db.close();
        List<Transactions> transactionsList = new ArrayList<>();
        System.out.println("..");
        //System.out.println(package1.get(0));
        //System.out.println(package1.get(1));
        for(Map<String, Object> pkg : package1){
           // System.out.println("diese: "+li);
            //System.out.println("hier "+(String) pkg.get("id"));
            //System.out.println("heyyo: "+pkg);
            Transactions transaction = new Transactions();

            transaction.setId((String) pkg.get("id"));
            transaction.setName((String) pkg.get("name"));
            //transaction.setDamage(Double.parseDouble((String) pkg.get("damage")));
            transaction.setDamage((Double) pkg.get("damage"));
            /*System.out.println("fuuuuu");
            System.out.println(transaction.getId());
            System.out.println(transaction.getName());
            System.out.println(transaction.getDamage());*/
            transactionsList.add(transaction);
        }
        /*for (Map<String, Object> pkg : package1) {
            Transactions transaction = new Transactions();
            transaction.setId((String) pkg.get("id"));
            transaction.setName((String) pkg.get("name"));
            transaction.setDamage((double) pkg.get("damage"));
            System.out.println((String) pkg.get("name"));

            System.out.println("heyyo: "+pkg);
            transactionsList.add(transaction);
        }*/
        return transactionsList;

    }
}
