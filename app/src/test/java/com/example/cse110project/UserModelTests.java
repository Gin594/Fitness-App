package com.example.cse110project;

        import org.junit.Test;

        import static org.junit.Assert.*;

        import com.example.cse110project.Models.User;
        import com.example.cse110project.Models.RouteForm;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserModelTests {

    @Test
    public void ctorInitTest(){
        int testUserId =  1 + (int)(Math.random() * (50 - 1));
        int testHeight =  4 + (int)(Math.random() * (7 - 1));
        User toTest = new User(testUserId, testHeight);

        assertEquals(toTest.getHeightInches(), testHeight);
        assertEquals(toTest.getUserID(), testUserId);
        assertEquals(toTest.getRoutes().size(), 0);

    }

    @Test
    public void ctorEmptyTest(){
        User toTest = new User();
        assertEquals(toTest.getUserID(), 0);
        assertEquals(toTest.getHeightInches(), 0);
        assertEquals(toTest.getRoutes().size(), 0);
    }

    @Test
    public void addRouteToUserTest(){
        User toTest = new User();

        int newRouteAdd = 1 + (int)(Math.random() * (50 - 1));
        for(int i = 0;  i < newRouteAdd; i++) {
            toTest.addRouteToUser(new RouteForm("notes test" + i, false,
                    "title test" + i, "start test" + i));
        }
        assertEquals(toTest.getRoutes().size(), newRouteAdd);
    }

    @Test
    public void getRouteByNameTest(){
        User toTest = new User();
        RouteForm expected = new RouteForm("notes test", false,
                "title test", "start test");
        toTest.addRouteToUser(expected);

        assertEquals(toTest.getRouteByName("title test"),expected);
    }
}