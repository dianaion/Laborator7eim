package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);
        String content;

        // TODO exercise 4
        try {
            // signal missing values through error messages

            // create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();
            // get method used for sending request from methodsSpinner
            if (method == Constants.GET_OPERATION) {

                // 1. GET
                // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
                // b) create an instance of a ResultHandler object
                // c) execute the request, thus generating the result
                String get_addr = Constants.GET_WEB_SERVICE_ADDRESS + "?" + "operator1=" + operator1 +
                        "&operator2=" + operator2 + "&operation=" + operation;
                HttpGet httpGet = new HttpGet(get_addr);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                content = httpClient.execute(httpGet, responseHandler);
            }
            else {
                // 2. POST
                // a) build the URL into a HttpPost object
                // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
                // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
                // d) create an instance of a ResultHandler object
                // e) execute the request, thus generating the result
                HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("operator1", operator1));
                param.add(new BasicNameValuePair("operator2", operator2));
                param.add(new BasicNameValuePair("operation", operation));
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param, HTTP.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                content = httpClient.execute(httpPost, responseHandler);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return content;
    }

    @Override
    protected void onPostExecute(String result) {
        // display the result in resultTextView
        if (result != null)
            resultTextView.setText(result);
        else
            resultTextView.setText("error");
    }

}
