package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        String date = LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generateCity(String locale) {
        // генерацию можно выполнить с помощью Faker, либо используя массив валидных городов и класс Random
        List<String> cityes = Arrays.asList("Воронеж", "Москва", "Пермь", "Санкт-Петербург", "Саратов", "Белгород", "Краснодар", "Пенза", "Владимир", "Рязань", "Челябинск", "Вологда");
        Random r = new Random();
        String city = cityes.get(r.nextInt( (11)+1));
        return city;
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale("ru"));
        String name = faker.name().fullName();
        return name;
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale("ru"));
        String phone = faker.numerify("[+7]##########");
        return phone;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            UserInfo user = new UserInfo(generateCity("ru"), generateName("ru"), generatePhone("ru"));
            return user;
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
