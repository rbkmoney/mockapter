package com.rbkmoney.mockapter.service;

import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.Rule;
import com.rbkmoney.mockapter.model.response.Response;
import com.rbkmoney.mockapter.model.response.delay.Delay;
import com.rbkmoney.mockapter.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestStubService {

    private AtomicReference<List<Rule>> rulesReference = new AtomicReference<>(Collections.emptyList());

    public ExitStateModel processRequest(EntryStateModel entryStateModel) {
        log.info("Request: entryStateModel='{}'", entryStateModel);
        Rule rule = getRule(entryStateModel);
        log.info("Found rule for request, rule='{}', entryStateModel='{}'", rule, entryStateModel);
        Response response = rule.getResponse();
        if (response.hasDelay()) {
            delay(response.getDelay());
        }
        ExitStateModel exitStateModel = response.getResult().buildResult(entryStateModel);
        log.info("Response: exitStateModel='{}', entryStateModel='{}'", exitStateModel, entryStateModel);
        return exitStateModel;
    }

    public void updateRules(List<Rule> newRules) {
        List<Rule> oldRules = rulesReference.getAndSet(newRules);
        log.info("Rules have been updated, newRules='{}', oldRules='{}'", newRules, oldRules);
    }

    private Rule getRule(EntryStateModel entryStateModel) {
        List<Rule> filteredRules = rulesReference.get()
                .stream()
                .filter(rule -> rule.getRequest().isHandle(entryStateModel))
                .collect(Collectors.toList());

        double maxWeight = getMaxWeight(filteredRules);
        return RandomUtil.getWeightedRandom(
                filteredRules.stream()
                        .map(rule -> rule.hasWeight() ? rule : new Rule(rule.getRequest(), rule.getResponse(), maxWeight))
                        .collect(
                                Collectors.groupingBy(
                                        Function.identity(),
                                        Collectors.summingDouble(Rule::getWeight)
                                )
                        ),
                ThreadLocalRandom.current()
        );
    }

    private double getMaxWeight(List<Rule> rules) {
        return rules.stream()
                .filter(rule -> rule.hasWeight())
                .mapToDouble(rule -> rule.getWeight())
                .max()
                .orElse(1.0);
    }

    private void delay(Delay delay) {
        try {
            long timeout = delay.nextTimeout();
            log.info("Go in timeout, {} ms", timeout);
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
