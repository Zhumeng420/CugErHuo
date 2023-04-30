package com.example.cugerhuo.elasticsearch;



import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author zhumeng
 * @time 2023 04/30/21:30
 *
 */
public class FuzzSearch {
    /**
     * 线程安全且高效的单例模式实例
     */
    private static volatile RestHighLevelClient client;
    /**
     * 连接es集群
     * @return 成功了就返回true
     */
    public  static RestHighLevelClient getInstence(){
        if(client==null){
            synchronized (FuzzSearch.class){
                if (client==null){
                    RestHighLevelClient client = new RestHighLevelClient(
                            RestClient.builder(
                                    new HttpHost("localhost", 9200, "http")));
                }
            }
        }
        return client;
    }
    /**
     * 向es集群中模糊查询
     * @return json格式的查询结果
     */
    public static void getUser() throws IOException {
        SearchRequest searchRequest = new SearchRequest("username_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("username", "风情扬");
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
