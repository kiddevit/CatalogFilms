package com.example.catalogfilms;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.adapters.GenreAdapter;
import com.example.catalogfilms.adapters.GenreDetailAdapter;
import com.example.catalogfilms.adapters.MovieAdapter;
import com.example.catalogfilms.models.DetailGenre;
import com.example.catalogfilms.models.Genre;
import com.example.catalogfilms.models.Movie;
import com.example.catalogfilms.utils.IOUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// A task with String input parameter, and returns the result as String.
// AsyncTask<Params, Progress, Result>
public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    private String objectType;
    private Context context;
    private RecyclerView recyclerView;

    public DownloadJsonTask(Context context, RecyclerView recyclerView, String objectType) {
        this.context = context;
        this.objectType = objectType;
        this.recyclerView = recyclerView;
    }

    @Override
    protected String doInBackground(String... params) {
        String textUrl = params[0];

        InputStream in = null;
        BufferedReader br = null;
        try {
            HttpURLConnection httpsConn = null;
            URL url = new URL(textUrl);
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                httpsConn = https;
            } else {
                httpsConn = (HttpURLConnection) url.openConnection();
            }

            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.connect();
            int resCode = httpsConn.getResponseCode();

            if (resCode == HttpsURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
        }
        return null;
    }

    // When the task is completed, this method will be called
    // Download complete. Lets update UI
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            final ObjectMapper mapper = new ObjectMapper();

            try {
                switch (objectType) {
                    case "Genre": {
                        List<Genre> genreList = mapper.readValue(result, new TypeReference<List<Genre>>(){});
                        GenreAdapter adapter = new GenreAdapter(context, genreList);
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                    case "DetailGenre": {
                        List<DetailGenre> detailGenreList = mapper.readValue(result, new TypeReference<List<DetailGenre>>(){});
                        GenreDetailAdapter adapter = new GenreDetailAdapter(context, detailGenreList);
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                    case "Movie": {
                        Movie movie = mapper.readValue(result, Movie.class);
                        List<Movie> movies = new ArrayList<>();
                        movies.add(movie);
                        MovieAdapter adapter = new MovieAdapter(context, movies);
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }

    // always verify the host - don`t check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    // Trust every server - don`t check for any certificate
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
