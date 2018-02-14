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

import com.exo.registrator.domain.User;
import com.exo.registrator.service.IRegistrationDbEao;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * Created by atanasko on 11/21/17.
 */
@ManagedBean(name = "chartView")
@ViewScoped
public class ChartView implements IRegistratorView {

    private static final Long MILLIS_PER_DAY = 86400000L;

    private LineChartModel percentPerDayModel;

    private BarChartModel percentPerMonthModel;

    @EJB
    private IRegistrationDbEao registrationDbEao;

    /**
     * Create line chart model for percent of users that login during the day of month.
     *
     * @return Chart model.
     */
    private LineChartModel createPercentPerDayModel() {

        Axis yAxis;
        DateAxis xAxis;

        LocalDate now = LocalDate.now();
        LocalDate startDate;
        Long startDateMilis;
        LocalDate endDate;
        Long endDateMilis;

        int userCount;

        LineChartModel percentPerDayModel = new LineChartModel();
        LineChartSeries percentPerDaySeries = new LineChartSeries();

        startDate = now.withDayOfMonth(1);
        startDateMilis = startDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        endDate = now.withDayOfMonth(now.lengthOfMonth());
        endDateMilis = endDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        percentPerDayModel.setTitle("Percent per Day");
        percentPerDayModel.setAnimate(true);
        percentPerDayModel.setLegendPosition("se");

        percentPerDayModel.setSeriesColors(Integer.toHexString(Color.RED.getRGB()).toUpperCase().substring(2));
        percentPerDayModel.addSeries(percentPerDaySeries);

        yAxis = percentPerDayModel.getAxis(AxisType.Y);
        yAxis.setLabel("Percent (%)");
        yAxis.setMin(0);
        yAxis.setMax(100);
        yAxis.setTickInterval("10");
        yAxis.setTickFormat("%d %");

        xAxis = new DateAxis();
        xAxis.setLabel("Date");
        xAxis.setTickFormat("%e/%b/%Y");
        xAxis.setTickAngle(-30);
        xAxis.setMin(startDateMilis);
        xAxis.setMax(endDateMilis);
        percentPerDayModel.getAxes().put(AxisType.X, xAxis);

        percentPerDaySeries.setLabel("percent per day");

        userCount = registrationDbEao.getUserList().size();

        for (long dateMillis = startDateMilis; dateMillis <= endDateMilis; dateMillis += MILLIS_PER_DAY) {
            LocalDate date;
            List<User> userPerDayList;
            int userCountPerDay;
            float percent;

            date = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            userPerDayList = registrationDbEao.getLoggedUserPerDayList(date);
            userCountPerDay = userPerDayList.size();
            percent = ((float) userCountPerDay / (float) userCount) * 100;
            percentPerDaySeries.set(dateMillis, percent);
        }

        return percentPerDayModel;
    }

    /**
     * Create bar chart model for percent of users that login during the month of year.
     *
     * @return
     */
    private BarChartModel createPercentPerMonthModel() {

        Axis yAxis;
        Axis xAxis;

        LocalDate now = LocalDate.now();

        BarChartModel percentPerMonthModel = new BarChartModel();
        ChartSeries percentPerMonthSeries = new ChartSeries();

        percentPerMonthModel.setSeriesColors(Integer.toHexString(Color.RED.getRGB()).toUpperCase().substring(2));
        percentPerMonthModel.setTitle("Percent per Month");
        percentPerMonthModel.setAnimate(true);
        percentPerMonthModel.setLegendPosition("se");

        percentPerMonthSeries.setLabel("percent per month");

        percentPerMonthModel.addSeries(percentPerMonthSeries);

        yAxis = percentPerMonthModel.getAxis(AxisType.Y);
        yAxis.setLabel("Percent (%)");
        yAxis.setMin(0);
        yAxis.setMax(100);
        yAxis.setTickInterval("10");
        yAxis.setTickFormat("%d %");

        xAxis = percentPerMonthModel.getAxis(AxisType.X);
        xAxis.setLabel("Month");
        xAxis.setTickAngle(-30);

        for (LocalDate date = now.withMonth(1); date.isBefore(now) || date.isEqual(now); date = date.plusMonths(1)) {
            List<User> userPerMonthList;
            int userCount;
            int userCountPerMonth;
            float percent;

            userCount = registrationDbEao.getUserList(date.getMonthValue()).size();
            userPerMonthList = registrationDbEao.getLoggedUserPerMonthList(date.getMonthValue());
            userCountPerMonth = userPerMonthList.size();
            percent = ((float) userCountPerMonth / (float) userCount) * 100;
            percentPerMonthSeries.set(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.US), percent);
        }

        return percentPerMonthModel;
    }



    @PostConstruct
    public void init() {
        this.percentPerDayModel = createPercentPerDayModel();
        this.percentPerMonthModel = createPercentPerMonthModel();
    }

    public LineChartModel getPercentPerDayModel() {
        return percentPerDayModel;
    }

    public BarChartModel getPercentPerMonthModel() {
        return percentPerMonthModel;
    }

}
