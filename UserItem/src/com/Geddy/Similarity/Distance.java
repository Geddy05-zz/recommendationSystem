package com.Geddy.Similarity;

import com.Geddy.Models.UserPreference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lord_ on 16-2-2016.
 */
public interface Distance {
    float calculate(UserPreference targetUser ,UserPreference user);
}
