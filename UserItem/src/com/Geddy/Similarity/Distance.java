package com.Geddy.Similarity;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.List;


public interface Distance {
    float calculate(UserPreference targetUser ,UserPreference user);
}
