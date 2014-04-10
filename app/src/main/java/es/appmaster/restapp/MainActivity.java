package es.appmaster.restapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import es.appmaster.restapp.adapter.StudentsAdapter;
import es.appmaster.restapp.api.RestClient;
import es.appmaster.restapp.model.Student;


public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private StudentsAdapter adapter;
    private ArrayList<Student> students;
    private Gson serializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        students = new ArrayList<Student>();
        adapter = new StudentsAdapter(this, students);

        listView = (ListView)findViewById(R.id.listview);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);

        listView.setAdapter(animationAdapter);

        serializer = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
    }

    private String callApi() {

        String response = "";

        // 10.10.60.183
        RestClient restClient = new RestClient();
        try {
            response = restClient.get("/students");
        } catch (IOException e) {
            Toast.makeText(this, "IO Exception", Toast.LENGTH_SHORT).show();
        }

        return response;
    }

    private String postToApi() {
        String response = "";

        RestClient restClient = new RestClient();
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", "Carmen"));
            params.add(new BasicNameValuePair("city", "Cartagena"));

            response = restClient.post("/students", params);
        } catch (IOException e) {
            Toast.makeText(this, "IO Exception", Toast.LENGTH_SHORT).show();
        }

        return response;
    }


    // AsyncTask<Parametros, Progreso, Resultado>
    private class HttpTask extends AsyncTask<String, Integer, ArrayList<Student>> {

        @Override
        protected ArrayList<Student> doInBackground(String... params) {
            ArrayList<Student> studentArrayList = new ArrayList<Student>();
            String result = callApi();

            // conversion de string a array de objetos
            try {
                JSONArray jsonArray = new JSONArray(result);

                for ( int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    /*int id = jsonObject.getInt("id");
                    String name = (String) jsonObject.get("name");
                    String city = jsonObject.getString("city");
                    String photo = jsonObject.getString("photo");

                    Student student = new Student(id, name, city, photo);*/

                    Student student = serializer.fromJson(jsonObject.toString(), Student.class);
                    studentArrayList.add(student);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return studentArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Student> result) {
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            students.addAll(result);

            // actualiza el adapter
            adapter.notifyDataSetChanged();
        }

    }


    private class PostTask extends AsyncTask<String, Integer, Student> {

        @Override
        protected Student doInBackground(String... params) {
            String result = postToApi();
            JSONArray jsonArray;
            try {
                //jsonArray = new JSONArray(result);
                JSONObject jsonObject = new JSONObject(result);
                return serializer.fromJson(jsonObject.toString(), Student.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Student result) {
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            students.add(result);

            // actualiza el adapter
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            new HttpTask().execute();

            return true;
        }
        if (id == R.id.action_add) {

            new PostTask().execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
