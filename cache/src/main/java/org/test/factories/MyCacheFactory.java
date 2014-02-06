package org.test.factories;

import com.tangosol.net.ExtensibleConfigurableCacheFactory;
import com.tangosol.run.xml.XmlElement;

/**
 * Created by Uze on 05.02.14.
 */
public class MyCacheFactory extends ExtensibleConfigurableCacheFactory {

    public MyCacheFactory(String path) {
        super(ExtensibleConfigurableCacheFactory.DependenciesHelper.newInstance(path));
        System.out.println("!!!!!!!!" + getClass().getName() + ".<init>()");

    }

    public void setConfig(XmlElement cfg) {
        System.out.println("!!!!!!!!" + getClass().getName() + ".setConfig(" + cfg.getClass() + ")");
    }
}
