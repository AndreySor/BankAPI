package com.sber.services;

import com.sber.models.Card;
import com.sber.models.User;
import java.util.List;

public interface UserService {
    public List<Card> returnListCards(User user);
}
