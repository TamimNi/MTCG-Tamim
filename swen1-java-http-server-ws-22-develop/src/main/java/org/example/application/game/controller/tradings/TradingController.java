package org.example.application.game.controller.tradings;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.application.game.model.tradings.Trading;
import org.example.application.game.repository.stats.*;
import org.example.application.game.model.stats.*;
import org.example.application.game.repository.tradings.TradingRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradingController {
    private final TradingRepository tradingRepository;

    public TradingController(TradingRepository tradingRepository) {
        this.tradingRepository = tradingRepository;
    }

    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals(Method.GET.method) || request.getMethod().equals(Method.POST.method) || request.getMethod().equals(Method.DELETE.method)) {
            return create(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(StatusCode.METHODE_NOT_ALLOWED.message);

        return response;
    }

    private Response create(Request request) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = null;
        //TODO hier trading
        String json = request.getContent();
        Trading trading = null;

        boolean createOffer = false;
        String[] pathTradingID = request.getPath().split("/");
        List<Trading> tradingList = new ArrayList<>();
        if(request.getPath().equals("/tradings")){
            createOffer = true; //JustCreating the offer
        }

        Response response = new Response();

        if(request.getMethod().equals(Method.GET.method)){
            tradingList = tradingRepository.getTrades(request);
            try {
                content = objectMapper.writeValueAsString(tradingList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            response.setContentType(ContentType.APPLICATION_JSON);
        }
        else if(request.getMethod().equals(Method.POST.method)){
            if(createOffer){
                try {
                    trading = objectMapper.readValue(json, Trading.class);
                } catch (JsonProcessingException e) {
                    System.out.println(" :( " + e);
                    throw new RuntimeException(e);
                }
                //System.out.println(trading.getCardToTrade()+" "+trading.getTradingID());
                trading = tradingRepository.offerTrade(request, trading);
                try {
                    content = objectMapper.writeValueAsString(trading);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                response.setContentType(ContentType.APPLICATION_JSON);
            }
            else{//accept offer
                String card = request.getContent();
                card = card.replaceAll("\"", "");
                trading = tradingRepository.aquireTrade(request, pathTradingID[2], card);
                try {
                    content = objectMapper.writeValueAsString(trading);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                response.setContentType(ContentType.APPLICATION_JSON);
            }
        }
        else if(request.getMethod().equals(Method.DELETE.method)){
            content = tradingRepository.cancelTrade(request, pathTradingID[2]);
            response.setContentType(ContentType.TEXT_PLAIN);
        }
        //trading = null;//statsRepository.getStatOfUser(request);
//TODO correct responses to all 4
/*
        try {
            content = objectMapper.writeValueAsString(trading);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
  */    response.setStatusCode(StatusCode.OK);
        response.setContent(content);
        return response;
    }
}

