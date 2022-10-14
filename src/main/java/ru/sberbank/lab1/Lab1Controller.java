package ru.sberbank.lab1;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.emptyList;

@RestController
@RequestMapping("/lab1")
public class Lab1Controller {

    private static final String URL = "http://export.rbc.ru/free/selt.0/free.fcgi?period=DAILY&tickers=USD000000TOD&separator=TAB&data_format=BROWSER";

    Long oneDayInSec = 24 * 60 * 60L;

    String obligatoryForecastStartInLA = "https://api.darksky.net/forecast/7680422f32effb534f62f1283f0c38be/34.053044,-118.243750,";
    String exclude = "?exclude=daily";

    String info;
    Long date;
    Double temp;
    int days;

    RestTemplate restTemplate = new RestTemplate();

    List<Double> temps = new ArrayList<>();

    @GetMapping("/quotes")
    public List<Quote> quotes(@RequestParam("days") int days) throws ExecutionException, InterruptedException, ParseException {
        AsyncHttpClient client = AsyncHttpClientFactory.create(new AsyncHttpClientFactory.AsyncHttpClientConfig());
        Response response = client.prepareGet(URL + "&lastdays=" + days).execute().get();

        String body = response.getResponseBody();
        String[] lines = body.split("\n");

        List<Quote> quotes = new ArrayList<>();

        Map<String, Double> maxMap = new HashMap<>();

        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i].split("\t");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(line[1]);
            String year = line[1].split("-")[0];
            String month = line[1].split("-")[1];
            String monthYear = year + month;
            Double high = Double.parseDouble(line[3]);

            Double maxYear = maxMap.get(year);
            if (maxYear == null || maxYear < high) {
                maxMap.put(year, high);
                if (maxYear != null) {
                    List<Quote> newQuotes = new ArrayList<>();
                    for (Quote oldQuote : quotes) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(oldQuote.getDate());
                        int oldYear = cal.get(Calendar.YEAR);
                        if (oldYear == Integer.parseInt(year)) {
                            if (oldQuote.getMaxInYear() < high) {
                                Quote newQuote = oldQuote.setMaxInYear(high);
                                newQuotes.add(newQuote);
                            } else {
                                newQuotes.add(oldQuote);
                            }
                        }
                    }
                    quotes.clear();
                    quotes.addAll(newQuotes);
                }
            }

            Double maxMonth = maxMap.get(monthYear);
            if (maxMonth == null || maxMonth < high) {
                maxMap.put(monthYear, high);
                if (maxMonth != null) {
                    List<Quote> newQuotes = new ArrayList<>();
                    for (Quote oldQuote : quotes) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(oldQuote.getDate());
                        int oldYear = cal.get(Calendar.YEAR);
                        int oldMonth = cal.get(Calendar.MONTH);
                        if (oldYear == Integer.parseInt(year) && oldMonth == Integer.parseInt(month)) {
                            if (oldQuote.getMaxInMonth() < high) {
                                Quote newQuote = oldQuote.setMaxInMonth(high);
                                quotes.remove(oldQuote);
                                quotes.add(newQuote);
                            }
                        }
                    }
                }
            }

            Quote quote = new Quote(line[0],
                    new SimpleDateFormat("yyyy-MM-dd").parse(line[1]),
                    Double.parseDouble(line[2]),
                    Double.parseDouble(line[3]),
                    Double.parseDouble(line[4]),
                    Double.parseDouble(line[5]),
                    Long.parseLong(line[6]),
                    Double.parseDouble(line[7]));
            quote = quote.setMaxInMonth(maxMap.get(monthYear));
            quote = quote.setMaxInYear(maxMap.get(year));

            quotes.add(quote);
        }
        return quotes;
    }

    @GetMapping("/weather")
    public List<Double> getWeatherForPeriod(Integer days) {
        try {
            this.days = days;
            getTemperatureForLastDays();
            return temps;
        } catch (JSONException e) {
        }

        return emptyList();
    }

    // Заменено умножение на сложение, функции заинлайнены, вынесено создание переменных
    // Убраны лишние переменные, oneDayInSec создается и вычисляется один раз
    // Сократила цикл на одну итерацию (на маленьких значениях будет иметь значение)
    // Убрана передача аргумента функции
    public void getTemperatureForLastDays() throws JSONException {
        if (!temps.isEmpty()) {
            temps.clear();
        }

        Long daysInSec = 0L;

        date = Calendar.getInstance().getTimeInMillis() / 1000;
        getTemperatureFromInfo();
        temps.add(temp);

        for (int i = 1; i < days; i++) {
            daysInSec += oneDayInSec;
            date = Calendar.getInstance().getTimeInMillis() / 1000 - daysInSec;
            getTemperatureFromInfo();
            temps.add(temp);
        }

    }

    // инлайн функций; объекты вынесены в поля класса, чтобы не создавались каждый раз
    // строки заранее объединены, убран параметр из вызова функции
    // убраны функции, чтобы не было лишних вызовов
    public void getTemperatureFromInfo() throws JSONException {
        info = restTemplate.getForEntity(obligatoryForecastStartInLA + date.toString() + exclude, String.class).getBody();
        temp = new JSONObject(new JSONObject(new JSONObject(info).getString("hourly")).getJSONArray("data").get(0).toString()).getDouble("temperature");
    }
}

