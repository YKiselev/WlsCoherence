package org.test;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by Uze on 25.01.14.
 */
public class HibernateConfig {

    enum Holder {

        INSTANCE;

        private final AnnotationConfiguration configuration = new AnnotationConfiguration();

        Holder() {
            configuration.configure();
        }
    }

    public static SessionFactory createSessionFactory() {
        return Holder.INSTANCE.configuration.buildSessionFactory();
    }
}
