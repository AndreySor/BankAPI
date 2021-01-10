package com.sber.services;

import com.sber.models.Card;
import com.sber.models.User;
import com.sber.repositories.UserRepository;
import com.sber.repositories.UserRepositoryImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImp implements UserService{

    private static Logger log = Logger.getLogger(UserServiceImp.class.getName());

    @Override
    public List<Card> returnListCards(User user) {
        List<Card> cards = new ArrayList<>();
        if (user != null && (user.getUserId() != null && user.getUserId() != 0)) {
            UserRepository userRepositoryImp = new UserRepositoryImp(ServiceDataSource.getDataSource());
            Optional<User> optional = null;
            try {
                optional = userRepositoryImp.get(user.getUserId());
                if (optional.isPresent()) {
                    User findUser = optional.get();
                    cards = findUser.getCards();
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Exception: ", ex);
                throw new NotSavedSubEntityException("problems with the cards list");
            }
        }
        return cards;
    }
}
