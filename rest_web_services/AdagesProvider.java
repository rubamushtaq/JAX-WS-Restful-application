package rest_web_services;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import org.xml.sax.InputSource;

@WebServiceProvider
@ServiceMode(javax.xml.ws.Service.Mode.MESSAGE) // entire message available
@BindingType(HTTPBinding.HTTP_BINDING) // versus SOAP binding
public class AdagesProvider implements Provider<Source> {

	@Resource
	protected WebServiceContext wctx;

	@Override
	public Source invoke(Source request) {
		if (wctx == null)
			throw new RuntimeException("Injection failed on wctx.");
		// Grab the message context and extract the request verb.
		MessageContext mctx = wctx.getMessageContext();
		String httpVerb = (String) mctx.get(MessageContext.HTTP_REQUEST_METHOD);
		httpVerb = httpVerb.trim().toUpperCase();
		// Dispatch on verb to the handler method. POST and PUT have non-null
		// requests so only these two get the Source request.
		if (httpVerb.equals("GET"))
			return doGet(mctx);
		return null;
	}

	private Source doGet(MessageContext mctx) {
		// Parse the query string. 
		String qs = (String) mctx.get(MessageContext.QUERY_STRING); // Get all Adages.
		if (qs == null)
			return adages2Xml(); // Get a specified Adage.
		else {
			int id = getId(qs);
			if (id < 0)
				throw new HTTPException(400); // bad request
			Adage adage = Adages.find(id);
			if (adage == null)
				throw new HTTPException(404); // not found
			return adage2Xml(adage);
		}
	}

	private InputSource toInputSource(Source source) {
		InputSource input = null;
		try {
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(bos);
			trans.transform(source, result);
			input = new InputSource(new ByteArrayInputStream(bos.toByteArray()));
		} catch (Exception e) {
			throw new HTTPException(500);
		} // internal server error
		return input;
	}

	private StreamSource toSource(String str) {
		return new StreamSource(new StringReader(str));
	}

	private int getId(String qs) {
		int badId = -1; // bad ID
		String[] parts = qs.split("=");
		if (!parts[0].toLowerCase().trim().equals("id"))
			return badId;
		int goodId = badId; // for now
		try {
			goodId = Integer.parseInt(parts[1].trim());
		} catch (Exception e) {
			return badId;
		}
		return goodId;
	}

	private String toXml(Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLEncoder enc = new XMLEncoder(out);
		enc.writeObject(obj);
		enc.close();
		return out.toString();
	}

	private StreamSource adage2Xml(Adage adage) {
		String str = toXml(adage);
		return toSource(str);
	}

	private StreamSource adages2Xml() {
		String str = toXml(Adages.getList());
		return toSource(str);
	}
}
