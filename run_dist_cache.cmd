java -cp "C:/AppServers/wls12c/coherence/lib/coherence.jar;C:/AppServers/wls12c/coherence/lib/coherence-hibernate.jar;cache-api/target/cache-api-1.0-SNAPSHOT.jar;cache/target/cache-1.0-SNAPSHOT.jar;cache/target/classes/lib/*" -Dtangosol.coherence.cacheconfig=cache/src/main/resources/META-INF/gar-cache-config.xml -Dtangosol.coherence.distributed.localstorage=true -Dtangosol.coherence.mode=prod -Dtangosol.pof.enabled=true -Dtangosol.pof.config=cache-api/src/main/resources/cache-api-pof-config.xml -Dtangosol.coherence.override=tangosol-coherence-override.xml -Dtangosol.coherence.management=all -Dtangosol.coherence.management.remote=true -Duser.country=US -Duser.language=en com.tangosol.net.DefaultCacheServer
