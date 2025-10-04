#!/bin/bash

# Add Favorites composable route after Settings
awk '/composable\(Screen.Settings.route\) {/{print;print "";print "        composable(Screen.Favorites.route) {";print "            FavoritesScreen(";print "                onNavigateToMatchDetail = { matchId ->";print "                    navController.navigate(Screen.MatchDetail.createRoute(matchId))";print "                }";print "            )";print "        }";next}1' Score24SevenNavigation.kt > temp1 && mv temp1 Score24SevenNavigation.kt

# Add Favorites to Screen sealed class
awk '/object Settings : Screen\("settings"\)/{print "    object Favorites : Screen(\"favorites\")";print}1' Score24SevenNavigation.kt > temp2 && mv temp2 Score24SevenNavigation.kt

# Replace Settings with Favorites in bottom navigation
awk '/route = Screen.Settings.route/{sub("Screen.Settings.route", "Screen.Favorites.route")}1' Score24SevenNavigation.kt | \
awk '/title = "Settings"/{sub("\"Settings\"", "\"Favorites\"")}1' > temp3 && mv temp3 Score24SevenNavigation.kt

echo "Navigation updated successfully"
