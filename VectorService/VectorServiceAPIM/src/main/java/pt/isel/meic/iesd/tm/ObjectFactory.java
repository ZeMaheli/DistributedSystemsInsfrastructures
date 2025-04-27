
package pt.isel.meic.iesd.tm;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pt.isel.meic.iesd.tm package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _Resource_QNAME = new QName("http://pt.isel.meic.iesd.tm.ax", "Resource");
    private static final QName _Register_QNAME = new QName("http://pt.isel.meic.iesd.tm.ax", "register");
    private static final QName _RegisterResponse_QNAME = new QName("http://pt.isel.meic.iesd.tm.ax", "registerResponse");
    private static final QName _Unregister_QNAME = new QName("http://pt.isel.meic.iesd.tm.ax", "unregister");
    private static final QName _UnregisterResponse_QNAME = new QName("http://pt.isel.meic.iesd.tm.ax", "unregisterResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pt.isel.meic.iesd.tm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Resource }
     * 
     * @return
     *     the new instance of {@link Resource }
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link Register }
     * 
     * @return
     *     the new instance of {@link Register }
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     * @return
     *     the new instance of {@link RegisterResponse }
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link Unregister }
     * 
     * @return
     *     the new instance of {@link Unregister }
     */
    public Unregister createUnregister() {
        return new Unregister();
    }

    /**
     * Create an instance of {@link UnregisterResponse }
     * 
     * @return
     *     the new instance of {@link UnregisterResponse }
     */
    public UnregisterResponse createUnregisterResponse() {
        return new UnregisterResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Resource }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Resource }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.ax", name = "Resource")
    public JAXBElement<Resource> createResource(Resource value) {
        return new JAXBElement<>(_Resource_QNAME, Resource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Register }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.ax", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.ax", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Unregister }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Unregister }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.ax", name = "unregister")
    public JAXBElement<Unregister> createUnregister(Unregister value) {
        return new JAXBElement<>(_Unregister_QNAME, Unregister.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnregisterResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UnregisterResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.ax", name = "unregisterResponse")
    public JAXBElement<UnregisterResponse> createUnregisterResponse(UnregisterResponse value) {
        return new JAXBElement<>(_UnregisterResponse_QNAME, UnregisterResponse.class, null, value);
    }

}
