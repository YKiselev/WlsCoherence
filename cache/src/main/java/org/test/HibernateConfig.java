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
        private final SessionFactory sessionFactory;

        Holder() {
            configuration.configure();
            this.sessionFactory = configuration.buildSessionFactory();
        }
    }

    public static SessionFactory getSessionFactory() {
        return Holder.INSTANCE.sessionFactory;//.configuration.buildSessionFactory();
    }
}
