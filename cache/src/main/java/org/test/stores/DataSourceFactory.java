package org.test.stores;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.test.Config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Locale;

/**
 * Created by Uze on 31.12.13.
 */
public final class DataSourceFactory {

    enum Holder {
        SINGLETONE;

        private final DataSource dataSource;

        Holder() {
            final Configuration factoryConfig = Config.get().subset("dataSourceFactory");
            final String jndiName = factoryConfig.getString("jndiName");
            DataSource ds = null;
            if (!StringUtils.isEmpty(jndiName)) {
                try {
                    Context ctx = new InitialContext();
                    ds = (DataSource) ctx.lookup(jndiName);
                } catch (NamingException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Locale.setDefault(Locale.ENGLISH);

                BasicDataSource bds = new BasicDataSource();

                final Configuration cfg = factoryConfig.subset("dataSource");

                bds.setDriverClassName(cfg.getString("driver"));
                bds.setUsername(cfg.getString("username"));
                bds.setPassword(cfg.getString("password"));
                bds.setUrl(cfg.getString("url"));
                bds.setDefaultAutoCommit(false);

                bds.setMaxActive(5);
                bds.setMaxIdle(1);
                bds.setInitialSize(1);
                bds.setValidationQuery(cfg.getString("validationQuery"));

                ds = bds;
            }
            this.dataSource = ds;
        }
    }

    private DataSourceFactory() {
    }

    public static DataSource getDataSource() {
        if (Holder.SINGLETONE.dataSource == null) {
            throw new RuntimeException("DataSource is null! Check configuration file.");
        }
        return Holder.SINGLETONE.dataSource;
    }
}
