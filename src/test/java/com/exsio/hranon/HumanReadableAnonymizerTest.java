package com.exsio.hranon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HumanReadableAnonymizerTest {


    @Test
    public void str() {
        testStringAnonymize(
                "<p class=\"test\">Test string to anonymize with a very long word \"Pneumonoultramicroscopicsilicovolcanoconiosis\", a phone number +48 555-555-555 and an email some@email.com.</p><a href=\"abc\"",
                "<p class=\"test\">",
                "</p><a href=\"abc\""
        );
        testStringAnonymize(
                "<span style=\"color:rgba(0,0,0,0.87);font-size:14px;\">OCE/DN/6133 - Ocean a  <br /></span><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\">Install of split system and Supply/install if drip tray</div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><br /><br />",
                "<span style=\"color:rgba(0,0,0,0.87);font-size:14px;\">",
                "</div><div style=\"color:rgba(0,0,0,0.87);font-size:14px;\"><br /></div><br /><br />"
        );
    }

    private void testStringAnonymize(String source, String start, String end) {
        var result = HumanReadableAnonymizer.anonymize(source);
        System.out.println(source);
        System.out.println(result);
        System.out.println();
        Assertions.assertNotEquals(result, source);
        Assertions.assertTrue(result.startsWith(start));
        Assertions.assertTrue(result.endsWith(end));
    }
}
