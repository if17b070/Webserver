package webserver;

import BIF.SWE1.interfaces.Url;

import java.util.HashMap;
import java.util.Map;


public class UrlImplements implements Url {

    protected String fullPath;
    protected Map<String,String> parametersMap ;
    protected String parameter = null;
    protected String value = null;
    protected String rest;
    protected int parameterCount=0;
    public UrlImplements(String path){

           this.fullPath=path;


        parametersMap = new HashMap<String, String>();

        //Splitting the URL parameters from each other and the rest of the path.
        if(this.fullPath!=null){
            if(this.fullPath.contains("?")){
                rest = this.fullPath.substring(this.fullPath.indexOf("?")+1);
                while(rest.contains("&")|| !rest.isEmpty() ){
                    parameter=rest.substring(0,rest.indexOf("="));
                    if(rest.contains("&")){
                        value = rest.substring(rest.indexOf("=")+1, rest.indexOf("&"));
                    }else{
                        value = rest.substring(rest.indexOf("=")+1);
                    }

                    parametersMap.put(parameter,value);
                    parameterCount++;

                    if(rest.contains("&")){
                        rest = rest.substring(rest.indexOf("&")+1);
                    }else {
                        break;
                    }



                }
            }
        }



    }

    @Override
    public String getRawUrl() {

        return this.fullPath;
    }

    @Override
    public String getPath() {
        String path=null;
        if(this.fullPath!=null){
            if(this.fullPath.contains("?")){
                path = this.fullPath.substring(0,this.fullPath.indexOf("?"));
            }else{
                path = fullPath;
            }
        }


        return path;
    }

    @Override
    public Map<String, String> getParameter() {

        return this.parametersMap;
    }

    @Override
    public int getParameterCount() {
        return this.parameterCount;
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
