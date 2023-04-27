package nurier.scraping.elasticsearch;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.repositories.fs.FsRepository;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.SnapshotInfo;

public class SnapshotService {

	public void getRepo() throws Exception {
		ElasticsearchService es = new ElasticsearchService();
		RestHighLevelClient client = es.getClient();
		GetRepositoriesRequest request = new GetRepositoriesRequest();
		try {
			GetRepositoriesResponse response = client.snapshot().getRepository(request, RequestOptions.DEFAULT);
			List<RepositoryMetaData> repositoryMetaDataResponse = response.repositories();
			System.out.println(repositoryMetaDataResponse.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
	}

	public void createRepo(String repoName, String repoLocation) throws Exception {
		ElasticsearchService es = new ElasticsearchService();
		RestHighLevelClient client = es.getClient();
		PutRepositoryRequest request = new PutRepositoryRequest();
		request.name(repoName);
		request.type(FsRepository.TYPE);

		Settings settings = Settings.builder().put(FsRepository.LOCATION_SETTING.getKey(), repoLocation)
				.put(FsRepository.COMPRESS_SETTING.getKey(), true).build();

		request.settings(settings);

		try {
			AcknowledgedResponse response = client.snapshot().createRepository(request, RequestOptions.DEFAULT);
			boolean acknowledged = response.isAcknowledged();
			System.out.println(acknowledged);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
	}

	public void deleteRepo(String repoName) throws Exception {
		ElasticsearchService es = new ElasticsearchService();
		RestHighLevelClient client = es.getClient();
		DeleteRepositoryRequest request = new DeleteRepositoryRequest(repoName);
		try {
			AcknowledgedResponse response = client.snapshot().deleteRepository(request, RequestOptions.DEFAULT);
			boolean acknowledged = response.isAcknowledged();
			System.out.println(acknowledged);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
	}

	public void createSnapshot(String repoName, String indexName) throws Exception {
		ElasticsearchService es = new ElasticsearchService();
		RestHighLevelClient client = es.getClient();

		CreateSnapshotRequest request = new CreateSnapshotRequest();
		request.repository(repoName);
		request.snapshot(indexName + "_snapshot");
		request.indices(indexName);
		request.waitForCompletion(true);

		try {
			CreateSnapshotResponse response = client.snapshot().create(request, RequestOptions.DEFAULT);
			SnapshotInfo snapshotInfo = response.getSnapshotInfo();
			RestStatus status = response.status();
			System.out.println(status.getStatus());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
	}
}
