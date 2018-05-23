package lv.roma.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestService {

    public static final String EMPTY_JSON_OBJECT = "{}";
    private ObjectMapper jacksonMapper = new ObjectMapper();

    private final String storageService;

    @Inject
    public RestService(@Named("storage.service") String storageService) {
        this.storageService = storageService;
    }

    public Map<String, List<PurchaseItem>> get(String token) {
        try {
            final URL url = getUrl(token);

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            final String output = getResponse(conn);
            conn.disconnect();
            if (EMPTY_JSON_OBJECT.equals(output)) {
                return new HashMap<>();
            }
            return jacksonMapper.readValue(output, new TypeReference<Map<String, List<PurchaseItem>>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void post(Map<String, List<PurchaseItem>> purchases, String token) {
        try {
            final URL url = getUrl(token);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            final String input = getInputJson(purchases);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK && conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            conn.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getInputJson(Map<String, List<PurchaseItem>> purchases) throws JsonProcessingException {
        if (purchases.isEmpty()) {
            return EMPTY_JSON_OBJECT;
        }
        return jacksonMapper.writeValueAsString(purchases);
    }

    private URL getUrl(String token) throws MalformedURLException, UnsupportedEncodingException {
        return new URL(storageService + URLEncoder.encode(token, "UTF-8"));
    }

    private String getResponse(HttpURLConnection conn) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        final StringBuilder stringBuilder = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            stringBuilder.append(output);
        }
        return stringBuilder.toString();
    }

    public Map<String, List<PurchaseItem>> mockData() {
        final Map<String, List<PurchaseItem>> purchases = new HashMap<>();

        final String[] owners = new String[] {"Mother's", "Wife's", "My own list"};
        final String[] products = new String[] {"Beer", "Bread", "Milk"};
        final int[] quantity = new int[] {5, 2, 3};

        for (int i = 0; i < 3;  i++) {
            ArrayList<PurchaseItem> purchaseItems = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                final PurchaseItem purchaseItem = new PurchaseItem(products[y], quantity[y]);
                purchaseItems.add(purchaseItem);
            }
            purchases.put(owners[i], purchaseItems);
        }

        return purchases;
    }
}
