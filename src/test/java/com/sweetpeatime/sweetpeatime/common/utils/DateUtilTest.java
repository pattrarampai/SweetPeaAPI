package com.sweetpeatime.sweetpeatime.common.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void getDate() {
        var date = DateUtil.getDate(LocalDate.now());
        System.out.println(date);
    }
}