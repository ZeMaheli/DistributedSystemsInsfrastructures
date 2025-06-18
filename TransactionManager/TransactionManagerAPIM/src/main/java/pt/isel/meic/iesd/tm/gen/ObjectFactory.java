
package pt.isel.meic.iesd.tm.gen;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pt.isel.meic.iesd.tm.gen package. 
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

    private static final QName _Commit_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "commit");
    private static final QName _CommitResponse_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "commitResponse");
    private static final QName _Prepare_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "prepare");
    private static final QName _PrepareResponse_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "prepareResponse");
    private static final QName _Rollback_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "rollback");
    private static final QName _RollbackResponse_QNAME = new QName("http://pt.isel.meic.iesd.tm.xa", "rollbackResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pt.isel.meic.iesd.tm.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Commit }
     * 
     * @return
     *     the new instance of {@link Commit }
     */
    public Commit createCommit() {
        return new Commit();
    }

    /**
     * Create an instance of {@link CommitResponse }
     * 
     * @return
     *     the new instance of {@link CommitResponse }
     */
    public CommitResponse createCommitResponse() {
        return new CommitResponse();
    }

    /**
     * Create an instance of {@link Prepare }
     * 
     * @return
     *     the new instance of {@link Prepare }
     */
    public Prepare createPrepare() {
        return new Prepare();
    }

    /**
     * Create an instance of {@link PrepareResponse }
     * 
     * @return
     *     the new instance of {@link PrepareResponse }
     */
    public PrepareResponse createPrepareResponse() {
        return new PrepareResponse();
    }

    /**
     * Create an instance of {@link Rollback }
     * 
     * @return
     *     the new instance of {@link Rollback }
     */
    public Rollback createRollback() {
        return new Rollback();
    }

    /**
     * Create an instance of {@link RollbackResponse }
     * 
     * @return
     *     the new instance of {@link RollbackResponse }
     */
    public RollbackResponse createRollbackResponse() {
        return new RollbackResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Commit }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Commit }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "commit")
    public JAXBElement<Commit> createCommit(Commit value) {
        return new JAXBElement<>(_Commit_QNAME, Commit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CommitResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "commitResponse")
    public JAXBElement<CommitResponse> createCommitResponse(CommitResponse value) {
        return new JAXBElement<>(_CommitResponse_QNAME, CommitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Prepare }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Prepare }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "prepare")
    public JAXBElement<Prepare> createPrepare(Prepare value) {
        return new JAXBElement<>(_Prepare_QNAME, Prepare.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrepareResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PrepareResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "prepareResponse")
    public JAXBElement<PrepareResponse> createPrepareResponse(PrepareResponse value) {
        return new JAXBElement<>(_PrepareResponse_QNAME, PrepareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rollback }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Rollback }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "rollback")
    public JAXBElement<Rollback> createRollback(Rollback value) {
        return new JAXBElement<>(_Rollback_QNAME, Rollback.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RollbackResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RollbackResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://pt.isel.meic.iesd.tm.xa", name = "rollbackResponse")
    public JAXBElement<RollbackResponse> createRollbackResponse(RollbackResponse value) {
        return new JAXBElement<>(_RollbackResponse_QNAME, RollbackResponse.class, null, value);
    }

}
