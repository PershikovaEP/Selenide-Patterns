package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.*;

public class CardDeliveryTest {

//Требования к содержимому полей:
//    Город - один из административных центров субъектов РФ
//    Дата - не ранее трёх дней с текущей даты
//    Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
//    Поле телефон - только цифры (11 цифр), символ + (на первом месте)
//    Флажок согласия должен быть выставлен
//    Тестируемая функциональность: отправка формы.



    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.timeout=8000;
        Configuration.headless=true;

    }

    @Test
    void shouldOrderCardDeliveryWhenValidCityNameIsEntered() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    @Test
    void shouldOrderCardDeliveryWhenValueOfFieldCityAreSelectedAndDateCalculate() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    @Test
    void shouldOrderCardDeliveryWhenValuesOfCityWhenCityWithoutCase() {
        $x("//*[@data-test-id='city']//input").setValue("воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    @Test
    void shouldErrorWhenValuesOfCityInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("voronej");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldErrorWhenValuesOfCityWhenCityNotAdministrativeCenter() {
        $x("//*[@data-test-id='city']//input").setValue("Острогожск");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldOrderCardDeliveryInTheNameWhenWithoutCase() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue("петров ИВАН");
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }


    @Test
    void shouldOrderCardDeliveryInTheNameWihtRussionYo() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Фёдор");
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    // ограничение длины поля фамилия и имя не установлено
    @Test
    void shouldOrderCardDeliveryWhenNameIs1CharLength() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue("И");
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }


    @Test
    void shouldErrorInTheNameInLatin() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue("ivanov ivan");
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }


    @Test
    void shouldErrorInTheNameWithSymbol() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue("иванов?");
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldErrorInTheCityIsNotFilled() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInTheNameIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneFilledIn10Digits() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIn12Digits() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("+799988899889");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInRussiaLetter() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998о");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInLatin() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998s");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIsNotPlus() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledWhenPlusInMiddle() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue("7+9998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorWhenFieldsAllIsNotFilled() {
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldBeVisibleCheckboxClassInputInvalidWhenSendingAnEmptyCheckbox() {
        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='agreement'].input_invalid")
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldNotCardDeliveryWhenDeliveryLessThan3Days() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(DataGenerator.generateDate(7));
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("//*[text()='Заказ на выбранную дату невозможен']")
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotCardDeliveryWhenDateIsSelectedFromCalendar() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $(".icon-button").click();
        int day = 7;
        int currentMonth = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).substring(3, 5));
        int generateMonth = Integer.parseInt(DataGenerator.generateDate(7).substring(3, 5));
        int result = generateMonth - currentMonth;
        if (result == 0) {
            return;
        }
        if (result > 0) {
            for (int i = 0; i < result; i++) {
                $(".calendar__arrow_direction_right.calendar__arrow_double+.calendar__arrow_direction_right").click();
            }
        } else {
            for (int i = 0; i < 12 + result; i++) {
                $(".calendar__arrow_direction_right.calendar__arrow_double+.calendar__arrow_direction_right").click();
            }
        }
        $x("//td[text()='" + Integer.parseInt(DataGenerator.generateDate(7).substring(0, 2)) + "']").click();
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
//        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе

        $x("//*[@data-test-id='city']//input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $x("//*[@data-test-id='phone']//input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(secondMeetingDate);
        $x("[data-test-id='replan-notification'] .button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }
}
