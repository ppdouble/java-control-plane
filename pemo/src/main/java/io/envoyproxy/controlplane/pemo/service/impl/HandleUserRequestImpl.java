package io.envoyproxy.controlplane.pemo.service.impl;

import com.google.common.collect.ImmutableList;
import io.envoyproxy.controlplane.cache.SnapshotConsistencyException;
import io.envoyproxy.controlplane.cache.TestResources;
import io.envoyproxy.controlplane.cache.v3.SimpleCache;
import io.envoyproxy.controlplane.cache.v3.Snapshot;
import io.envoyproxy.controlplane.pemo.service.HandleUserRequest;
import io.envoyproxy.envoy.config.cluster.v3.Cluster;
import org.springframework.stereotype.Service;

import static io.envoyproxy.controlplane.pemo.grpcserver.GrpcServer.getCache;

/*
 * When receive request from controller, create new snapshot and send to envoy.
 * Since
 */
@Service
public class HandleUserRequestImpl implements HandleUserRequest {

    private static final String GROUP = "key";

    @Override
    public void handleRequest(String c, String ip, Integer port, String version) {

        // Using the cache known by Grpc Server
        SimpleCache<String> cache = getCache();

        // create snapshot
        Snapshot snapshot = createSnapshot(c, ip, port, version);

        try {
            snapshot.ensureConsistent();
        } catch (SnapshotConsistencyException e) {
            System.out.println("Check Snapshot Consistency Failure");
            e.printStackTrace();
        }
        // setsnapshot to envoy
        cache.setSnapshot(
            GROUP,
            snapshot);

    }

    public Snapshot createSnapshot (String c, String ip, Integer port, String version) {

        return Snapshot.create(
            ImmutableList.of(
                TestResources.createCluster(
                    c, ip, port, Cluster.DiscoveryType.STATIC)),
            ImmutableList.of(),
            ImmutableList.of(),
            ImmutableList.of(),
            ImmutableList.of(),
            version);
    }
}
