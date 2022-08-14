package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

//Требования к содержимому полей:
//    Город - один из административных центров субъектов РФ
//    Дата - не ранее трёх дней с текущей даты
//    Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
//    Поле телефон - только цифры (11 цифр), символ + (на первом месте)
//    Флажок согласия должен быть выставлен
//    Тестируемая функциональность: отправка формы.

    public String calcDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.timeout=8000;
        Configuration.headless=true;

    }

    @Test
    void shouldOrderCardDeliveryWhenValidCityNameIsEntered() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Станислав");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Алексей");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Владислав");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Александр");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldErrorWhenValuesOfCityWhenCityNotAdministrativeCenter() {
        $x("//*[@data-test-id='city']//input").setValue("Острогожск");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Олег");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("петров ИВАН");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Иванов Фёдор");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
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
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("И");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }


    @Test
    void shouldErrorInTheNameInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("ivanov ivan");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }


    @Test
    void shouldErrorInTheNameWithSymbol() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("иванов?");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldErrorInTheCityIsNotFilled() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Сидоров Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("Гусев Иван");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInTheNameIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneFilledIn10Digits() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("германов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIn12Digits() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("коршунов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+799988899889");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInRussiaLetter() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("васечкин иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998о");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("кондратьев иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998s");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIsNotPlus() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("найденов иван");
        $x("//*[@data-test-id='phone']//input").setValue("79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledWhenPlusInMiddle() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("репин иван");
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
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(7));
        $("[data-test-id='name'] input").setValue("логачев иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='agreement'].input_invalid")
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldNotCardDeliveryWhenDeliveryLessThan3Days() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(calcDate(2));
        $("[data-test-id='name'] input").setValue("Иволгин Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotCardDeliveryWhenDateIsSelectedFromCalendar() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $(".icon-button").click();
        int day = 150;
        int currentMonth = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).substring(3, 5));
        int generateMonth = Integer.parseInt(calcDate(day).substring(3, 5));
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
        $x("//td[text()='" + Integer.parseInt(calcDate(day).substring(0, 2)) + "']").click();
        $("[data-test-id='name'] input").setValue("Аркадьев Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
    }
}
