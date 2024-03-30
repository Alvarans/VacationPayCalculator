package com.example.vacationpaycalculator.controller;

import com.example.vacationpaycalculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorService calculatorService;

    /**
     * controller on HTTP "/calculacte", which take request and send data to calculating service
     *
     * @param salary    - average salary for 12 months
     * @param days      - vacation days amount
     * @param startDate - day on which vacation begins
     * @return - amount of payment | amount of payment considering the holidays (requered startDate)
     */
    @GetMapping("/calculacte")
    double takeVacationPayAmount(@RequestParam("salary") int salary,
                                 @RequestParam("days") short days,
                                 @RequestParam("startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> startDate) {
        return startDate.isEmpty() ? calculatorService.vacationPayCalculate(salary, days)
                : calculatorService.vacationPayCalculateWithWeekends(salary, days, startDate.orElse(null));
    }
}
