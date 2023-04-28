package com.example.language_trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class TranslateFragment extends Fragment {

    private EditText inputEditText;
    private TextView outputTextView;
    private Button translateButton;
    private ProgressBar progressBar;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private String inputText;

    private final String API_KEY = "YOUR_API_KEY";
    private final String API_URL = "https://translation.googleapis.com/language/translate/v2?key=" + API_KEY;

    public TranslateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        // Initialize the UI elements
        inputEditText = view.findViewById(R.id.edit_text_source);
        outputTextView = view.findViewById(R.id.edit_text_translation);
        translateButton = view.findViewById(R.id.button_translate);
        progressBar = view.findViewById(R.id.progress_bar);

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(getActivity());

        // Set an onClickListener for the translate button
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input text
                inputText = inputEditText.getText().toString().trim();

                // If the input text is empty, show an error message
                if (inputText.isEmpty()) {
                    inputEditText.setError("Please enter text to translate");
                    return;
                }

                // Set the progress bar to visible
                progressBar.setVisibility(View.VISIBLE);

                // Make a request to the translation API
                stringRequest = new StringRequest(Request.Method.POST, API_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Set the progress bar to invisible
                                progressBar.setVisibility(View.INVISIBLE);

                                try {
                                    // Parse the JSON response
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    JSONArray translationsArray = dataObject.getJSONArray("translations");
                                    JSONObject translationObject = translationsArray.getJSONObject(0);
                                    String translatedText = translationObject.getString("translatedText");

                                    // Set the translated text to the output text view
                                    outputTextView.setText(translatedText);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Set the progress bar to invisible
                                progressBar.setVisibility(View.INVISIBLE);

                                // Show an error message
                                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Set the request parameters
                        Map<String, String> params = new HashMap<>();
                        params.put("q", inputText);
                        params.put("target", "en");
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                requestQueue.add(stringRequest);
            }
        });

        return view;
    }
}