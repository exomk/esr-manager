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

package com.exo.esr.domain;

public enum ERegisterType {

    REGISTER_IN("01"),
    BREAK("02"),
    REGISTER_OUT("03");

    private String checkTypeValue;

    ERegisterType(String checkTypeValue) {
        this.checkTypeValue = checkTypeValue;
    }

    public String getCheckTypeValue() {
        return checkTypeValue;
    }

    public static ERegisterType findByValue(String checkTypeValue) {

        for (ERegisterType checkType : values()) {
            if (checkType.getCheckTypeValue().equals(checkTypeValue)) {
                return checkType;
            }
        }

        return null;
    }
}
