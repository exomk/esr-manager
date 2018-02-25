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

import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.selectonemenu.SelectOneMenu;

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

@ManagedBean(name = "userProfileValidator")
@RequestScoped
public class UserProfileValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String profile;
        Fieldset supplierField;
        Fieldset buyerField;

        profile = (String) o;
        supplierField = (Fieldset) FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:supplierFieldset");
        buyerField = (Fieldset) FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:buyerFieldset");

        if(profile.equals("-- select --")){
            SelectOneMenu selectOneMenu;
            FacesMessage errorMessage;

            supplierField.setRendered(false);
            buyerField.setRendered(false);

            errorMessage = new FacesMessage("User profile: User profile not selected.");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(null, errorMessage);

            selectOneMenu = (SelectOneMenu) uiComponent;
            selectOneMenu.setValid(false);
        } else {
            supplierField.setRendered(true);
            buyerField.setRendered(true);
        }

    }
}