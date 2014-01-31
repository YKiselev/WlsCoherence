
set WLS=C:\AppServers\wls12c
rem set WLS=C:\Oracle\wls12c

java -cp "%WLS%/coherence/lib/coherence.jar;%WLS%/coherence/lib/coherence-hibernate.jar;cache-api/target/cache-api-1.0-SNAPSHOT.jar;cache/target/cache-1.0-SNAPSHOT.jar;cache/target/classes/lib/*" -Dtangosol.coherence.cacheconfig=cache/src/main/resources/META-INF/gar-cache-config.xml -Dtangosol.pof.config=cache-api/src/main/resources/cache-api-pof-config.xml -Dtangosol.coherence.distributed.localstorage=true -Dtangosol.coherence.mode=prod -Dtangosol.pof.enabled=true -Dtangosol.coherence.override=tangosol-coherence-override.xml -Dtangosol.coherence.management=all -Dtangosol.coherence.management.remote=true -Duser.country=US -Duser.language=en com.tangosol.net.DefaultCacheServer