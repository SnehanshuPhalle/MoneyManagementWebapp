package com.money.money_management.util;

import java.util.*;

public class CategoryUtils {

    private static final Map<String, String> keywordCategoryMap = new HashMap<>();

    static {
        keywordCategoryMap.put("restaurant", "Food");
        keywordCategoryMap.put("lunch", "Food");
        keywordCategoryMap.put("dinner", "Food");
        keywordCategoryMap.put("grocery", "Food");
        keywordCategoryMap.put("rent", "Rent");
        keywordCategoryMap.put("uber", "Transport");
        keywordCategoryMap.put("taxi", "Transport");
        keywordCategoryMap.put("bus", "Transport");
        keywordCategoryMap.put("salary", "Salary");
        keywordCategoryMap.put("freelance", "Freelance");
        keywordCategoryMap.put("bonus", "Bonus");
        keywordCategoryMap.put("electricity", "Utilities");
        keywordCategoryMap.put("internet", "Utilities");
    }

    public static String detectCategory(String description) {
        if (description == null) return "Other";

        String lowerDesc = description.toLowerCase();
        for (Map.Entry<String, String> entry : keywordCategoryMap.entrySet()) {
            if (lowerDesc.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Other";
    }
}
