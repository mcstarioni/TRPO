package example;

import com.google.gson.Gson;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class CurrencyClient implements Runnable
{
    public static final CurrencyClient instance = new CurrencyClient();
    private final int timeToWait = 10000;
    private String data = "";
    private HttpClient client = null;
    private ArrayList<Session> listeners = new ArrayList<>();
    private CurrencyClient()
    {
        System.out.println("Thread start");
        init();
        new Thread(this).start();
    }
    public void addSession(Session s)
    {
        listeners.add(s);
        sendData(s);
    }
    public void removeSession(Session s)
    {
        listeners.remove(s);
    }
    private void sendData(Session sess)
    {
        try
        {
            System.out.println("Sending data to " + sess.getRemote()
                    .getInetSocketAddress().toString());
            sess.getRemote().sendString(data);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private void notifyListeners()
    {
        listeners.forEach(this::sendData);
    }
    private void init()
    {
        SslContextFactory sslContextFactory = new SslContextFactory();
        client = new HttpClient(sslContextFactory);
        client.setFollowRedirects(false);
        try
        {
            client.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String getCurrency()
    {
        Request req = client.newRequest("https://min-api.cryptocompare.com/data/pricemulti");
        ContentResponse resp = null;
        try
        {
            resp = req
                    .param("fsyms","USD,EUR")
                    .param("tsyms","RUR")
                    .method(HttpMethod.GET).send();
        } catch (Exception e)
        {
            e.printStackTrace();
        };
        return resp.getContentAsString();
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String result = this.getCurrency();
                if(data == null)
                {
                    data = result;
                }
                else
                {
                    if(!result.equals(data))
                    {
                        System.out.println("Data changed");
                        System.out.println(data);
                        System.out.println(result);
                        data = result;
                        notifyListeners();
                    }
                }
                sleep(5000);
            } catch (InterruptedException e)
            {
                break;
            }
        }
    }
}
