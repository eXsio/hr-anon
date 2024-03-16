package com.exsio.hranon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class HumanReadableAnonymizerTest {


    @Test
    public void testStringAnonymization() {
        testStringAnonymize(
                "<p class=\"test\">Test string to anonymize with a very long word \"Pneumonoultramicroscopicsilicovolcanoconiosis\", a phone number +48 555-555-555 and an email some@email.com.</p><a href=\"abc\"",
                true,
                "<p class=\"test\">",
                "</p><a href=\"abc\""
        );
        testStringAnonymize(
                "<span style=\"color:rgba(0,0,0,0.87);font-size:14px;\">OCE/DN/6133 - Ocean a  <br /></span><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\">Install of split system and Supply/install if drip tray</div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><br /><br />",
                true,
                "<span style=\"color:rgba(0,0,0,0.87);font-size:14px;\">",
                "</div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><br /><br />"
        );

        testStringAnonymize(
                "<p class=\"test\"><a href=\"test.com\">p class test a href</a></p>",
                true,
                "<p class=\"test\"><a href=\"test.com\">",
                "</a></p>"
        );

        testStringAnonymize(
                "<p \n" +
                        "class=\"test\">\n" +
                        "\t<a \n" +
                        "\t\thref=\n" +
                        "\t\t\t\"test.com\">p class test a href\n" +
                        "\t</a>\n" +
                        "</p>",
                true,
                "<p \n" +
                        "class=\"test\">\n" +
                        "\t<a \n" +
                        "\t\thref=\n" +
                        "\t\t\t\"test.com\">",
                "\n" +
                        "\t</a>\n" +
                        "</p>"
        );

        testStringAnonymize(
                "<p class=\"test\"><a href=\"test.com\">p class test a href</a></p>",
                false,
                "<p class=\"test\"><a href=\"test.com\">",
                "</a></p>"
        );
    }

    @Test
    public void testNumberAnonymization() {
        testNumberAnonymization(222, false);
        testNumberAnonymization(222L, false);
        testNumberAnonymization(222f, true);
        testNumberAnonymization(222.3, true);
        testNumberAnonymization(222.3d, true);
        testNumberAnonymization(222.345, true);
        testNumberAnonymization(new BigDecimal("222.345"), true);
        testNumberAnonymization(new BigInteger("222"), false);
    }

    public <T extends Number> void testNumberAnonymization(T source, boolean isFloatingPoint) {
        var result =  HumanReadableAnonymizer.anonymize(source);
        System.out.println(source);
        System.out.println(result);
        System.out.println();
        Assertions.assertNotEquals(source, result);
        Assertions.assertEquals(source.toString().length(), result.toString().length());
        Assertions.assertEquals(source.getClass(), result.getClass());
        if(isFloatingPoint) {
            Assertions.assertTrue(result.toString().contains("."));
        } else {
            Assertions.assertFalse(result.toString().contains("."));
        }
    }


    private void testStringAnonymize(String source, boolean isHtmlOrXml, String start, String end) {
        var result = HumanReadableAnonymizer.anonymize(source, isHtmlOrXml);
        System.out.println(source);
        System.out.println(result);
        System.out.println();
        Assertions.assertNotEquals(result, source);
        if(isHtmlOrXml) {
            Assertions.assertTrue(result.startsWith(start));
            Assertions.assertTrue(result.endsWith(end));
        } else {
            Assertions.assertFalse(result.startsWith(start));
            Assertions.assertFalse(result.endsWith(end));
        }

    }
}
