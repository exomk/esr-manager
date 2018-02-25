/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2018  EXO Service Solutions
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 * You can contact us at contact4exo@exo.mk
 */

package com.exo.esr.view;

import com.exo.esr.auth.RegistrationAuthenticator;
import com.exo.esr.domain.ERole;
import com.exo.esr.domain.User;
import com.exo.esr.service.IRegistrationDbEao;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;


@SessionScoped
@ManagedBean(name = "loginView", eager = true)
public class LoginView implements Serializable, IESRManagerView {

    private String username;

    private String password;

    private User currentUser;

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @EJB
    RegistrationAuthenticator registrationAuthenticator;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        FacesMessage errorMessage;

        currentUser = registrationDbEao.getUser(username);
        if (registrationAuthenticator.isValidUser(username, password)) {
            String navigationString;

            switch (currentUser.getRole()) {
                case ADMIN:
                    navigationString = "/view/chart?faces-redirect=true";
                    break;
                case MANAGER:
                    navigationString = "/login?faces-redirect=true";
                    break;
                case USER:
                    navigationString = "/view/report?faces-redirect=true";
                    break;
                default:
                    navigationString = "/login?faces-redirect=true";
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", currentUser);

            return navigationString;
        }


        errorMessage = new FacesMessage("Please enter correct username and Password!");
        errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, errorMessage);

        return "/login?faces-redirect=true";
    }

    public boolean getIsUserRole() {
        return currentUser != null && currentUser.getRole() == ERole.USER;
    }

    /**
     * Invalidate the session
     *
     * @return Login redirect URL
     */
    public String logout() {

        currentUser = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "/login?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

}
