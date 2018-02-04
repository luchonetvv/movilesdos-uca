package ni.edu.uca.mytwitterapplication.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase concreta para interceptar la peticion y agregarles informacion extra por ejemplo en este\
 * caso datos a la cabecera acerca del consumer key, token, etc para conectarme al API de Twitter.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final String OAUTH_CONSUMER_KEY_KEY = "oauth_consumer_key";
        final String OAUTH_CONSUMER_KEY_VALUE = "ep9YtX6zSYyrr2KeQzBtjuPvp";
        final String OAUTH_CONSUMER_SECRET_VALUE = "jubXwWazkTYySUZ7LGJuUm0YqDzh1ijcsR9N4NdEFpey4XN84P";
        final String OAUTH_TOKEN_KEY = "oauth_token";
        final String OAUTH_TOKEN_VALUE = "94272347-v8ZDlQiyJ5WsaCi5rPntgAdbBx3jSWuIr2JGnCFVq";
        final String OAUTH_TOKEN_SECRET_VALUE = "Kgxi6WvL82a8R0NULedGu2OUW9V6ZiOFru2D3huUlvmWK";
        final String OAUTH_SIGNATURE_METHOD_KEY = "oauth_signature_method";
        final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
        final String OAUTH_TIMESTAMP_KEY = "oauth_timestamp";
        final String OAUTH_TIMESTAMP_VALUE = getTimestamp();
        final String OAUTH_NONCE_KEY = "oauth_nonce";
        final String OAUTH_NONCE_VALUE = getNonce();
        final String OAUTH_VERSION_KEY = "oauth_version";
        final String OAUTH_VERSION_VALUE = "1.0";
        final String OAUTH_SIGNATURE_KEY = "oauth_signature";

        Request request = chain.request();
        String queryParam = request.url().query();
        String strParams;
        if (queryParam != null) {
            strParams = encode(OAUTH_CONSUMER_KEY_KEY) + "=" + encode(OAUTH_CONSUMER_KEY_VALUE) + "&" +
                    encode(OAUTH_NONCE_KEY) + "=" + encode(OAUTH_NONCE_VALUE) + "&" +
                    encode(OAUTH_SIGNATURE_METHOD_KEY) + "=" + encode(OAUTH_SIGNATURE_METHOD_VALUE) + "&" +
                    encode(OAUTH_TIMESTAMP_KEY) + "=" + encode(OAUTH_TIMESTAMP_VALUE) + "&" +
                    encode(OAUTH_TOKEN_KEY) + "=" + encode(OAUTH_TOKEN_VALUE) + "&" +
                    encode(OAUTH_VERSION_KEY) + "=" + encode(OAUTH_VERSION_VALUE) + "&" +
                    encode(queryParam.split("=")[0]) + "=" + encode(queryParam.split("=")[1]);
        } else {
            strParams = encode(OAUTH_CONSUMER_KEY_KEY) + "=" + encode(OAUTH_CONSUMER_KEY_VALUE) + "&" +
                    encode(OAUTH_NONCE_KEY) + "=" + encode(OAUTH_NONCE_VALUE) + "&" +
                    encode(OAUTH_SIGNATURE_METHOD_KEY) + "=" + encode(OAUTH_SIGNATURE_METHOD_VALUE) + "&" +
                    encode(OAUTH_TIMESTAMP_KEY) + "=" + encode(OAUTH_TIMESTAMP_VALUE) + "&" +
                    encode(OAUTH_TOKEN_KEY) + "=" + encode(OAUTH_TOKEN_VALUE) + "&" +
                    encode(OAUTH_VERSION_KEY) + "=" + encode(OAUTH_VERSION_VALUE);
        }

        String urlBaseResourceParam = request.url().url().getProtocol() + "://" + request.url().url().getHost() +
                request.url().url().getPath();
        String strSignatureBase = encode(request.method().toUpperCase()) + "&" +
                encode(urlBaseResourceParam.trim()) + "&" + encode(strParams);
        String strSignature = generateSignature(
                strSignatureBase,
                encode(OAUTH_CONSUMER_SECRET_VALUE),
                encode(OAUTH_TOKEN_SECRET_VALUE)
        );
        request = request.newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader(
                        "Authorization",
                        "OAuth " + OAUTH_CONSUMER_KEY_KEY + "=" + OAUTH_CONSUMER_KEY_VALUE + "," +
                                OAUTH_NONCE_KEY + "=" + OAUTH_NONCE_VALUE + "," +
                                OAUTH_SIGNATURE_METHOD_KEY + "=" + OAUTH_SIGNATURE_METHOD_VALUE + "," +
                                OAUTH_TIMESTAMP_KEY + "=" + OAUTH_TIMESTAMP_VALUE + "," +
                                OAUTH_TOKEN_KEY + "=" + OAUTH_TOKEN_VALUE + "," +
                                OAUTH_VERSION_KEY + "=" + OAUTH_VERSION_VALUE + "," +
                                OAUTH_SIGNATURE_KEY + "=" + strSignature
                )
                .build();
        return chain.proceed(request);
    }

    private String getTimestamp() {
        final long secondsFromEpoch = System.currentTimeMillis() / 1000;
        return Long.toString(secondsFromEpoch);
    }

    private String getNonce() {
        return String.valueOf(System.nanoTime()) + String.valueOf(Math.abs(new SecureRandom().nextLong()));
    }

    private String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb.append("%2A");
            } else if (focus == '+') {
                sb.append("%20");
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb.append('~');
                i += 2;
            } else {
                sb.append(focus);
            }
        }
        return sb.toString();
    }

    private String generateSignature(String strSignatueBase, String oAuthConsumerSecret, String oAuthTokenSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(strSignatueBase.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode(Base64.encodeToString(byteHMAC, Base64.DEFAULT));
    }
}
