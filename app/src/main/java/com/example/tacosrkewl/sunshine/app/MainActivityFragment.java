package com.example.tacosrkewl.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 78/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHER STATION - 68/51",
                "Sun - Sunny - 80/68"
        };

        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<>(
                // The current context (fragment's parent activity)
                getActivity(),
                // ID of list item layout
                R.layout.list_item_forecast,
                // ID of textview to populate
                R.id.list_item_forecast_textview,
                // Data
                weekForecast);

        // Get a reference to the ListView, and attach the adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        // Declare to close inside finally block
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Contain raw JSON response as string
        String forecastJsonStr = null;

        try {
            // Construct URL for OpenWeatherMap query
            // Find URL at http://openweathermap.org/API#forecast
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=95758&mode=json&units=metric&cnt=7");

            // Connect to OpenWeatherMap and open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read input stream into String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Newline not necessary since JSON
                // Makes debugging easier
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty, no parsing
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If code did not successfully get data, do not parse
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return rootView;
    }
}
