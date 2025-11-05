package com.senimen.controllers;

import com.senimen.models.Event;
import com.senimen.services.AIService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    private final AIService ai;
    public AIController(AIService ai){ this.ai = ai; }

    @GetMapping("/recommendations/{userId}")
    public List<Event> recommend(@PathVariable String userId){ return ai.recommend(userId); }
}
