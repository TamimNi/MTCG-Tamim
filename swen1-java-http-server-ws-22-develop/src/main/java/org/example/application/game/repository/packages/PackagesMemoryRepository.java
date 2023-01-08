package org.example.application.game.repository.packages;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.packages.Packages;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.List;

public class PackagesMemoryRepository implements PackagesRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();

    @Override
    public List<Packages> createPackage(List<Packages> packages, Request request) throws SQLException {
        boolean success;
        db.connect();
            success = db.packagesToDB(packages, request.getAuthorization());
        db.close();

        if(success) return packages;
        else return null;
    }
}
