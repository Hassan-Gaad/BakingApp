package com.example.baking;


import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.baking.models.Cake;
import com.example.baking.models.Ingredients;
import com.example.baking.models.Steps;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)

public class DetailActivityIntentTest {
    @Rule
    public final ActivityTestRule<DetailActivity> detailActivityActivityTestRule=new ActivityTestRule<DetailActivity>(DetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Ingredients tempIngredient = new Ingredients("2", "CUP", "Graham Cracker crumbs");
            ArrayList<Ingredients> tempIngredientsList = new ArrayList<>();
            tempIngredientsList.add(tempIngredient);

            Steps tempSteps = new Steps(0, "Recipe Introduction", "Recipe Introduction", "", "");
            ArrayList<Steps> tempStepList = new ArrayList<>();
            tempStepList.add(tempSteps);

            Cake tempCake = new Cake("Nutella Pie", 0, tempIngredientsList, tempStepList);


            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Cake", tempCake);
            return intent;
        }
    };

    @Test
    public void DetailsActivityLaunchIntentTest() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_ingredients)).check(ViewAssertions.matches(ViewMatchers.withText("Graham Cracker crumbs")));
        Espresso.onView(ViewMatchers.withId(R.id.tv_step_short_discribtion)).check(ViewAssertions.matches(ViewMatchers.withText("Recipe Introduction")));

        Espresso.onView(ViewMatchers.withId(R.id.tv_ingredients_measure)).check(ViewAssertions.matches(ViewMatchers.withText("CUP")));
        Espresso.onView(ViewMatchers.withId(R.id.tv_ingredients_quantity)).check(ViewAssertions.matches(ViewMatchers.withText("2")));

    }
}
