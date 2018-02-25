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

package com.exo.esr.validator;

import com.exo.esr.domain.User;
import com.exo.esr.service.IRegistrationDbEao;
import org.primefaces.component.inputtext.InputText;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created by atanasko on 11/21/17.
 */
@ManagedBean(name = "uniqueUsernameValidator")
@RequestScoped
public class UniqueUsernameValidator implements Validator {

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        User user;
        String username;

        username = (String) o;
        user = registrationDbEao.getUser(username);
        if (user != null) {
            InputText inputText;
            FacesMessage errorMessage;

            errorMessage = new FacesMessage("Username is already in use!");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(null, errorMessage);

            inputText = (InputText) uiComponent;
            inputText.setValid(false);
        }
    }
}
