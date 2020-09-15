package ru.sberbank.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/demo1")
public class Opdemo1Controller {
    @Autowired
    ItemsComparisonService service;

    @GetMapping("/compare")
    public String getComparison(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("first") LocalDate first,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("second") LocalDate second) {
        try {
            List<Comparison> comparisons = service.compareForDate(first, second);
            return "We done";
        } catch (Exception e) {
            throw e;
        }
    }
}

