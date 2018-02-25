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

package com.exo.esr.converter;


import com.exo.esr.domain.User;
import com.exo.esr.service.IRegistrationDbEao;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Created by atanasko on 11/13/17.
 */
@RequestScoped
@ManagedBean(name = "userConverterBean")
public class UserConverter implements Converter {

    @EJB
    private IRegistrationDbEao registrationDbEao;

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        User user = null;

        if (value != null && !value.isEmpty()) {
            user = registrationDbEao.getUser(Integer.parseInt(value));
        }

        return user;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
        return obj.toString();
    }

}

