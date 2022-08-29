package ru.vk.competition.minbenchmark.util;

import org.h2.engine.Mode;
import org.h2.value.DataType;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlTypesTest {

    @Test
    void isValid() {
        assertTrue(SqlTypes.isValid("INT"));
    }

    @Test
    void isValidType() {
        JDBCType type = JDBCType.valueOf("enum('&', '.')");
        System.out.println(type);
    }

    @Test
    void seeJDBCTypeValues() {
        for (JDBCType type : JDBCType.values()) {
            System.out.println(type);
        }
    }

    @Test
    void testEnumRegEx() {
        String strRegEx = "ENUM\\s*\\(\\s*'[^']+'(\\s*,?\\s*'[^']+')*?\\s*\\)";
        System.out.println("enum('&')".toUpperCase().matches(strRegEx));
        System.out.println("enum('&', '.')".toUpperCase().matches(strRegEx));
        System.out.println("enum('&', '.', ',')".toUpperCase().matches(strRegEx));
        System.out.println("ENUM('dre''wr')".toUpperCase().matches(strRegEx));
        System.out.println("ENUM ('drewr')".toUpperCase().matches(strRegEx));
    }

    @Test
    void testH2GetTypeByName() throws SQLException {
        Mode mode = Mode.getRegular();
        DataType typeByName = DataType.getTypeByName("int4", mode);
        System.out.println(typeByName);
    }
}