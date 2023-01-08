package org.example.application.game.repository.packages;
import org.example.application.game.model.packages.Packages;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.List;

public interface PackagesRepository {
    List<Packages> createPackage(List<Packages> packages, Request request) throws SQLException;
}
