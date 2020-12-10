package com.sber.services;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.repositories.AccountRepositoryImp;
import com.sber.repositories.CardRepositoryImp;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardServiceImp implements CardService{

    private static Logger log = Logger.getLogger(CardServiceImp.class.getName());

    private final String SUCCESS = "SUCCESS";
    private final String UNSUCCESS = "UNSUCCESS";

    @Override
    public String addNewCardOnAccountNumber(Account account) {
        if (account != null && (account.getAccountNumber() != null && !account.getAccountNumber().isEmpty())) {
            AccountRepositoryImp accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());
            try {
                Optional<Account> changeAccount = accountRepositoryImp.getByNumber(account.getAccountNumber());
                if (changeAccount.isPresent()) {
                    Generator generator = new Generator();
                    Card newCard = Card.builder()
                            .cardNumber(generator.generateCardNumber())
                            .account(changeAccount.get())
                            .owner(changeAccount.get().getOwner())
                            .build();
                    CardRepositoryImp cardRepositoryImp = new CardRepositoryImp(ServiceDataSource.getDataSource());
                    cardRepositoryImp.save(newCard);
                    return SUCCESS;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Exception: ", ex);
            }
        }
        return UNSUCCESS;
    }

    private static class Generator {
        public String generateCardNumber() {
            String cardNumber = String.valueOf((int) (Math.random() * 9));;

            for (int i = 0; i < 15; i++) {
                if (i % 4 == 3) {
                    cardNumber = cardNumber + " ";
                }
                cardNumber = cardNumber + ((int) (Math.random() * 9));
            }
            return cardNumber;
        }
    }
}
