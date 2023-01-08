package org.example.server;

import org.example.server.dto.Request;
import org.example.server.dto.Response;

import java.sql.SQLException;

public interface Application {

    Response handle(Request request) throws SQLException;
}
