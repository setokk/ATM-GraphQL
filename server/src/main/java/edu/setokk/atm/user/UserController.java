package edu.setokk.atm.user;

import edu.setokk.atm.auth.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping(name = "login")
    public String loginUser(@Argument String username,
                            @Argument String password) {

        User user = userService.loginUser(username, password);
        return JwtUtils.generateJWT(user);
    }

    @MutationMapping(name = "register")
    public String registerUser(@Argument String username,
                               @Argument String password,
                               @Argument String email) {

        User user = userService.registerUser(username, password, email);
        return JwtUtils.generateJWT(user);
    }

    @PreAuthorize("hasRole('USER')")
    @MutationMapping(name = "deposit")
    public Boolean depositAmount(@Argument BigDecimal amount) {
        User authUser = getAuthenticatedUser();
        if (authUser == null)
            return false;

        userService.deposit(authUser, amount);
        return true;
    }

    @PreAuthorize("hasRole('USER')")
    @MutationMapping(name = "withdraw")
    public Boolean withdrawAmount(@Argument BigDecimal amount) {
        User authUser = getAuthenticatedUser();
        if (authUser == null)
            return false;

        userService.withdraw(authUser, amount);
        return true;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User))
            return null;

        return (User) authentication.getPrincipal();
    }
}
