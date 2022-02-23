package guru.qa;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ParameterizedTests {
    @BeforeEach
    void precondition() {
        Configuration.browserSize = "1920x1080";
        open("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
    }

    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }

    static Stream<Arguments> mixedArgumentsTestDataProvider() {
        return Stream.of(
                Arguments.of("Roman", "TestLast", "123456"),
                Arguments.of("TestFirst","Ivanov", "654321")
        );
    }
    @MethodSource(value = "mixedArgumentsTestDataProvider")
    @ParameterizedTest(name = "Проверка формы добавления клиента")
    void addNewCustomerTest(String firstArg, String secondArg, String postCodeValue) {
        $("button[ng-click='manager()']").click();
        $("button[ng-click='addCust()']").click();

        $("input[ng-model='fName']").setValue(firstArg);
        $("input[ng-model='lName']").setValue(secondArg);
        $("input[ng-model='postCd']").setValue(postCodeValue);
        $(".btn-default").click();
        //Проверка в таблице customers что новый клиент добавился
        $("button[ng-click='showCust()']").click();
        $(".table-bordered").scrollTo().shouldHave(text(firstArg), text(secondArg), text(postCodeValue));
    }

    @ValueSource(strings = {"Harry Potter", "Ron Weasly"})
    @ParameterizedTest(name = "Проверка авторизации под разными клиентами")
    void loginTest(String testData) {
        $("button[ng-click='customer()']").click();

        $("#userSelect").click();
        $(byText(testData)).click();
        $(".btn-default").click();
        //Проверка, что мы авторизовались нужным клиентом
        $(".fontBig").shouldHave(text(testData));


    }
}
