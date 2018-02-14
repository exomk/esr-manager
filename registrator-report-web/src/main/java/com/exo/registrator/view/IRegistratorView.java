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

package com.exo.registrator.view;

/**
 * Created by atanasko on 11/21/17.
 */
public interface IRegistratorView {

    String JS_ROW_EDITOR =
            "$( document ).ready(function() { " +
                "$('a.ui-row-editor-pencil').on('click', function(){" +
                    "$(this).parent('div.ui-row-editor').css('width', '100%');" +
                    "$(this).closest('td').find('button.btn-red-color').hide();" +
                "});" +
            "});";

    String JS_ADD =
            "var btnSave = $('tbody.ui-datatable-data tr:first-child td div.ui-row-editor a.ui-row-editor-check');" +
            "$('tbody.ui-datatable-data tr:first-child td div.ui-row-editor a.ui-row-editor-pencil').click();" +
            "$('tbody.ui-datatable-data tr:first-child td div.ui-row-editor').css('width', '100%');" +
            "$('tbody.ui-datatable-data tr:first-child td button.btn-red-color').hide();" +

            "btnSave.addClass('ui-row-editor-disabled');" +
            "btnSave.on( 'click', false);" +

            "$('tbody.ui-datatable-data tr:first-child input').keyup(function() {" +

                "var empty = false;" +
                "$('tbody.ui-datatable-data tr:first-child input:not([role=\"combobox\"])').each(function() {" +
                    "if ($(this).val() == '') {" +
                    "empty = true;" +
                    "}" +
                "});" +

                "if (empty) {" +
                    "btnSave.on( 'click', false);" +
                    "btnSave.addClass('ui-row-editor-disabled');" +
                "} else {" +
                    "btnSave.unbind('click');" +
                    "btnSave.removeClass('ui-row-editor-disabled');" +
                "}" +
            "});";
}
