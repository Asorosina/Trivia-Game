package com.angela.trivia.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.angela.trivia.controller.AppController;
import com.angela.trivia.format.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionsArrayList = new ArrayList <>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int loop = 0; loop < response.length(); loop++) {
                try {
                    Question question = new Question(response.getJSONArray(loop).get(0).toString(), response.getJSONArray(loop).getBoolean(1));

                    questionsArrayList.add(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (null != callBack) callBack.processFinished(questionsArrayList);

        }, error -> {

        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest); // to get instance of singleton

    return questionsArrayList;
    }
}
