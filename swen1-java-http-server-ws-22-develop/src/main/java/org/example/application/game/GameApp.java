package org.example.application.game;
import org.example.application.game.controller.Scoreboard.ScoreboardController;
import org.example.application.game.controller.battle.BattleController;
import org.example.application.game.controller.cards.CardsController;
import org.example.application.game.controller.deck.DeckController;
import org.example.application.game.controller.newcards.NewCardsController;
import org.example.application.game.controller.packages.PackagesController;
import org.example.application.game.controller.session.SessionController;
import org.example.application.game.controller.tradings.TradingController;
import org.example.application.game.controller.transaction.TransactionsController;
import org.example.application.game.controller.user.*;
import org.example.application.game.controller.stats.*;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.repository.battle.BattleMemoryRepository;
import org.example.application.game.repository.battle.BattleRepository;
import org.example.application.game.repository.cards.CardsMemoryRepository;
import org.example.application.game.repository.cards.CardsRepository;
import org.example.application.game.repository.deck.DeckMemoryRepository;
import org.example.application.game.repository.deck.DeckRepository;
import org.example.application.game.repository.newcards.NewCardsMemoryRepository;
import org.example.application.game.repository.newcards.NewCardsRepository;
import org.example.application.game.repository.packages.PackagesMemoryRepository;
import org.example.application.game.repository.packages.PackagesRepository;
import org.example.application.game.repository.scoreboard.ScoreboardMemoryRepository;
import org.example.application.game.repository.scoreboard.ScoreboardRepository;
import org.example.application.game.repository.session.SessionMemoryRepository;
import org.example.application.game.repository.session.SessionRepository;
import org.example.application.game.repository.tradings.TradingMemoryRepository;
import org.example.application.game.repository.tradings.TradingRepository;
import org.example.application.game.repository.transaction.TransactionMemoryRepository;
import org.example.application.game.repository.transaction.TransactionRepository;
import org.example.application.game.repository.user.UserMemoryRepository;
import org.example.application.game.repository.user.UserRepository;
import org.example.application.game.repository.stats.*;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

import java.sql.SQLException;

public class GameApp implements Application {


    private UserController userController;
    private SessionController sessionController;
    private PackagesController packageController;
    private TransactionsController transactionsController;
    private CardsController cardsController;
    private DeckController deckController;
    private StatsController statsController;
    private ScoreboardController scoreboardController;
    private BattleController battleController;
    private TradingController tradingController;
    private NewCardsController newCardsController;
    /*private UserMemoryRepository userMemoryRepository;

    public GameApp() {
        this.inject();
    }*/

    public GameApp() throws SQLException {

        UserRepository userRepository = new UserMemoryRepository();
        this.userController = new UserController(userRepository);
        SessionRepository sessionRepository = new SessionMemoryRepository();
        this.sessionController = new SessionController(sessionRepository);
        PackagesRepository packagesRepository = new PackagesMemoryRepository();
        this.packageController = new PackagesController(packagesRepository);
        TransactionRepository transactionRepository = new TransactionMemoryRepository();
        this.transactionsController = new TransactionsController(transactionRepository);
        CardsRepository cardsRepository = new CardsMemoryRepository();
        this.cardsController = new CardsController(cardsRepository);
        DeckRepository deckRepository = new DeckMemoryRepository();
        this.deckController = new DeckController(deckRepository);
        StatsRepository statsRepository = new StatsMemoryRepository();
        this.statsController = new StatsController(statsRepository);
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        this.scoreboardController = new ScoreboardController(scoreboardRepository);
        BattleRepository battleRepository = new BattleMemoryRepository();
        this.battleController= new BattleController(battleRepository);
        TradingRepository tradingRepository = new TradingMemoryRepository();
        this.tradingController= new TradingController(tradingRepository);
        NewCardsRepository newCardsRepository = new NewCardsMemoryRepository();
        this.newCardsController= new NewCardsController(newCardsRepository);
        //databaseMemoryRepository db = new databaseMemoryRepository();
        //db.connect();
        //db.userToDb("Tamim","Test");
    }



    @Override
    public Response handle(Request request) throws SQLException {
        //System.out.println(Thread.currentThread());
        //System.out.println(request.getPath() + request.getRequest()+"\n___________________________");
        if (request.getPath().startsWith("/users")) {
            return userController.handle(request);
        }
        if (request.getPath().equals("/sessions")) {
            return sessionController.handle(request);
        }
        if (request.getPath().equals("/packages")) {
            return packageController.handle(request);
        }
        if (request.getPath().startsWith("/transactions")) {
            return transactionsController.handle(request);
        }
        if (request.getPath().equals("/cards")) {
            return cardsController.handle(request);
        }
        if (request.getPath().startsWith("/deck")) {
            //System.out.println("dies ist der path:      " + request.getPath());
            return deckController.handle(request);
        }
        if(request.getPath().equals("/stats")){
            return statsController.handle(request);
        }
        if(request.getPath().equals("/score")){
            return scoreboardController.handle(request);
        }
        if (request.getPath().equals("/battles")) {
            return battleController.handle(request);
        }
        if (request.getPath().startsWith("/tradings")) {
            return tradingController.handle(request);
        }
        if (request.getPath().startsWith("/newcards")) {
            return newCardsController.handle(request);
        }

        //TODO if fuer trades
        //TODO unique feature
        //TODO gescheite rückantwort schicken bei responses, statuscode & co.
        Response response = new Response();
        response.setStatusCode(StatusCode.NOT_FOUND);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(StatusCode.NOT_FOUND.message);

        return response;
    }

    /*private void inject() {
        this.userMemoryRepository = new UserMemoryRepository();
        this.userController = new UserController(this.userMemoryRepository);
    }*/
}
