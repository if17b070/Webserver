package uebungen;

import BIF.SWE1.interfaces.Url;
import webserver.UrlImplements;

public class UEB1 {

	public Url getUrl(String path) {
		return new UrlImplements(path);
	}

	public void helloWorld() {
	}
}
