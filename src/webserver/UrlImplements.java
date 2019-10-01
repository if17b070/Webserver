package webserver;

import BIF.SWE1.interfaces.Url;

import java.util.Map;

public class UrlImplements implements Url {

    @Override
    public String getRawUrl() {

        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Map<String, String> getParameter() {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public String[] getSegments() {
        return new String[0];
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public String getFragment() {
        return null;
    }
}
