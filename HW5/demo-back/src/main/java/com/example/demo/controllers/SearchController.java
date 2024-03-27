package com.example.demo.controllers;

import com.example.demo.models.BaseResponse;
import com.example.demo.utils.VectorSearch;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public String[] showStatus(@RequestParam(name = "value") String param) throws IOException {
        return VectorSearch.search(param);
    }
}
