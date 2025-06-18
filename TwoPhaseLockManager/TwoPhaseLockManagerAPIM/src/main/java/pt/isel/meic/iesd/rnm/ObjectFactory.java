
package pt.isel.meic.iesd.rnm;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pt.isel.meic.iesd.rnm package. 
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

    private static final QName _Exception_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "Exception");
    private static final QName _AddLockHeld_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "addLockHeld");
    private static final QName _AddLockHeldResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "addLockHeldResponse");
    private static final QName _AddPendingRequest_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "addPendingRequest");
    private static final QName _AddPendingRequestResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "addPendingRequestResponse");
    private static final QName _CheckRmStatus_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "checkRmStatus");
    private static final QName _CheckRmStatusResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "checkRmStatusResponse");
    private static final QName _ClearHolder_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "clearHolder");
    private static final QName _ClearHolderResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "clearHolderResponse");
    private static final QName _ClearLocksHeld_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "clearLocksHeld");
    private static final QName _ClearLocksHeldResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "clearLocksHeldResponse");
    private static final QName _GetHolder_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getHolder");
    private static final QName _GetHolderResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getHolderResponse");
    private static final QName _GetLocksHeld_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getLocksHeld");
    private static final QName _GetLocksHeldResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getLocksHeldResponse");
    private static final QName _GetPendingRequest_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getPendingRequest");
    private static final QName _GetPendingRequestResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getPendingRequestResponse");
    private static final QName _GetPendingTransactions_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getPendingTransactions");
    private static final QName _GetPendingTransactionsResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "getPendingTransactionsResponse");
    private static final QName _RemovePendingRequest_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "removePendingRequest");
    private static final QName _RemovePendingRequestResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "removePendingRequestResponse");
    private static final QName _SetHolder_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "setHolder");
    private static final QName _SetHolderResponse_QNAME = new QName("http://pt.isel.meic.iesd.rnmtplm", "setHolderResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pt.isel.meic.iesd.rnm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Exception }
     * 
     * @return
     *     the new instance of {@link Exception }
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link AddLockHeld }
     * 
     * @return
     *     the new instance of {@link AddLockHeld }
     */
    public AddLockHeld createAddLockHeld() {
        return new AddLockHeld();
    }

    /**
     * Create an instance of {@link AddLockHeldResponse }
     * 
     * @return
     *     the new instance of {@link AddLockHeldResponse }
     */
    public AddLockHeldResponse createAddLockHeldResponse() {
        return new AddLockHeldResponse();
    }

    /**
     * Create an instance of {@link AddPendingRequest }
     * 
     * @return
     *     the new instance of {@link AddPendingRequest }
     */
    public AddPendingRequest createAddPendingRequest() {
        return new AddPendingRequest();
    }

    /**
     * Create an instance of {@link AddPendingRequestResponse }
     * 
     * @return
     *     the new instance of {@link AddPendingRequestResponse }
     */
    public AddPendingRequestResponse createAddPendingRequestResponse() {
        return new AddPendingRequestResponse();
    }

    /**
     * Create an instance of {@link CheckRmStatus }
     * 
     * @return
     *     the new instance of {@link CheckRmStatus }
     */
    public CheckRmStatus createCheckRmStatus() {
        return new CheckRmStatus();
    }

    /**
     * Create an instance of {@link CheckRmStatusResponse }
     * 
     * @return
     *     the new instance of {@link CheckRmStatusResponse }
     */
    public CheckRmStatusResponse createCheckRmStatusResponse() {
        return new CheckRmStatusResponse();
    }

    /**
     * Create an instance of {@link ClearHolder }
     * 
     * @return
     *     the new instance of {@link ClearHolder }
     */
    public ClearHolder createClearHolder() {
        return new ClearHolder();
    }

    /**
     * Create an instance of {@link ClearHolderResponse }
     * 
     * @return
     *     the new instance of {@link ClearHolderResponse }
     */
    public ClearHolderResponse createClearHolderResponse() {
        return new ClearHolderResponse();
    }

    /**
     * Create an instance of {@link ClearLocksHeld }
     * 
     * @return
     *     the new instance of {@link ClearLocksHeld }
     */
    public ClearLocksHeld createClearLocksHeld() {
        return new ClearLocksHeld();
    }

    /**
     * Create an instance of {@link ClearLocksHeldResponse }
     * 
     * @return
     *     the new instance of {@link ClearLocksHeldResponse }
     */
    public ClearLocksHeldResponse createClearLocksHeldResponse() {
        return new ClearLocksHeldResponse();
    }

    /**
     * Create an instance of {@link GetHolder }
     * 
     * @return
     *     the new instance of {@link GetHolder }
     */
    public GetHolder createGetHolder() {
        return new GetHolder();
    }

    /**
     * Create an instance of {@link GetHolderResponse }
     * 
     * @return
     *     the new instance of {@link GetHolderResponse }
     */
    public GetHolderResponse createGetHolderResponse() {
        return new GetHolderResponse();
    }

    /**
     * Create an instance of {@link GetLocksHeld }
     * 
     * @return
     *     the new instance of {@link GetLocksHeld }
     */
    public GetLocksHeld createGetLocksHeld() {
        return new GetLocksHeld();
    }

    /**
     * Create an instance of {@link GetLocksHeldResponse }
     * 
     * @return
     *     the new instance of {@link GetLocksHeldResponse }
     */
    public GetLocksHeldResponse createGetLocksHeldResponse() {
        return new GetLocksHeldResponse();
    }

    /**
     * Create an instance of {@link GetPendingRequest }
     * 
     * @return
     *     the new instance of {@link GetPendingRequest }
     */
    public GetPendingRequest createGetPendingRequest() {
        return new GetPendingRequest();
    }

    /**
     * Create an instance of {@link GetPendingRequestResponse }
     * 
     * @return
     *     the new instance of {@link GetPendingRequestResponse }
     */
    public GetPendingRequestResponse createGetPendingRequestResponse() {
        return new GetPendingRequestResponse();
    }

    /**
     * Create an instance of {@link GetPendingTransactions }
     * 
     * @return
     *     the new instance of {@link GetPendingTransactions }
     */
    public GetPendingTransactions createGetPendingTransactions() {
        return new GetPendingTransactions();
    }

    /**
     * Create an instance of {@link GetPendingTransactionsResponse }
     * 
     * @return
     *     the new instance of {@link GetPendingTransactionsResponse }
     */
    public GetPendingTransactionsResponse createGetPendingTransactionsResponse() {
        return new GetPendingTransactionsResponse();
    }

    /**
     * Create an instance of {@link RemovePendingRequest }
     * 
     * @return
     *     the new instance of {@link RemovePendingRequest }
     */
    public RemovePendingRequest createRemovePendingRequest() {
        return new RemovePendingRequest();
    }

    /**
     * Create an instance of {@link RemovePendingRequestResponse }
     * 
     * @return
     *     the new instance of {@link RemovePendingRequestResponse }
     */
    public RemovePendingRequestResponse createRemovePendingRequestResponse() {
        return new RemovePendingRequestResponse();
    }

    /**
     * Create an instance of {@link SetHolder }
     * 
     * @return
     *     the new instance of {@link SetHolder }
     */
    public SetHolder createSetHolder() {
        return new SetHolder();
    }

    /**
     * Create an instance of {@link SetHolderResponse }
     * 
     * @return
     *     the new instance of {@link SetHolderResponse }
     */
    public SetHolderResponse createSetHolderResponse() {
        return new SetHolderResponse();
    }

    /**
     * Create an instance of {@link Lock }
     * 
     * @return
     *     the new instance of {@link Lock }
     */
    public Lock createLock() {
        return new Lock();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddLockHeld }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddLockHeld }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "addLockHeld")
    public JAXBElement<AddLockHeld> createAddLockHeld(AddLockHeld value) {
        return new JAXBElement<>(_AddLockHeld_QNAME, AddLockHeld.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddLockHeldResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddLockHeldResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "addLockHeldResponse")
    public JAXBElement<AddLockHeldResponse> createAddLockHeldResponse(AddLockHeldResponse value) {
        return new JAXBElement<>(_AddLockHeldResponse_QNAME, AddLockHeldResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddPendingRequest }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddPendingRequest }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "addPendingRequest")
    public JAXBElement<AddPendingRequest> createAddPendingRequest(AddPendingRequest value) {
        return new JAXBElement<>(_AddPendingRequest_QNAME, AddPendingRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddPendingRequestResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddPendingRequestResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "addPendingRequestResponse")
    public JAXBElement<AddPendingRequestResponse> createAddPendingRequestResponse(AddPendingRequestResponse value) {
        return new JAXBElement<>(_AddPendingRequestResponse_QNAME, AddPendingRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckRmStatus }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CheckRmStatus }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "checkRmStatus")
    public JAXBElement<CheckRmStatus> createCheckRmStatus(CheckRmStatus value) {
        return new JAXBElement<>(_CheckRmStatus_QNAME, CheckRmStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckRmStatusResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CheckRmStatusResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "checkRmStatusResponse")
    public JAXBElement<CheckRmStatusResponse> createCheckRmStatusResponse(CheckRmStatusResponse value) {
        return new JAXBElement<>(_CheckRmStatusResponse_QNAME, CheckRmStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearHolder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClearHolder }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "clearHolder")
    public JAXBElement<ClearHolder> createClearHolder(ClearHolder value) {
        return new JAXBElement<>(_ClearHolder_QNAME, ClearHolder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearHolderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClearHolderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "clearHolderResponse")
    public JAXBElement<ClearHolderResponse> createClearHolderResponse(ClearHolderResponse value) {
        return new JAXBElement<>(_ClearHolderResponse_QNAME, ClearHolderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearLocksHeld }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClearLocksHeld }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "clearLocksHeld")
    public JAXBElement<ClearLocksHeld> createClearLocksHeld(ClearLocksHeld value) {
        return new JAXBElement<>(_ClearLocksHeld_QNAME, ClearLocksHeld.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearLocksHeldResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClearLocksHeldResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "clearLocksHeldResponse")
    public JAXBElement<ClearLocksHeldResponse> createClearLocksHeldResponse(ClearLocksHeldResponse value) {
        return new JAXBElement<>(_ClearLocksHeldResponse_QNAME, ClearLocksHeldResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHolder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetHolder }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getHolder")
    public JAXBElement<GetHolder> createGetHolder(GetHolder value) {
        return new JAXBElement<>(_GetHolder_QNAME, GetHolder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHolderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetHolderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getHolderResponse")
    public JAXBElement<GetHolderResponse> createGetHolderResponse(GetHolderResponse value) {
        return new JAXBElement<>(_GetHolderResponse_QNAME, GetHolderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocksHeld }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetLocksHeld }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getLocksHeld")
    public JAXBElement<GetLocksHeld> createGetLocksHeld(GetLocksHeld value) {
        return new JAXBElement<>(_GetLocksHeld_QNAME, GetLocksHeld.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocksHeldResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetLocksHeldResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getLocksHeldResponse")
    public JAXBElement<GetLocksHeldResponse> createGetLocksHeldResponse(GetLocksHeldResponse value) {
        return new JAXBElement<>(_GetLocksHeldResponse_QNAME, GetLocksHeldResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPendingRequest }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPendingRequest }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getPendingRequest")
    public JAXBElement<GetPendingRequest> createGetPendingRequest(GetPendingRequest value) {
        return new JAXBElement<>(_GetPendingRequest_QNAME, GetPendingRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPendingRequestResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPendingRequestResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getPendingRequestResponse")
    public JAXBElement<GetPendingRequestResponse> createGetPendingRequestResponse(GetPendingRequestResponse value) {
        return new JAXBElement<>(_GetPendingRequestResponse_QNAME, GetPendingRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPendingTransactions }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPendingTransactions }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getPendingTransactions")
    public JAXBElement<GetPendingTransactions> createGetPendingTransactions(GetPendingTransactions value) {
        return new JAXBElement<>(_GetPendingTransactions_QNAME, GetPendingTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPendingTransactionsResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPendingTransactionsResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "getPendingTransactionsResponse")
    public JAXBElement<GetPendingTransactionsResponse> createGetPendingTransactionsResponse(GetPendingTransactionsResponse value) {
        return new JAXBElement<>(_GetPendingTransactionsResponse_QNAME, GetPendingTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemovePendingRequest }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemovePendingRequest }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "removePendingRequest")
    public JAXBElement<RemovePendingRequest> createRemovePendingRequest(RemovePendingRequest value) {
        return new JAXBElement<>(_RemovePendingRequest_QNAME, RemovePendingRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemovePendingRequestResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemovePendingRequestResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "removePendingRequestResponse")
    public JAXBElement<RemovePendingRequestResponse> createRemovePendingRequestResponse(RemovePendingRequestResponse value) {
        return new JAXBElement<>(_RemovePendingRequestResponse_QNAME, RemovePendingRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetHolder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetHolder }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "setHolder")
    public JAXBElement<SetHolder> createSetHolder(SetHolder value) {
        return new JAXBElement<>(_SetHolder_QNAME, SetHolder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetHolderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetHolderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.rnmtplm", name = "setHolderResponse")
    public JAXBElement<SetHolderResponse> createSetHolderResponse(SetHolderResponse value) {
        return new JAXBElement<>(_SetHolderResponse_QNAME, SetHolderResponse.class, null, value);
    }

}
