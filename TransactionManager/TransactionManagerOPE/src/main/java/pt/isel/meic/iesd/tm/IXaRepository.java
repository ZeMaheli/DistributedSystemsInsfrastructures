package pt.isel.meic.iesd.tm;

import java.net.MalformedURLException;

public interface IXaRepository {
    IXA getManager(Resource resource) throws MalformedURLException;
}
