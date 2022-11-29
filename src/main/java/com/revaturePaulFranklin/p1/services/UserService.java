package com.revaturePaulFranklin.p1.services;

import com.revaturePaulFranklin.p1.daos.UserDAO;
import com.revaturePaulFranklin.p1.dtos.requests.LoginRequest;
import com.revaturePaulFranklin.p1.dtos.requests.NewUserRequest;
import com.revaturePaulFranklin.p1.dtos.responses.Principal;
import com.revaturePaulFranklin.p1.models.User;
import com.revaturePaulFranklin.p1.utils.custom_exceptions.InvalidAuthenticationException;
import com.revaturePaulFranklin.p1.utils.custom_exceptions.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

//    DAOs
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(NewUserRequest requestToCreateNewUser) {
        User newUser = new User(UUID.randomUUID().toString(), requestToCreateNewUser.getUsername(), requestToCreateNewUser.getEmail(), requestToCreateNewUser.getPassword(), requestToCreateNewUser.getGivenName(), requestToCreateNewUser.getSurname(), requestToCreateNewUser.getActive());

        newUserHasPassword(newUser);

        userDAO.createUser(newUser);
    }

    public Principal loginUser(LoginRequest requestToLoginUser) {
        User user = userDAO.getUserByLogin(requestToLoginUser.getUsername(), requestToLoginUser.getPassword());
        if (user == null)
        {
            logger.error("Failed to get User by login from service.");
            throw new InvalidAuthenticationException();
        }

        return new Principal(user.getUserId(), user.getUsername(), user.getRole());
    }

    private void newUserHasPassword(User newUser)
    {
        if (newUser.getPassword().length() == 0)
        {
            logger.error("Failed to create new User by login from service because new user has no password.");
            throw new InvalidUserException();
        }
    }
}
