# VectorServiceAPIM

Defines the IVector Interface by declaring

The interface structure 

    public interface IVector { ... }

and its interface body with methods declaration

    @WebMethod
    int read(int pos);

    @WebMethod
    void write(int pos, int n);

    @WebMethod
    String invariantCheck();


The implementation in the VectorServiceOPE project