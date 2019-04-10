package soap_web_services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import rest_web_services.Adage;

@WebService
//@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public class SoapAdageProvider {
	
@WebMethod(operationName="getAdage")
	public Adage doGet() {
		Adage adage=new Adage();
		adage.setWordCount(12);
		adage.setWords("I am SOAP Service");
		return adage;
	}
}
