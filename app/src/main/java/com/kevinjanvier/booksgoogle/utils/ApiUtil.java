package com.kevinjanvier.booksgoogle.utils;

import android.net.Uri;
import android.util.Log;

import com.kevinjanvier.booksgoogle.data.entities.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {

    private ApiUtil() {
    }

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes/";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyDg2_GIY6s6RY8fSAIz-amhX9axNPNp5ao";

    public static final String TITLE ="intitle";
    public static final String AUTHOR ="inauthor";
    public static final String PUBLISHER ="inpublisher";
    public static final String ISBN ="isbn";

    public static URL buildUrl(String title) {
//        String fullURL = BASE_API_URL + "?q=" + title;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        URL url = null;
        try {
//            url = new URL(fullURL);
            url = new URL(uri.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURL (String title, String author, String publisher, String isbn){
        URL url = null;
        StringBuilder sb = new StringBuilder();
        if (!title.isEmpty()) sb.append(TITLE + title + "+");
        if (!title.isEmpty()) sb.append(AUTHOR + author + "+");
        if (!title.isEmpty()) sb.append(PUBLISHER + publisher + "+");
        if (!title.isEmpty()) sb.append(ISBN + isbn + "+");
        sb.setLength(sb.length() -1);
        String query = sb.toString();
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());

        }catch (Exception e){
            Log.d("Error ", e.getMessage());
        }
        return url;

    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        } finally {
            connection.disconnect();
        }


    }

    public static ArrayList<Book> getBooksFromJson(String json) {

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHER_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGELINKS = "imageLinks";
        final String THUMBNAILS = "thumbnail";
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ITEMS);
            int numberOfBook = jsonArray.length();
            for (int i = 0; i < numberOfBook; i++) {
                JSONObject bookJson = jsonArray.getJSONObject(i);
                JSONObject volumeInfoJSON = bookJson.getJSONObject(VOLUMEINFO);
                JSONObject imageLinksJson = null;
                if (volumeInfoJSON.has(IMAGELINKS)){
                    volumeInfoJSON.getJSONObject(IMAGELINKS);

                }

                int authNumber;
                try {
                     authNumber = volumeInfoJSON.getJSONArray(AUTHORS).length();
                }catch (Exception e){
                    authNumber =0;
                }

                String[] authors = new String[authNumber];
                for (int j = 0; j < authNumber; j++) {
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(bookJson.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE) ? "" : volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJSON.isNull(PUBLISHER) ? "" : volumeInfoJSON.getString(PUBLISHER)),
                        (volumeInfoJSON.isNull(PUBLISHER_DATE) ? "" : volumeInfoJSON.getString(PUBLISHER_DATE)),
                        (volumeInfoJSON.isNull(DESCRIPTION) ? "":volumeInfoJSON.getString(DESCRIPTION)),
                        (imageLinksJson ==null) ? "" : imageLinksJson.getString(THUMBNAILS));
                books.add(book);


            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());

        }
        return books;
    }


}
