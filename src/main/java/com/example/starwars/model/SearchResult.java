package com.example.starwars.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchResult {

    private String type;       // Type of the object (e.g., Planets, Spaceships, Vehicles, etc.)
    private String name;       // Name of the object (e.g., Sand Crawler, Tatooine, etc.)
    private int count;         // Count of items matching the search criteria (if applicable)
    private List<String> films;  // List of films related to the object

    @Override
    public String toString() {
        return "SearchResult{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count + '\'' +
                ", films=" + films + '\'' +
                '}';
    }

    public SearchResult(String type2, String name2, String string2) {
        this.type=type2;
        this.name=name2;
        this.films=List.of(string2);
    }
}
